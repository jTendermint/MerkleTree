package com.github.jtendermint.merkletree;

import static org.junit.Assert.assertNotEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.github.jtendermint.merkletree.MerkleNode;
import com.github.jtendermint.merkletree.MerkleTree;
import org.junit.Test;

import com.github.jtendermint.crypto.ByteUtil;
import com.github.jtendermint.crypto.HashFunction;
import com.github.jtendermint.merkletree.byteable.types.ByteableLong;
import com.github.jtendermint.merkletree.byteable.types.IByteable;

public class MerkleTreeOtherHashTest {

    @Test
    public void testSha1() {

        MerkleTree<ByteableLong> treeRipe = new MerkleTree<>();
        MerkleTree<ByteableLong> treeSHA = new SHA1Tree<>();
        MerkleTree<ByteableLong> treeSHA512 = new SHA512Tree<>();

        treeRipe.add(new ByteableLong(1l));
        treeSHA.add(new ByteableLong(1l));
        treeSHA512.add(new ByteableLong(1l));

        System.out.println("SHA-1: " + ByteUtil.toString00(treeSHA.getRootHash()));
        System.out.println("SHA512:" + ByteUtil.toString00(treeSHA512.getRootHash()));
        System.out.println("RIPE:  " + ByteUtil.toString00(treeRipe.getRootHash()));

        assertNotEquals(ByteUtil.toString00(treeSHA.getRootHash()), ByteUtil.toString00(treeRipe.getRootHash()));
        assertNotEquals(ByteUtil.toString00(treeSHA.getRootHash()), ByteUtil.toString00(treeSHA512.getRootHash()));
        assertNotEquals(ByteUtil.toString00(treeSHA512.getRootHash()), ByteUtil.toString00(treeRipe.getRootHash()));
    }

    public class SHA1Tree<K extends IByteable> extends MerkleTree<K> {

        protected MerkleNode<K> createNode(K entry) {
            return new SHA1Node<K>(entry);
        };
    }

    public class SHA1Node<V extends IByteable> extends MerkleNode<V> {
        public SHA1Node(V key) {
            super(key);
            hashFunction = new SHA1Func();
        }

        @Override
        protected MerkleNode<V> createNode(V entry) {
            return new SHA1Node<V>(entry);
        }
    }

    public class SHA1Func implements HashFunction {

        MessageDigest digest;

        public SHA1Func() {
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        @Override
        public byte[] hashBytes(byte[] arg0) {
            return digest.digest(arg0);
        }

    }
    
    public class SHA512Tree<K extends IByteable> extends MerkleTree<K> {

        protected MerkleNode<K> createNode(K entry) {
            return new SHA512Node<K>(entry);
        };
    }

    public class SHA512Node<V extends IByteable> extends MerkleNode<V> {
        public SHA512Node(V key) {
            super(key);
            hashFunction = new SHA512Func();
        }

        @Override
        protected MerkleNode<V> createNode(V entry) {
            return new SHA512Node<V>(entry);
        }
    }

    public class SHA512Func implements HashFunction {

        MessageDigest digest;

        public SHA512Func() {
            try {
                digest = MessageDigest.getInstance("SHA-512");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        @Override
        public byte[] hashBytes(byte[] arg0) {
            return digest.digest(arg0);
        }

    }

}
