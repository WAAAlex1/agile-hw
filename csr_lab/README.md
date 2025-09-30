# CSR Adapter Generator

A CSR (Control and Status Register) is a register used to configure, control, and monitor hardware components in a digital system, typically within processors, peripherals, or custom IP blocks. They form the software-visible interface to hardware functionality. In the case of RISC-V, architectural CSR's are accessed through dedicated instructions, for instance to configure interrupts. A more typical example, however, are memory-mapped CSR's which are accessed through standard load/store instructions, which could provide access to peripherals or platform configuration mechanisms. 

When designing a system, the definition of CSR's and their mapping into an address space has to be defined early on such that both hardware and software components can be developed in parallel. However, requirements are often subject to change and the CSR specification may need to be updated. This can lead to a tedious and error-prone process of manually updating several *views* of the same information such as the RTL implementation, verification infrastructure, software components and documentation.

The solution is of course to have one formal description of the CSR's and generate all other views such as RTL and documentation from this single source of truth. Since IP blocks are often reused across different projects, the CSR specification should be composable such that CSR's of different IP blocks can be combined into a single address space.

While standards such as [SystemRDL](https://en.wikipedia.org/wiki/SystemRDL) and [IP-XACT](https://en.wikipedia.org/wiki/IP-XACT) as well as tools like [PeakRDL](https://peakrdl.readthedocs.io/en/latest/index.html) exist to solve exactly this problem, the reality is that many companies and projects use ad-hoc solutions such as spreadsheets or even text documents to define CSR's. In this lab, you will create a simple tool to generate CSR adapters from a spreadsheet description of CSR's. A reference implementation is provided in Python. You will implement the equivalent generator in Chisel, showcasing the power of the combination of an embedded hardware DSL and Scala's unrestricted programming capabilities, opposed to a traditional HDL or ad-hoc generator scripts.

## CSR Specification

The CSR specification is provided in Excel spreadsheets. For each IP block, a separate sheet with the blocks name provides the CSR definition for that block. Registers have a name and an offset from the base address of the block. Each register can have multiple fields. If no subfields exist, the field name is left blank. A whole register or a field can have one of the following types:
* `rw`: software can read and write, hardware can read
* `ro`: software can read, hardware provides the value
* `wotrg`: software can only write, sending an event to the hardware in addition to the written value
* `rotrg`: software can only read, sending an event to the hardware, which in turn provides the value
* `const`: software can only read, the value is hardwired inside the CSR adapter

An example CSR definition is shown below:

| Register  | Offset | Field   | Type   | Range   | Init        |
|-----------|--------|---------|--------|---------|-------------|
| myreg0    | 0x0    | foo     | rw     | 11:0    | 0x123       |
| myreg0    | 0x0    | bar     | ro     | 31:12   | ?           |
| myreg1    | 0x4    |         | rw     | 7:0     | 0x7         |
| myWrTrg   | 0x8    | baz     | wotrg  | 7:0     | ?           |
| myRdTrg   | 0xC    | qux     | rotrg  | 15:0    | ?           |
| myConst   | 0x10   |         | const  | 31:0    | 0xdeadbeef  |

The spreadsheet called `Map` defines the memory map of the system. Each row defines an instance of an IP block with a `Block` type, a unique `Name` and an address range. Additional fields such as the interace type or whether the block is cacheable or executable are not used in this lab, but could be used in a more advanced implementation to generate different types of adapters or additional documentation.

An example memory map is shown below:

| Block         | Name    | Interface | Base Address | End Address | Cacheable | Executable | Description      |
|---------------|---------|-----------|--------------|-------------|-----------|------------|------------------|
| MyBlock       | block0  | APB       | 0x1000       | 0x10FF      | no        | no         | my block         |
| MyOtherBlock  | block1  | APB       | 0xFF0000     | 0xFF0FFF    | no        | no         | my other block   |


# Adapter Interface



### Using the python generator

```bash
python generate_csr.py soc.xlsx
```
