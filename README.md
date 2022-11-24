# POL
This is a simple library that allows any user to generate a merkle tree with balances.
The balance of each node in the graph is the sum of the two direct childs. 

The following sample code shows how to generate the root node, 
the call to getNode is where you should put your code to transform your user accounts to a list
of node objects that the Pol library can process.

```java
 List<Node> nodes = getNodes();
 Pol pol = new Pol(nodes.size());
 pol.create(nodes);
 Node root = pol.getRootNode();
```

## Test
In the class Poltest in the test directory there are a few tests that can verify that the data generated is correct.
And also they can help to evaluate (with error) how long it will take to this library to generate the data based on your 
number of accounts.

Inside the demo folder, you will find a java app that you can build and run that will also display the merkle tree.


### Warning:
This is not supposed to be used in production environments because I did not have the time to
do all the testing needed. But it could be used as a reference or an idea of how to generate a merkle tree
to track balances.



```
Tips accepted at: 
 bc1qlyxmepm2dmjk5sj8wgqvmfc0sumxft5c26snk7fkgd0edp845mxqknergl
```