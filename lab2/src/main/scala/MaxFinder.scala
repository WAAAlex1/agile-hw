
import chisel3._
import chisel3.util._

/**
  * Find the maximum value in a Vec using treeReduce
  * @param n number of elements
  */
class MaxFinder1(val n: Int, val width: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(n, UInt(width.W)))
    val max = Output(UInt(width.W))
  })
  io.max := io.in.reduceTree((x,y) => Mux(x<y, y, x))
}

class MaxFinder2(val n: Int, val width: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(n, UInt(width.W)))
    val max = Output(UInt(width.W))
    val index = Output(UInt(log2Ceil(n).W))
  })

  val res = io.in.zipWithIndex
            .map((x) => (x._1, x._2.U))
            .reduce((x,y) => (
              Mux(x._1 > y._1, x._1, y._1),
              Mux(x._1 > y._1, x._2, y._2))
            )

  io.max := res._1
  io.index := res._2

}

class MaxFinder3(val n: Int, val width: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(n, UInt(width.W)))
    val max = Output(UInt(width.W))
    val index = Output(UInt(log2Ceil(n).W))
  })


  val resIntermediate = io.in.zipWithIndex
    .map((x) => MixedVecInit(x._1 , x._2.U(width.W)))

  val resFinal = VecInit(resIntermediate)
    .reduceTree((x,y) => (
      Mux(x(0) > y(0), x, y))
    )

  io.max := resFinal(0)
  io.index := resFinal(1)

}


object Max extends App {
  emitVerilog (new MaxFinder2(64, 8))
}
