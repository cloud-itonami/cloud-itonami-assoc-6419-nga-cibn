(ns association-facts-test
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io] [clojure.java.shell :as shell]
            [clojure.test :refer [deftest is testing]]
            [kotoba.compiler.core :as compiler] [kotoba.kir :as ir]))
(def source (slurp "src/association_facts.kotoba"))
(defn call [kir f & xs] (ir/execute kir f (vec xs)))
(defn present [x] (when (second x) (nth x 2)))
(def fields ["id" "title" "association" "isic" "country" "kind" "url" "url-provenance"
             "established-date" "last-revised-date" "retrieved-at"])
(def expected
  [{"id" "cibn.founding-1963-11-28" "title" "CIBN founding (Corporate Information)"
    "association" "cibn" "isic" "6419" "country" "NGA" "kind" "governance-program"
    "url" "https://cibng.org/corporate-information/" "url-provenance" "official-cibng-org"
    "established-date" "1963-11-28" "last-revised-date" nil "retrieved-at" "2026-07-17"}
   {"id" "cibn.chartered-status-act-12-1990"
    "title" "CIBN Chartered Status (Federal Government Act No. 12 of 1990)"
    "association" "cibn" "isic" "6419" "country" "NGA" "kind" "governance-program"
    "url" "https://cibng.org/corporate-information/" "url-provenance" "official-cibng-org"
    "established-date" "1990-05-18" "last-revised-date" nil "retrieved-at" "2026-07-17"}])
(deftest reference-preserves-authority
  (let [kir (:kir (compiler/compile-source source :js-kotoba-v1))
        observed (mapv (fn [i] (into {} (map (fn [f] [f (present (call kir 'entry-field "cibn" i f))]) fields))) [0 1])]
    (is (= expected observed))
    (is (= ["1963-11-28" "1990-05-18"] (mapv #(present (call kir 'entry-field "cibn" % "established-date")) [0 1])))
    (is (= [nil nil] (mapv #(present (call kir 'entry-field "cibn" % "last-revised-date")) [0 1])))
    (is (= [["governance"] ["governance"]]
           (mapv (fn [i] (mapv #(present (call kir 'topic "cibn" i %)) (range (call kir 'topic-count "cibn" i)))) [0 1])))
    (is (= ["cibn.founding-1963-11-28" "cibn.chartered-status-act-12-1990"]
           (mapv #(present (call kir 'by-topic-id "cibn" "governance" %)) [0 1])))
    (is (= #{} (set (:effects kir))))
    (testing "fail closed"
      (is (zero? (call kir 'entry-count "chartered-institute-of-bankers-of-nigeria")))
      (is (zero? (call kir 'entry-count "tbb")))
      (is (nil? (present (call kir 'entry-field "cibn" 2 "id"))))
      (is (nil? (present (call kir 'entry-field "cibn" 0 "last-revised-date"))))
      (is (nil? (present (call kir 'topic "cibn" 1 1))))
      (is (zero? (call kir 'by-topic-count "cibn" "labor")))
      (is (nil? (present (call kir 'by-topic-id "cibn" "governance" 2)))))))
(defn compiler-root [] (nth (iterate #(.getParent ^java.nio.file.Path %)
  (java.nio.file.Path/of (.toURI (io/resource "kotoba/compiler/core.clj")))) 4))
(defn base64 [x] (.encodeToString (java.util.Base64/getEncoder) x))
(deftest restricted-js-and-wasm-conform-semantically
  (let [js (compiler/compile-source source :js-kotoba-v1) wasm (compiler/compile-source source :wasm32-browser-kotoba-v1)
        js64 (base64 (.getBytes ^String (:source js) "UTF-8")) wasm64 (base64 ^bytes (:bytes wasm))
        p (shell/sh "node" "--input-type=module" "-e"
            (str "import(process.argv[1]).then(async h=>{const j=await import('data:text/javascript;base64," js64 "');const w=await h.instantiateKotoba(Buffer.from(process.argv[2],'base64'));const r=x=>{if(x['entry-field']('cibn',0n,'established-date')[2]!=='1963-11-28'||x['entry-field']('cibn',1n,'established-date')[2]!=='1990-05-18'||x['entry-field']('cibn',0n,'last-revised-date')[1]!==false)throw Error('dates');if(x['by-topic-count']('cibn','governance')!==2n||x['by-topic-id']('cibn','governance',1n)[2]!=='cibn.chartered-status-act-12-1990'||x['entry-count']('chartered-institute-of-bankers-of-nigeria')!==0n||x['entry-count']('tbb')!==0n)throw Error('authority');};r(j.instantiateKotoba({}));r(w.instance.exports)}).catch(e=>{console.error(e);process.exit(99)})")
            (.toString (.toUri (.resolve (compiler-root) "runtime/browser-host.mjs"))) wasm64)]
    (is (zero? (:exit p)) (str (:out p) (:err p)))))
(deftest production-source-authority
  (is (= ["src/association_facts.kotoba"] (->> (file-seq (io/file "src")) (filter #(.isFile %)) (map str) sort vec))))

(deftest resident-component-canary
  (let [canary-source (slurp "qualification/resident_canary.kotoba")
        manifest (edn/read-string (slurp "murakumo.component.edn"))
        artifact (compiler/compile-component
                  canary-source {}
                  {:budgets (:budgets manifest)})
        component (java.nio.file.Files/createTempFile
                   "cloud-itonami-cibn-" ".component.wasm"
                   (make-array java.nio.file.attribute.FileAttribute 0))]
    (try
      (java.nio.file.Files/write
       component ^bytes (:bytes artifact)
       (make-array java.nio.file.OpenOption 0))
      (let [run (shell/sh "wasmtime" "run" "--invoke" "main()"
                          (.toString component))]
        (is (zero? (:exit run)) (str (:out run) (:err run)))
        (is (= (str (:expected-result manifest))
               (.trim ^String (:out run)))))
      (is (= :murakumo.kototama-component/v1 (:format manifest)))
      (is (= :wasm-component-kotoba-v1 (:target artifact) (:target manifest)))
      (is (= #{} (:capabilities artifact)))
      (is (= [] (:imports artifact)))
      (is (false? (get-in artifact [:admission-request :ambient-wasi])))
      (is (= (:budgets manifest) (:budgets artifact)))
      (finally
        (java.nio.file.Files/deleteIfExists component)))))
