(ns association.facts-test
  (:require [clojure.test :refer [deftest is]]
            [association.facts :as facts]))

(deftest cibn-has-spec-basis
  (let [sb (facts/spec-basis "cibn")]
    (is (= 2 (count sb)))
    (is (every? #(= "6419" (:association-rule/isic %)) sb))
    (is (every? #(= "NGA" (:association-rule/country %)) sb))))

(deftest unknown-association-has-no-spec-basis
  (is (nil? (facts/spec-basis "tbb")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["cibn" "tbb"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["tbb"] (:missing-associations c)))))

(deftest by-topic-filters
  (is (= 2 (count (facts/by-topic "cibn" :governance))))
  (is (empty? (facts/by-topic "cibn" :labor)))
  (is (empty? (facts/by-topic "tbb" :governance))))
