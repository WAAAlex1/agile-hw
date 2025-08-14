
import chisel3._
import chisel3.util._

import org.scalatest.funsuite.AnyFunSuite   

/**
  * n-way combinational priority arbiter
  * @param n number of requesters
  */
class Arbiter(val n: Int) extends Module {
  val io = IO(new Bundle {
    val req = Input(Vec(n, Bool()))
    val grant = Output(Vec(n, Bool()))
  })

  ???
}
