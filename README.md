# Agile Hardware Design (02201)

Material for the [Agile Hardware Design Course](https://kurser.dtu.dk/course/02201) at DTU.

Agile hardware design with Chisel (Constructing Hardware in a Scala Embedded Language) is a specialized course that focuses on using the Chisel hardware description language (HDL) as part of the agile hardware design process. Chisel is a modern hardware description language that enables designers to create digital circuits with concise, high-level code that is easier to write and maintain than traditional HDLs.

This course is designed for students who have a background in digital design and are interested in learning how to apply agile methodologies to hardware design using Chisel. The course will cover the principles of agile hardware design, the Chisel HDL, and how to use Chisel to create efficient, flexible, and maintainable hardware designs.

The course covers the following topics:

1. Introduction to Agile Hardware Design with Chisel: an overview of agile design in general and hardware design and the Chisel HDL, including its syntax, features, and benefits.

2. Agile Hardware Design Process with Chisel: agile hardware design process using Chisel, including requirements gathering, design, prototyping, testing, and deployment.

3. Chisel Design Patterns: design patterns and idioms used in Chisel, such as generators, parameterization, and functional abstraction.

4. Verification of Designs: Verification with Chisel Test, test-driven development, continuous integration, formal verification.

5. Agile Hardware Design Case Studies: a project of a real-world hardware design project that uses Chisel and agile methodologies, providing students with practical examples of how to apply these concepts in their work.

Overall, the course will provide the students with a comprehensive understanding of agile hardware design with Chisel, enabling them to design and implement complex digital circuits via generators.

## Getting Started

 * Install the tools as described in [Setup.md](Setup.md).
 * Test the installation with the [lab0](lab0) example design and test.

## Course Info

 * Instructor: Martin Schoeberl
 * Email: masca@dtu.dk
 * When: Tuesdays 13:00-17:00
 * Place: 324.020

 ## Lecture Plan

 ### Week 1: Agile and Scala

  * [Slides](01_scala.pdf)
  * Lab: [lab0](lab0) and [lab1](lab1)

### Week 2: Introduction to Chisel

  * [Slides](02_chisel.pdf)
  * Labs from the Digital Electroncs 2 course: [Chisel Lab 2](https://github.com/schoeberl/chisel-lab/tree/master/lab2) and [Chisel Lab 3](https://github.com/schoeberl/chisel-lab/tree/master/lab3)

### Week 3: Simple Generators

 * [Slides](03_simp_gen.pdf)
 * Labs: [lab4](lab4) and [lab5](lab5)

### Week 4: Digital Design with Clash

 * Slides
 * Lab: [Clash Lab on DTU Learn](https://learn.inside.dtu.dk/d2l/le/lessons/270912/topics/1074403)

 ### Week 5: Generators

 * [Slides](05_generators.pdf)
 * Project presentations

 ### Week 6: Testing and CI

 * [Slides](05_testing_and_ci.pdf)
 * Lab: [lab6](lab6)

## Chattutor

 * We have a Chisel version of the [Chattutor](https://chattutor.dk/c/111/s/141/) project.

## Further Material

 * [The Chisel book](https://www.imm.dtu.dk/~masca/chisel-book.html)
 * [Scott's Agile Hardware Design Course](https://classes.soe.ucsc.edu/cse228a/Winter24/)
 * [Agile SW on Wikipedia](https://en.wikipedia.org/wiki/Agile_software_development)

 ### Papers

 * [Scala defined hardware generators for Chisel](https://www.sciencedirect.com/science/article/pii/S014193312500050X)
 * [Creating an Agile Hardware Design Flow](https://cs.stanford.edu/~niemetz/publications/2020/DAC2020.pdf)
