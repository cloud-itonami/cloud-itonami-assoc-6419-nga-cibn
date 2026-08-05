# cloud-itonami-assoc-6419-nga-cibn

Industry rule/history catalog for the **Chartered Institute of Bankers
of Nigeria** (CIBN) — the TENTH entry aligned to **ISIC 6419** (other
monetary intermediation / banking), alongside
[`-6419-jpn-zenginkyo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-jpn-zenginkyo)
(Japan),
[`-6419-deu-bankenverband`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-deu-bankenverband)
(Germany),
[`-6419-fra-fbf`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-fra-fbf)
(France),
[`-6419-aus-aba`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-aus-aba)
(Australia),
[`-6419-are-ubf`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-are-ubf)
(UAE),
[`-6419-vnm-vnba`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-vnm-vnba)
(Vietnam),
[`-6419-phl-bap`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-phl-bap)
(Philippines),
[`-6419-esp-aeb`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-esp-aeb)
(Spain), and
[`-6419-tur-tbb`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-tur-tbb)
(Turkey).
Part of the [`cloud-itonami`](https://github.com/cloud-itonami)
compliance-fact family (ADR-2607141700,
`cloud-itonami-compliance-fact-federation`, in `com-junkawasaki/root`).

## Sourcing note

This repo fills Nigeria's previously-open association-axis gap (noted
honestly at tick 117). Nigeria now has real, individually verified
facts across all three axes: municipality
([`cloud-itonami-municipality-nga-abuja`](https://github.com/cloud-itonami/cloud-itonami-municipality-nga-abuja)),
country
([`cloud-itonami-iso3166-nga`](https://github.com/cloud-itonami/cloud-itonami-iso3166-nga)),
and association (this repo).

## Scope

A **read-only reference/archive** catalog — not an Advisor⊣Governor
actuation actor. It proposes or executes nothing on CIBN's behalf.

Coverage is reported honestly by the fail-closed exported Kotoba ABI: an
association not explicitly admitted has **no spec-basis**, full stop — never
fabricate one.

## Data

- `src/association_facts.kotoba` — the sole production catalog authority.
- `schema/association-rule.edn` — DataScript schema.
- `data/datascript-tx.edn` — derived DataScript tx-data (query this
  alongside other `cloud-itonami`/`etzhayyim` compliance-fact sources via
  `com-junkawasaki/root`'s `scripts/compliance-fact-query.cljs`).

Both entries directly WebFetch-verified against `cibng.org`'s own
"Corporate Information" page: the 28 November 1963 founding date and
the 18 May 1990 Chartered Status (Federal Government Act No. 12 of
1990).

The catalog compiles through `kotoba-lang/compiler` to the reference evaluator,
restricted JavaScript, and typed WebAssembly. Clojure/JVM and Node are test and
compiler hosts only; neither is production authority. Compatibility is checked
by observable values, typed ABI, empty effects, bounds, and fail-closed
rejections—not compiler-output byte identity.

## Resident Component canary

`qualification/resident_canary.kotoba` is the provider-free vertical slice for
the new murakumo runtime. CI compiles it to a sealed Wasm Component and executes
it with the qualified Wasmtime 42 runtime. `murakumo.component.edn` binds its
target, budgets, expected result, and canary-only placement. It neither replaces
nor duplicates `src/association_facts.kotoba`; the production catalog remains
the sole source authority, while this probe proves Component residency before
the catalog's richer string/option exports acquire qualified Canonical lowering.

The canary is resident on murakumo node `asher` as the system LaunchDaemon
`com.murakumo.kototama-component`. It binds loopback only, survived a forced
process termination through launchd `KeepAlive`, and returns `6419002`.
`qualification/murakumo-asher.edn` records the exact Component/runtime digests
and an independently verifiable node-local Ed25519 execution receipt.

`qualification/effectful_app.kotoba` is the first effectful
production-shaped slice. Kotoba owns the sequence
`http/post -> storage/transact -> llm/generate -> decision`; the host receives
only the abilities in the SHA-pinned
`qualification/effectful-capabilities.json`. Those abilities name two literal
Ollama loopback endpoints and one absolute append-only storage log. The live
asher run, forced restart, exact artifacts, three effect calls, and
independently verified receipt are recorded in
`qualification/murakumo-asher-effects.edn`.

## License

AGPL-3.0-or-later (matches the `cloud-itonami-iso3166-*` /
`-municipality-*` / `-assoc-*` / `-lei-*` convention). Policy text
itself remains CIBN's; this repo stores only citation metadata
(id/title/url/dates), not full text.
