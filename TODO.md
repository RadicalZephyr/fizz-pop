## Graph Data Model Next Steps

1. **Validate** this schema with a couple more real subgraphs (e.g. `StreamSnapshot` + `CellLift`).
2. **Decide** on the transform language: pure JQ, a very small subset of JS, or a dual-mode (`:fn/jq` *or* `:fn/js`).
3. **Write a reader/writer** that converts between this EDN and an in-memory graph so we can plug it into both the ClojureScript editor and the TS runtime.
4. **Iterate** on ergonomics—e.g., add `:default-value` hints for ports, or metadata for UI layout.
