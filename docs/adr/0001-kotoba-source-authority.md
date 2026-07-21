# ADR 0001: Kotoba is the CIBN catalog source authority

- Status: Accepted
- Date: 2026-07-21

`src/association_facts.kotoba` is the sole production source. It preserves the
official complete 1963-11-28 founding and 1990-05-18 chartered-status dates,
keeps both revision dates absent, and does not invent a separate entry for the
described 2007 reenactment. Both entries retain governance topics and the
official CIBN citation. Unknown associations, aliases, fields, topics, and
indexes fail closed; no effects are declared.

Conformance is observable semantics across the reference evaluator, restricted
JavaScript, and instantiated typed WebAssembly, including the typed ABI, bounds,
effects, and rejection behavior. Compiler-output byte identity is not a language
gate. Clojure and the JVM are compiler/test hosts only.
