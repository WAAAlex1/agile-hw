import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

trait ALUImpl {
  def compute(op1: Int, op2: Int, sel: Int, signed: Boolean): (BigInt, Seq[Boolean], Seq[Boolean])
}


class ALUModel extends ALUImpl {
  def compute(op1: Int, op2: Int, sel: Int, signed: Boolean): (BigInt, Seq[Boolean], Seq[Boolean]) = {
    // Convert to appropriate types for computation
    val op1_unsigned = op1 & 0xFFFFFFFFL
    val op2_unsigned = op2 & 0xFFFFFFFFL

    // For signed operations, properly sign-extend 32-bit values
    val op1_signed = if ((op1 & 0x80000000) != 0) op1 | 0xFFFFFFFF00000000L else op1 & 0xFFFFFFFFL
    val op2_signed = if ((op2 & 0x80000000) != 0) op2 | 0xFFFFFFFF00000000L else op2 & 0xFFFFFFFFL

    val result: Long = sel match {
      case 0 => // NOT
        if (signed) ~op1_signed else ~op1_unsigned
      case 1 => // AND
        if (signed) op1_signed & op2_signed else op1_unsigned & op2_unsigned
      case 2 => // OR
        if (signed) op1_signed | op2_signed else op1_unsigned | op2_unsigned
      case 3 => // XOR
        if (signed) op1_signed ^ op2_signed else op1_unsigned ^ op2_unsigned
      case 4 => // ADD
        if (signed) op1_signed + op2_signed else op1_unsigned + op2_unsigned
      case 5 => // SUB
        if (signed) op1_signed - op2_signed else op1_unsigned - op2_unsigned
      case 6 => // DIV
        if (op2 == 0) 0 // Avoid division by zero
        else if (signed) op1_signed / op2_signed else op1_unsigned / op2_unsigned
      case 7 => // MOD
        if (op2 == 0) 0 // Avoid division by zero
        else if (signed) op1_signed % op2_signed else op1_unsigned % op2_unsigned
      case 8 => // MUL
        if (signed) op1_signed * op2_signed else op1_unsigned * op2_unsigned
      case 9 => // SRL (shift right logical)
        val shift_amount = (op2 & 0x3F).toInt
        if (signed) op1_signed >> shift_amount else op1_unsigned >>> shift_amount
      case 10 => // SLL (shift left logical)
        val shift_amount = (op2 & 0x3F).toInt
        if (signed) op1_signed << shift_amount else op1_unsigned << shift_amount
      case _ => 0
    }

    // Mask result to 32 bits
    val result32 = result & 0xFFFFFFFFL

    // Compute comparison flags for operands
    val is_lesser = if (signed) op1_signed < op2_signed else op1_unsigned < op2_unsigned
    val is_equal = if (signed) op1_signed == op2_signed else op1_unsigned == op2_unsigned
    val is_greater = if (signed) op1_signed > op2_signed else op1_unsigned > op2_unsigned

    val comp_flags = Seq(
      is_lesser,           // <
      is_equal,            // ==
      is_greater,          // >
      is_lesser || is_equal, // <=
      is_greater || is_equal  // >=
    )

    // Compute comparison flags for result vs 0
    val result_signed = if ((result32 & 0x80000000L) != 0) result32 | 0xFFFFFFFF00000000L else result32
    val is_lesser_0 = if (signed) result_signed < 0 else false // Unsigned can never be less than 0
    val is_equal_0 = result32 == 0
    val is_greater_0 = if (signed) result_signed > 0 else result32 > 0

    val comp_0_flags = Seq(
      is_lesser_0,
      is_equal_0,
      is_greater_0,
      is_lesser_0 || is_equal_0,
      is_greater_0 || is_equal_0
    )

    (BigInt(result32), comp_flags, comp_0_flags)
  }
}

// Chisel implementation wrapper
// This wrapper is complete and drives the ALU hardware
class ALUChisel(dut: ALU) extends ALUImpl {
  def compute(op1: Int, op2: Int, sel: Int, signed: Boolean): (BigInt, Seq[Boolean], Seq[Boolean]) = {
    // Set the signed mode
    dut.io.in_signed.poke(signed.B)

    // Set the operation selector
    dut.io.in_sel.poke(sel.U)

    // Set unsigned operands
    dut.io.in_op1.poke((op1 & 0xFFFFFFFFL).U)
    dut.io.in_op2.poke((op2 & 0xFFFFFFFFL).U)

    // Set signed operands
    dut.io.in_op1_signed.poke(op1.S)
    dut.io.in_op2_signed.poke(op2.S)

    // Step the clock to propagate values
    dut.clock.step(1)

    // Read the result
    val result = if (signed) {
      val signedResult = dut.io.out_result_signed.peek().litValue
      // Convert to unsigned representation for consistency
      signedResult & BigInt("FFFFFFFF", 16)
    } else {
      dut.io.out_result.peek().litValue
    }

    // Read comparison flags
    val comp_flags = (0 until 5).map { i =>
      dut.io.out_comp(i).peek().litToBoolean
    }

    // Read result vs 0 comparison flags
    val comp_0_flags = (0 until 5).map { i =>
      dut.io.out_result_comp_0(i).peek().litToBoolean
    }

    (result, comp_flags, comp_0_flags)
  }
}

