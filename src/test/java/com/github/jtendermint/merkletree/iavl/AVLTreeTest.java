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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import com.github.jtendermint.crypto.ByteUtil;
import com.github.jtendermint.crypto.RipeMD160;
import com.github.jtendermint.merkletree.iavl.IterateFunct.Loop;
import com.github.jtendermint.merkletree.HashWithCount;

public class AVLTreeTest {

    private AVLTree<String> tree;
    private Hashing<String> hashFunc;

    @Before
    public void setup() {

        hashFunc = new Hashing<String>() {
            @Override
            public byte[] hashBytes(byte[] byteArray) {
                return RipeMD160.hash(byteArray);
            }
            @Override
            public byte[] hashBytes(String k) {
                return hashBytes(k.getBytes());
            }
        };
        tree = new AVLTree<String>(hashFunc);
    }

    @Test
    public void testSimpleTreeOps() throws Exception {

        assertEquals(0, tree.getHeight());
        assertNull(tree.getRoot());
        assertNull(tree.getRootHash());

        tree.add("test");

        assertEquals(0, tree.getHeight());
        assertNotNull(tree.getRoot());
        assertNotNull(tree.getRootHash());

        tree.add("test");
        tree.add("test");
        tree.add("test");
        tree.add("test");
        tree.add("test");

        assertEquals(0, tree.getHeight());

        tree.add("more");

        assertEquals(1, tree.getHeight());
    }

    @Test
    public void testPrettyString() throws Exception {
        // empty tree
        assertEquals("()", tree.toPrettyString());

        String expected = "((1 2) (3 4))";
        // order of adds is unimportant
        tree.add("2");
        tree.add("1");
        tree.add("4");
        tree.add("3");
        assertEquals(expected, tree.toPrettyString());

    }

    @Test
    public void testSize() throws Exception {
        assertEquals(0, tree.size());
        tree.add("test");
        assertEquals(1, tree.size());
    }

    @Test
    public void testContains() throws Exception {
        assertFalse(tree.contains((String) null));
        assertFalse(tree.contains("test"));
        tree.add("test");

        assertTrue(tree.contains("test"));
    }

    @Test
    public void testRootHash() throws Exception {

        assertNull(tree.getRootHash());

        tree.add("test");
        String expectedHash = ByteUtil.toString00(hashFunc.hashBytes(hashFunc.hashBytes("test")));
        assertEquals(expectedHash, ByteUtil.toString00(tree.getRootHash()));
    }

    @Test
    public void testGet() throws Exception {

        assertNull(tree.get(1));
        assertNull(tree.get("test"));

        tree.add("test");

        assertEquals("test", tree.get(0).getValue());
        assertEquals("test", tree.get("test"));

        assertFalse(tree.add("more1"));
        assertFalse(tree.add("more2"));
        assertFalse(tree.add("more3"));
        assertFalse(tree.add("more4"));
        assertFalse(tree.add("moremoremore"));

        assertEquals(6, tree.size());
    }

    @Test
    public void testHashWithCount() throws Exception {

        HashWithCount h1 = tree.getHashWithCount();

        assertEquals(0, h1.count);
        assertEquals(null, h1.hash);

        tree.add("test");
        h1 = tree.getHashWithCount();
        String expectedHash = ByteUtil.toString00(hashFunc.hashBytes(hashFunc.hashBytes("test")));

        assertEquals(1, h1.count);
        assertEquals(expectedHash, ByteUtil.toString00(h1.hash));

        // count expresses the amount of new hashes that were built
        assertEquals(0, tree.getHashWithCount().count);
    }

    @Test
    public void testIteration() throws Exception {
        AtomicInteger count = new AtomicInteger(0);

        tree.iterateNodes(node -> {
            count.incrementAndGet();
            return Loop.CONTINUE;
        });

        assertEquals(0, count.get());

        tree.add("test1");
        tree.iterateNodes(node -> {
            count.incrementAndGet();
            return Loop.CONTINUE;
        });
        assertEquals(1, count.get());
        count.set(0);

        tree.add("test2");
        tree.add("test3");

        tree.iterateNodes(node -> {
            count.incrementAndGet();
            return Loop.CONTINUE;
        });

        assertEquals(5, count.get());
    }
}
