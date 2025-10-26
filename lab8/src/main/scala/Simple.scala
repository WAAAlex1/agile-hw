import chisel3._
import chisel3.util._

class Simple extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(5.W))
    val out = Output(UInt(5.W))
  })

  io.out := io.in + 1.U
}