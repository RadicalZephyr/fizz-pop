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
 - `CellLift`: Combines two or more Cells through applying a function to their values
 - `CellSwitch`: Dynamically switches between Cell sources, transforms a `Cell<Cell<T>>` into a `Cell<T>`


### Stream Operations:

 - `StreamNever`: A Stream that never fires
 - `StreamMap`: Transforms Stream event values with a function
 - `Stream Filter`: Conditionally allows events to propagate based on a boolean function on the event data
 - `StreamMerge`: Combines events from multiple Streams
 - `StreamSnapshot`: Samples one or more Cell values when a Stream fires and combines them all with a function
 - `StreamHold`: Converts a Stream to a Cell containing the most recent event value
 - `StreamSwitch`: Dynamically switches between Stream sources, transforms a `Cell<Stream<T>>` into a `Stream<T>`


### Compound Operations:

 - `StreamAccumulate`: As each event is received, the accumulating function f is called with the current state and the new event value
 - `StreamCollect`: Transform an event with a generalized state loop (a Mealy machine). The function is passed the input and the old state and returns the new state and output value
 - `StreamGate`: Controls event flow based on a condition `Cell<bool>`
 - `StreamOnce`: A Stream that will fire only one time


### I/O Operations:

 - `CellSink`: Input interface for updating Cell values
 - `CellSample`: Output interface for reading the current value of a Cell
 - `CellUpdates`: Output interface for running side effects when a Cell value is updated
 - `StreamSink`: Input interface for firing events with optional data
 - `StreamListener`: Output interface for running side effects when events occur



## Cycles and Feedback

Cycles in the dependency graph are only allowed when they involve
Cells, as Cell updates are not visible until the next
transaction. Cycles involving only Streams could cause infinite loops
and should be detected as errors.


# Graph Editor Requirements

Based on this understanding, our graph editor needs to visually represent:

## Node Types:

 - Input nodes (`CellSink`, `StreamSink`)
 - Output nodes (`StreamListener`, `CellListener`)
 - Cell-based nodes (`CellConstant`, `CellMap`, `CellLift`, `CellSwitch`)
 - Stream-based nodes (`StreamNever`, `StreamMap`, `StreamFilter`, `StreamMerge`, `StreamSnapshot`, `StreamHold`, `StreamSwitch`)
 - Compound operation nodes (`StreamAccumulate`, `StreamCollect`, `StreamGate`, `StreamOnce`)


## Edge Types:

 - Dependencies between nodes (directed connections)
 - Visual distinction between Cell connections and Stream connections


## Special Features:

 - Name and description fields for all nodes
 - Type information for input and output Cells and Streams
 - Required input and output Cells and Streams
 - Comment nodes for descriptive text
 - Cycle detection and visualization
 - Function references, parameter configuration and type information for each node
 - Visual distinction between primitive operations and compound operations
 - User-created abstractions "Compound Nodes" (which will use the same mechanism as built-in compound operations)
 - System boundary containers that visually group nodes and show dependencies crossing their boundaries

### User Created Abstractions / Reusable Systems

A compound node consists of a subgraph which can be edited
separately. It will have a set of input and output Cells and Streams
(can be multiple Cells and Streams as both inputs and outputs). This
abstraction can then be reused multiple times else where in the
program as a single node and when the abstraction subgraph is updated
all the places it is used will be updated as well.

## Code Generation:

 - Translation of the visual graph to Sodium API calls in the target language
 - Transaction management in the generated code
 - Support for different backend languages


This comprehensive visual editor will enable users to construct
complex reactive programs by manipulating the graph directly, avoiding
the complexity of managing the dependency structure through code
alone.
