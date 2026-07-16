(ns association.facts
  "Industry rule/history catalog for the Chartered Institute of Bankers
  of Nigeria (CIBN) -- a 40th industry-association-level source (see
  cloud-itonami-assoc-6419-jpn-zenginkyo, -6419-deu-bankenverband,
  -6419-fra-fbf, -6419-aus-aba, -6419-are-ubf, -6419-vnm-vnba,
  -6419-phl-bap, -6419-esp-aeb, -6419-tur-tbb for the first nine) per
  ADR-2607141700 (cloud-itonami-compliance-fact-federation). The TENTH
  entry aligned to ISIC 6419 (other monetary intermediation /
  banking). Fills Nigeria's previously-open association-axis gap
  (noted honestly at tick 117) -- Nigeria now has real, individually
  verified facts across ALL THREE axes (municipality:
  cloud-itonami-municipality-nga-abuja, tick 116; country:
  cloud-itonami-iso3166-nga statute.facts, tick 117; association:
  this entry, tick 118).

  Both entries directly WebFetch-verified against cibng.org's own
  'Corporate Information' page: the 28 November 1963 founding as the
  Local Centre of the Institute of Bankers, London, in Nigeria, and
  the verbatim-quoted text 'the attainment of a Chartered Status,
  achieved on May 18th, 1990 by the Federal Government Act No. 12 of
  1990' (now re-enacted as CIBN Act No. 5 of 2007, per the same
  page).

  An association not in `catalog` has NO spec-basis, full stop; never
  fabricate one.")

(def catalog
  "association-slug -> vector of association-rule entries."
  {"cibn"
   [{:association-rule/id "cibn.founding-1963-11-28"
     :association-rule/title "CIBN founding (Corporate Information)"
     :association-rule/association "cibn"
     :association-rule/isic "6419"
     :association-rule/country "NGA"
     :association-rule/kind :governance-program
     :association-rule/url "https://cibng.org/corporate-information/"
     :association-rule/url-provenance :official-cibng-org
     :association-rule/established-date "1963-11-28"
     :association-rule/retrieved-at "2026-07-17"
     :association-rule/topic #{:governance}}
    {:association-rule/id "cibn.chartered-status-act-12-1990"
     :association-rule/title "CIBN Chartered Status (Federal Government Act No. 12 of 1990)"
     :association-rule/association "cibn"
     :association-rule/isic "6419"
     :association-rule/country "NGA"
     :association-rule/kind :governance-program
     :association-rule/url "https://cibng.org/corporate-information/"
     :association-rule/url-provenance :official-cibng-org
     :association-rule/established-date "1990-05-18"
     :association-rule/retrieved-at "2026-07-17"
     :association-rule/topic #{:governance}}]})

(defn spec-basis [association] (get catalog association))

(defn coverage
  ([] (coverage (keys catalog)))
  ([associations]
   (let [have (filter catalog associations)
         missing (remove catalog associations)]
     {:requested (count associations)
      :covered (count have)
      :covered-associations (vec (sort have))
      :missing-associations (vec (sort missing))
      :note (str "cloud-itonami-assoc-6419-nga-cibn Wave 0 (ADR-2607141700): "
                 (count (get catalog "cibn")) " CIBN entries seeded "
                 "with cibng.org citations. "
                 "Extend `association.facts/catalog`, never fabricate an id/url.")})))

(defn by-topic [association topic]
  (filterv #(contains? (:association-rule/topic %) topic) (spec-basis association)))
