import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MaxFinderTest extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  "MaxFinder" should "find the maximum value in a Vec" in {
    test(new MaxFinder2(4, 8)) { dut =>
      val testVectors = Seq(
        (Seq(3, 7, 2, 5), 7, 1),
        (Seq(10, 4, 8, 1), 10, 0),
        (Seq(0, 0, 0, 0), 0, 3),
        (Seq(1, 2, 3, 4), 4, 3)
      )
      for ((vec, expectedVal, expectedIndex) <- testVectors) {
        for (i <- vec.indices) {
          dut.io.in(i).poke(vec(i).U)
        }
        dut.io.max.expect(expectedVal.U)
        dut.io.index.expect(expectedIndex.U)
      }
    }
  }
}


