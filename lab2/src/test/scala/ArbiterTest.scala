import org.scalatest.funsuite.AnyFunSuite

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers



class ArbiterTest extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  "Arbiter" should "do something" in {
    test(new Arbiter(4)) { dut =>

    }
  }
}
