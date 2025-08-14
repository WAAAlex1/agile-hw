# Arbitration Generator

Write a generator for arbitration between n masters. In a agile setting you can also start to write the tests first.

1. Start with a combinational 2:1 arbiter and then use `treeReduce` to build an arbitration tree.

2. Write a `ChiselTest` for your arbiter to verify its functionality:
 - Create a test that checks the arbitration logic for different input scenarios.
 - Test that each request will succeed eventually.
 - Test if no two requests are granted simultaneously.

3. A combinational arbiter is unfair as there is a fixed priority assigned to each request. Consider a register toggling mechanism to allow for fairer arbitration. But toggle only when there is no request being served (in the 2:1 arbiter).