// Simple tester that compares multiple implementations
class ALUSimpleTester(implementations: Seq[ALUImpl]) {
  def testOperation(description: String)(op1: Int, op2: Int, sel: Int, signed: Boolean): Unit = {
    println(s"\nTesting: $description")
    println(s"Inputs: op1=$op1, op2=$op2, sel=$sel, signed=$signed")

    val results = implementations.map { impl =>
      impl.compute(op1, op2, sel, signed)
    }

    if (results.size > 1) {
      // Compare all implementations
      val reference = results.head
      results.zipWithIndex.tail.foreach { case (result, idx) =>
        assert(result._1 == reference._1,
          s"Implementation ${idx + 1} result mismatch: ${result._1} != ${reference._1}")
        assert(result._2 == reference._2,
          s"Implementation ${idx + 1} comp flags mismatch: ${result._2} != ${reference._2}")
        assert(result._3 == reference._3,
          s"Implementation ${idx + 1} comp_0 flags mismatch: ${result._3} != ${reference._3}")
      }
      println(s"✓ All implementations agree: result = ${reference._1}")
      println(s"  Comparison flags (< = > <= >=): ${reference._2}")
      println(s"  Result vs 0 flags (< = > <= >=): ${reference._3}")
    } else if (results.size == 1) {
      val result = results.head
      println(s"✓ Result = ${result._1}")
      println(s"  Comparison flags (< = > <= >=): ${result._2}")
      println(s"  Result vs 0 flags (< = > <= >=): ${result._3}")
    }
  }
}

// Main test class
class ALUTest extends AnyFlatSpec with ChiselScalatestTester {

  behavior of "ALU"

  it should "perform unsigned addition correctly" in {
    test(new ALU) { dut =>
      val model = new ALUModel()
      val chiselImpl = new ALUChisel(dut)
      val tester = new ALUSimpleTester(Seq(model, chiselImpl))

      tester.testOperation("5 + 3 (unsigned)")(5, 3, 4, false)
      tester.testOperation("100 + 50 (unsigned)")(100, 50, 4, false)
      tester.testOperation("0 + 0 (unsigned)")(0, 0, 4, false)
    }
  }

  it should "perform unsigned subtraction correctly" in {
    test(new ALU) { dut =>
      val model = new ALUModel()
      val chiselImpl = new ALUChisel(dut)
      val tester = new ALUSimpleTester(Seq(model, chiselImpl))

      tester.testOperation("10 - 3 (unsigned)")(10, 3, 5, false)
      tester.testOperation("100 - 50 (unsigned)")(100, 50, 5, false)
    }
  }

  it should "perform bitwise operations correctly" in {
    test(new ALU) { dut =>
      val model = new ALUModel()
      val chiselImpl = new ALUChisel(dut)
      val tester = new ALUSimpleTester(Seq(model, chiselImpl))

      tester.testOperation("15 & 7 (AND)")(15, 7, 1, false)
      tester.testOperation("8 | 4 (OR)")(8, 4, 2, false)
      tester.testOperation("15 ^ 7 (XOR)")(15, 7, 3, false)
      tester.testOperation("NOT 5")(5, 0, 0, false)
    }
  }

  it should "perform multiplication and division correctly" in {
    test(new ALU) { dut =>
      val model = new ALUModel()
      val chiselImpl = new ALUChisel(dut)
      val tester = new ALUSimpleTester(Seq(model, chiselImpl))

      tester.testOperation("6 * 7 (MUL)")(6, 7, 8, false)
      tester.testOperation("20 / 4 (DIV)")(20, 4, 6, false)
      tester.testOperation("23 % 5 (MOD)")(23, 5, 7, false)
    }
  }

  it should "perform shift operations correctly" in {
    test(new ALU) { dut =>
      val model = new ALUModel()
      val chiselImpl = new ALUChisel(dut)
      val tester = new ALUSimpleTester(Seq(model, chiselImpl))

      tester.testOperation("8 << 2 (SLL)")(8, 2, 10, false)
      tester.testOperation("32 >> 2 (SRL)")(32, 2, 9, false)
    }
  }

  it should "handle signed operations correctly" in {
    test(new ALU) { dut =>
      val model = new ALUModel()
      val chiselImpl = new ALUChisel(dut)
      val tester = new ALUSimpleTester(Seq(model, chiselImpl)) // TODO: Add model when implemented

      tester.testOperation("-5 + 3 (signed)")(-5, 3, 4, true)
      tester.testOperation("10 - 20 (signed)")(10, 20, 5, true)
      tester.testOperation("-4 * 3 (signed)")(-4, 3, 8, true)
    }
  }

  it should "set comparison flags correctly" in {
    test(new ALU) { dut =>
      val model = new ALUModel()
      val chiselImpl = new ALUChisel(dut)
      val tester = new ALUSimpleTester(Seq(model, chiselImpl)) // TODO: Add model when implemented

      // When op1 < op2, flags should be [true, false, false, true, false]
      tester.testOperation("3 < 5 comparison")(3, 5, 4, false)

      // When op1 = op2, flags should be [false, true, false, true, true]
      tester.testOperation("7 = 7 comparison")(7, 7, 4, false)

      // When op1 > op2, flags should be [false, false, true, false, true]
      tester.testOperation("10 > 5 comparison")(10, 5, 4, false)
    }
  }
}
