# MerkleTree
A Java implemetation of an AVL tree. 
Can and should be used with jTMSP for storing transactions.

[![CircleCI](https://circleci.com/gh/jTendermint/MerkleTree.svg?style=shield)](https://circleci.com/gh/jTendermint/MerkleTree)


## How to use
### Variant A
```java
IMerkleTree<ByteableLong> tree = new MerkleTree<ByteableLong>();
tree.add(new ByteableLong(1));
tree.add(new ByteableLong(2));
tree.add(new ByteableLong(5));

System.out.println(tree.toPrettyString());  //prints: (1 (2 5))
System.out.println(ByteUtil.toString00(tree.getRootHash())); //prints: 9BDF43BD12B0BF8333C95EF484D3C12B6E19E26B
```
### Variant B


```java
AVLTree<String> tree = new AVLTree<String>(new Hashing<String>() {
            public byte[] hashBytes(byte[] byteArray) {
                return RipeMD160.hash(byteArray);
            }
            public byte[] hashBytes(String k) {
                return hashBytes(k.getBytes());
            }
        });
tree.add("cat");
tree.add("dog");
tree.add("bird");


System.out.println(tree.toPrettyString());  //prints: ((bird cat) dog)
System.out.println(ByteUtil.toString00(tree.getRootHash())); // prints: 6CFCCBFCF3F5499F09CD51BA3A5D77E6BADC187F
```
