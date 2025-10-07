object Exercises {
  // 1. Double Elements
  def doubleElements(lst: List[Int]): List[Int] = {

    def double(x: Int) = {
      x*2
    }

    lst.map(double)
  }

  // 2. Filter Odd Numbers
  def filterOddNumbers(lst: List[Int]): List[Int] = {
    lst.filter(x => x % 2 == 0)
  }

  // 3. Higher-Order Function
  def applyFunction[A, B](f: A => B, lst: List[A]): List[B] = {
    lst.map(f)
  }
}
