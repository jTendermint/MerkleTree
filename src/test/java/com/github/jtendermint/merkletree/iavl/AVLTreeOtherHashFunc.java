package com.github.jtendermint.merkletree.iavl;

import static org.junit.Assert.assertNotEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

import com.github.jtendermint.crypto.ByteUtil;
import com.github.jtendermint.crypto.RipeMD160;

public class AVLTreeOtherHashFunc {

    private AVLTree<String> tree1;
    private AVLTree<String> tree2;

    private Hashing<String> func1;
    private Hashing<String> func2;

    @Before
    public void setup() throws NoSuchAlgorithmException {

        func1 = new Hashing<String>() {
            @Override
            public byte[] hashBytes(byte[] byteArray) {
                return RipeMD160.hash(byteArray);
            }
            @Override
            public byte[] hashBytes(String k) {
                return RipeMD160.hash(k.getBytes());
            }
        };

        func2 = new Hashing<String>() {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            @Override
            public byte[] hashBytes(byte[] byteArray) {
                return digest.digest(byteArray);
            }

            @Override
            public byte[] hashBytes(String k) {
                return digest.digest(k.getBytes());
            }
        };

        tree1 = new AVLTree<String>(func1);
        tree2 = new AVLTree<String>(func2);
    }

    @Test
    public void testUnequalHashes() {

        tree1.add("test");
        tree2.add("test");

        String tree1Hash = ByteUtil.toString00(tree1.getRootHash());
        String tree2Hash = ByteUtil.toString00(tree2.getRootHash());
        assertNotEquals(tree1Hash, tree2Hash);

    }

}
