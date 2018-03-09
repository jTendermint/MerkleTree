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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.jtmsp.merkletree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.jtendermint.crypto.ByteUtil;
import com.github.jtmsp.merkletree.byteable.ByteableLong;
import com.github.jtmsp.merkletree.byteable.ByteableString;

public class MerkleTreeTest {

    private IMerkleTree<ByteableString> stringTree;
    private IMerkleTree<ByteableLong> longTree;

    @Before
    public void setup() {
        stringTree = new MerkleTree<ByteableString>();
        longTree = new MerkleTree<ByteableLong>();
    }

    @Test
    public void testTreeUpdate() {
        boolean update;

        update = longTree.add(new ByteableLong(1));
        assertFalse("Did not expect an update (should have been create)", update);

        update = longTree.add(new ByteableLong(2));
        assertFalse("Did not expect an update (should have been create)", update);

        update = longTree.add(new ByteableLong(2));
        assertTrue("Expected an update", update);

        update = longTree.add(new ByteableLong(5));
        assertFalse("Did not expect an update (should have been create)", update);

        assertEquals("(1 (2 5))", longTree.toPrettyString());
    }

    @Test
    public void testIteration() {
        // instantly returns false, because no root element
        assertFalse(stringTree.iterateNodes(node -> true));

        stringTree.add(new ByteableString("String1"));
        stringTree.add(new ByteableString("String2"));
        stringTree.add(new ByteableString("String3"));

        ArrayList<String> hitItems = new ArrayList<>();

        stringTree.iterateNodes(new IterateFunction<ByteableString>() {
            @Override
            public boolean currentNode(MerkleNode<ByteableString> node) {
                if (node.isLeafNode())
                    hitItems.add(node.getKey().string);
                return false;
            }
        });

        assertEquals(3, hitItems.size());
        assertTrue(hitItems.contains("String1"));
        assertTrue(hitItems.contains("String2"));
        assertTrue(hitItems.contains("String3"));
    }

    @Test
    public void testSize() {
        assertEquals(0, stringTree.size());
        stringTree.add(new ByteableString("test"));
        assertEquals(1, stringTree.size());
    }

    @Test
    public void testHeight() {
        assertEquals(0, stringTree.getHeight());

        stringTree.add(new ByteableString("1"));
        assertEquals(0, stringTree.getHeight());

        stringTree.add(new ByteableString("2"));
        assertEquals(1, stringTree.getHeight());

        stringTree.add(new ByteableString("3"));
        stringTree.add(new ByteableString("4"));
        assertEquals(2, stringTree.getHeight());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAndRemove() {
        assertNull(stringTree.get(new ByteableString("test")));
        assertNull(stringTree.get(0));

        stringTree.add(new ByteableString("test"));
        assertNotNull(stringTree.get(new ByteableString("test")));
        assertNotNull(stringTree.get(0));

        stringTree.remove(new ByteableString("test")); // throws UnsupportedOperationException
        assertNull(stringTree.get(new ByteableString("test")));

    }

    @Test
    public void testGetRootHash() {
        assertFalse(stringTree.contains(new ByteableString("test")));
        assertNull(stringTree.getRootHash());

        stringTree.add(new ByteableString("test"));
        assertEquals("C9893DAFCECB4E9FF86BF16501397E4A2DC8B9E5", ByteUtil.toString00(stringTree.getRootHash()));

        assertEquals(new ByteableString("test"), stringTree.getRoot().getKey());

        assertTrue(stringTree.contains(new ByteableString("test")));
        assertFalse(stringTree.contains(new ByteableString("nottest")));
    }

    @Test
    public void testToPrettyString() {
        assertEquals("()", stringTree.toPrettyString());
        stringTree.add(new ByteableString("1"));
        stringTree.add(new ByteableString("2"));
        assertEquals("(49 50)", stringTree.toPrettyString());
    }

    @Test
    public void testHashWithCount() {

        HashWithCount hwc = longTree.getHashWithCount();
        assertNull(hwc.hash);
        assertEquals(0, hwc.count);

        longTree.add(new ByteableLong(1l));

        hwc = longTree.getHashWithCount();
        assertEquals(1, hwc.count);
        assertEquals("8F7EA991CBC6B6FDCE27D02DC9A3297AF102F94B", ByteUtil.toString00(hwc.hash));

        longTree.add(new ByteableLong(2l));

        hwc = longTree.getHashWithCount();
        assertEquals(2, hwc.count);
        assertEquals("E9CD625EBC02C8602B8E0DC8861B9E4B480757BA", ByteUtil.toString00(hwc.hash));

    }

}
