### 1  Top-level graph record

```edn
{:graph/id        "my-graph-v0"
 :graph/name      "FizzPop demo"
 :graph/version   0
 :graph/types     [ ... ]          ;; optional user-defined types
 :graph/nodes     [ ... ]          ;; every node appears once
 :graph/edges     [ ... ]          ;; directed connections
 :graph/compounds [ ... ]}         ;; reusable sub-graphs
```

---

### 2  Node specification

| key                                | value                               | notes                                                     |
| ---------------------------------- | ----------------------------------- | --------------------------------------------------------- |
| `:node/id`                         | `"uuid-string"`                     | stable identifier                                         |
| `:node/kind`                       | one of the enums in the Sodium list | determines default ports                                  |
| `:node/name`                       | string                              | display label                                             |
| `:node/doc`                        | string                              | markdown description                                      |
| `:node/params`                     | map                                 | operator-specific constants (e.g. filter predicate as JQ) |
| `:node/inports` / `:node/outports` | vector of **port** maps             | see below                                                 |
| `:node/type`                       | EDN type form                       | overall output type (esp. for Cells)                      |

**Port map**

```edn
{:port/name  "value"
 :port/type  <type>
 :port/doc   "..."}
```

---

### 3  Edge specification

Edges are the only place we reference nodes by ID, keeping nodes self-contained.

```edn
{:edge/id     "uuid"
 :edge/from   [:node/id "out-port"]
 :edge/to     [:node/id "in-port"]
 :edge/kind   :cell    ;; or :stream per editor guidance
 :edge/doc    "optional comment"}
```

---

### 4  Light-weight structural type system

We mimic the expressive subset of TypeScript without T-level arithmetic or conditional types:

```edn
;; primitives
{:type/kind :number} | {:type/kind :string} | {:type/kind :boolean}

;; arrays & tuples
{:type/kind :array :type/item <type>}
{:type/kind :tuple :type/items [<type1> <type2> ...]}

;; object shapes
{:type/kind :object
 :type/fields {:x {:type/kind :number}
               :y {:type/kind :number}}}

;; unions & literals
{:type/kind :union  :type/options [<typeA> <typeB>]}
{:type/kind :literal :value 42}
```

*Rationale*: this is close enough to TS for predictable interop while
still serialisable in EDN; at runtime we can convert to `io-ts`-style
validators or fall back to duck-typing.

---

### 5  Function bodies / data transforms

To avoid arbitrary TS in node functions we support **JQ expressions**
stored in `:node/params`:

```edn
{:node/id    "map-1"
 :node/kind  :StreamMap
 :node/params {:fn/jq ".value * 2"}}
```

A small eval sandbox (e.g. [gojq](https://github.com/itchyny/gojq)
compiled to WASM) is plenty for most data-mangling needs and is
side-effect-free.

---

### 6  Compound nodes (user abstractions)

```edn
{:compound/id      "uuid"
 :compound/name    "Moving average"
 :compound/inputs  [{:port/name "in"  :port/type {:type/kind :number}}]
 :compound/outputs [{:port/name "out" :port/type {:type/kind :number}}]
 :compound/graph   {:graph/nodes [...], :graph/edges [...]}}
```

Multiple instances of a compound node are inserted into the main graph
via ordinary `:node/kind :CompoundRef` nodes whose `:params` point to
the compound’s ID.

---

### 7  Minimal worked example

```edn
{:graph/id  "example"
 :graph/nodes
 [{:node/id    "const1"
   :node/kind  :CellConstant
   :node/name  "Five"
   :node/outports [{:port/name "value"
                    :port/type {:type/kind :number}}]
   :params {:value 5}}

  {:node/id   "map1"
   :node/kind :CellMap
   :node/name "x2"
   :node/inports  [{:port/name "in"
                    :port/type {:type/kind :number}}]
   :node/outports [{:port/name "out"
                    :port/type {:type/kind :number}}]
   :params {:fn/jq ". * 2"}}]

 :graph/edges
 [{:edge/id  "e1"
   :from     ["const1" "value"]
   :to       ["map1" "in"]
   :kind     :cell}]}
```
