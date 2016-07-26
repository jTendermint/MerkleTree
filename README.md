# MerkleTree
A Java implemetation of an AVL tree. 
Can and should be used with jTMSP for storing transactions.


## How to use
```java
IMerkleTree<ByteableLong> tree = new MerkleTree<ByteableLong>();
tree.add(new ByteableLong(1));
tree.add(new ByteableLong(2));
tree.add(new ByteableLong(5));

System.out.println(tree.toPrettyString());  //prints: (1 (2 5))
System.out.println(ByteUtil.toString00(tree.getRootHash())); //prints: 9BDF43BD12B0BF8333C95EF484D3C12B6E19E26B
```
