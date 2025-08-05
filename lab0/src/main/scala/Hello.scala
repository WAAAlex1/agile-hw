/*
 * A minimal compopnent to test the Chisel setup.
 *
 * Copyright: 2025, Technical University of Denmark, DTU Compute
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * 
 */

import chisel3._

class Hello(n: Integer = 8, m: Integer = 16) extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(8.W))
    val dout = Output(UInt(16.W))
  })

  val accReg = RegInit(0.U(16.W))
  accReg := accReg + io.din
  io.dout := accReg
}

/**
 * An object extending App to generate the Verilog code.
 */
object Hello extends App {
  println("Hello World, I will now generate the Verilog file!")
  emitVerilog(new Hello())
}
