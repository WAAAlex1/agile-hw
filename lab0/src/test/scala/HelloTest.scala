import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class HelloTest extends AnyFlatSpec with ChiselScalatestTester {

  "Hello" should "pass" in {
    test(new Hello()) { dut =>
      println("Testing the circuit")
      dut.io.din.poke(1.U)
      dut.clock.step(1)
      dut.io.dout.expect(1.U)
    }
  }
}

