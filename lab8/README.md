# Lab 8: Formal Verification

This lab introduces formal verification techniques for hardware designs using Chisel and related tools. You will learn how to specify properties of your hardware and verify them using formal methods.

To use Chisel formal you need to install the open [Z3 theorem prover](https://github.com/Z3Prover/z3). Use your package manager to install, e.g. on Ubuntu:

```bash
sudo apt install z3
```
On MacOS you can use Homebrew:

```bash
brew install z3
```

## Hello World

We start with a minimal example in `Simple.scala`, which contains two assertions.
One is correct the other one is incorrect. The test given in `SimpleTest` will pass, although one assertion is wrong.

```
sbt "testOnly SimpleTest"
```

However, the formal verification (`SimpleVerify`) will catch the error.

```
sbt "testOnly SimpleVerify"
```

Play with this example: run the tests and explore the VCD file.
Does the formal verification show a counter example in the VCD file?

Fix the assertion on check that the formal test passes.


## A simple counter

We start with a simple example of a 2-bit counter. Create a new Chisel module `Counter2Bit` in a file named `Counter2Bit.scala`:

```scala
import chisel3._
import chisel3.util._
class Counter2Bit extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(2.W))
  })
  val count = RegInit(0.U(2.W))
  count := count + 1.U
  io.out := count
}
```
Next, we will write a formal specification for this counter. Create a new file named `Counter2BitFormal.scala`:

```scala
import chisel3._
import chisel3.util._
import chisel3.experimental.ChiselEnum
import chisel3.formal._
class Counter2BitFormal extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(2.W))
  })
  val count = RegInit(0.U(2.W))
  count := count + 1.U
  io.out := count

  // Formal properties
  assert(count <= 3.U, "Counter should not exceed 3")
  cover(count === 0.U, "Counter reaches 0")
}
```
