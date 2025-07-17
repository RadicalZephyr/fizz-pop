# Sodium Computational Model

Sodium is a Functional Reactive Programming (FRP) framework that
provides a structured way to create reactive programs. It operates in
two distinct phases:

1. Initialization Phase:

During this phase, a directed graph is constructed through Sodium API
calls, typically within a single transaction. This graph represents
the reactive relationships between components.

2. Running Phase:

After initialization, the system processes events from external
sources through two primary interfaces:

 - `CellSinks`: Update discrete data values that change over time
 - `StreamSinks`: Send discrete events into the system, often with associated data

The setup phase can also attach listeners/call back functions to any
`Stream` to run code when that `Stream` fires an event.


## Core Concepts

 - Cells: Represent values that change over time and always have a current value. Cell updates propagate to dependent Cells but cannot trigger Stream events.
 - Streams: Represent discrete events occurring at specific points in time. Stream events can only be initiated by StreamSinks and propagate through the system according to the graph structure.
 - Transactions: All external updates are wrapped in transactions, either implicitly or explicitly. Each transaction processes completely before the next begins, ensuring consistent state.
 - Propagation Model: Updates flow through the graph in dependency order - all dependencies are processed before dependent nodes. Cell updates aren't visible to dependencies until the next transaction.

## Key Operations

### Cell Operations:

 - `CellConstant`: A Cell with an unchanging value
 - `CellMap`: Applies a function to a Cell's value
 - `CellLift`: Combines multiple Cells through a function
 - `CellSwitch`: Dynamically switches between Cell sources


### Stream Operations:

 - `StreamNever`: A Stream that never fires
 - `StreamMap`: Transforms Stream event values
 - `Stream Filter`: Conditionally allows events to propagate
 - `StreamMerge`: Combines events from multiple Streams
 - `StreamSnapshot`: Captures Cell values when a Stream fires
 - `StreamHold`: Converts a Stream to a Cell containing the most recent event value
 - `StreamSwitch`: Dynamically switches between Stream sources


### Compound Operations:

 - `StreamAccumulate`: Accumulates values over a series of events
 - `StreamCollect`: Collects event values
 - `StreamGate`: Controls event flow based on a condition
 - `StreamOnce`: Allows a Stream to fire only once


### I/O Operations:

 - `CellSink`: Input interface for updating Cell values
 - `StreamSink`: Input interface for firing events
 - `StreamListener`: Output interface for causing side effects when events occur



## Cycles and Feedback

Cycles in the dependency graph are only allowed when they involve
Cells, as Cell updates are not visible until the next
transaction. Cycles involving only Streams could cause infinite loops
and should be detected as errors.


# Graph Editor Requirements

Based on this understanding, our graph editor needs to visually represent:

## Node Types:

 - Input nodes (`CellSink`, `StreamSink`)
 - Output nodes (`StreamListener`)
 - Cell-based nodes (`CellConstant`, `CellMap`, `CellLift`, `CellSwitch`)
 - Stream-based nodes (`StreamNever`, `StreamMap`, `StreamFilter`, `StreamMerge`, `StreamSnapshot`, `StreamHold`, `StreamSwitch`)
 - Compound operation nodes (`StreamAccumulate`, `StreamCollect`, `StreamGate`, `StreamOnce`)


## Edge Types:

 - Dependencies between nodes (directed connections)
 - Visual distinction between Cell connections and Stream connections


## Special Features:

 - Cycle detection and visualization
 - Transaction boundaries representation
 - Function references and parameter configuration for each node
 - Visual distinction between primitive operations and compound operations


## Code Generation:

 - Translation of the visual graph to Sodium API calls in the target language
 - Transaction management in the generated code
 - Support for different backend languages


This comprehensive visual editor will enable users to construct
complex reactive programs by manipulating the graph directly, avoiding
the complexity of managing the dependency structure through code
alone.
