/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 - 2018
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.jtendermint.merkletree.iavl;

import static org.junit.Assert.assertNotEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

import com.github.jtendermint.crypto.ByteUtil;
import com.github.jtendermint.crypto.RipeMD160;

public class AVLTreeOtherHashFuncTest {

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
