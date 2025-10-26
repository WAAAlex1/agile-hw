import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec


// Main test class
class SimpleTest extends AnyFlatSpec with ChiselScalatestTester {

  behavior of "Simple"

  it should "perform unsigned addition correctly" in {
    test(new Simple) { dut =>

    }
  }
}