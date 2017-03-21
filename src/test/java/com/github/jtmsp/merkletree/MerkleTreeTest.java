/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.jtmsp.merkletree.byteable.ByteableLong;
import com.github.jtmsp.merkletree.byteable.ByteableString;
import com.github.jtmsp.merkletree.crypto.ByteUtil;

public class MerkleTreeTest {

    @Test
    public void testBasic() {

        boolean update;

        IMerkleTree<ByteableLong> tree = new MerkleTree<ByteableLong>();

        update = tree.add(new ByteableLong(1));
        assertFalse("Did not expect an update (should have been create)", update);

        System.out.println(tree.toPrettyString());
        update = tree.add(new ByteableLong(2));
        assertFalse("Did not expect an update (should have been create)", update);

        System.out.println(tree.toPrettyString());
        update = tree.add(new ByteableLong(2));
        assertTrue("Expected an update", update);

        System.out.println(tree.toPrettyString());
        update = tree.add(new ByteableLong(5));
        assertFalse("Did not expect an update (should have been create)", update);

        System.out.println(tree.toPrettyString());

        tree.iterateNodes(node -> {
            if (node.isLeafNode()) {
                System.out.println(ByteUtil.toString00(node.getKey().toByteArray()));
            } else {
                System.out.println("tree node");
            }
            return false;
        });

    }

    @Test
    public void testIteration() {

        IMerkleTree<ByteableString> tree = new MerkleTree<ByteableString>();
        tree.add(new ByteableString("String1"));
        tree.add(new ByteableString("String2"));
        tree.add(new ByteableString("String3"));

        System.out.println("String node: " + tree.toPrettyString());

        tree.iterateNodes(new IterateFunction<ByteableString>() {

            @Override
            public boolean currentNode(MerkleNode<ByteableString> node) {
                System.out.println(node.getKey().string);

                return false;
            }
        });

        System.out.println("\n\n");
    }

}
