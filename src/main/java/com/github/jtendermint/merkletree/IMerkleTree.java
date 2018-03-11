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
package com.github.jtendermint.merkletree;

import com.github.jtendermint.merkletree.byteable.types.IByteable;

public interface IMerkleTree<K extends IByteable> {

    /**
     * Returns the tree size (elements in tree)
     */
    int size();

    /**
     * Returns the tree-height
     */
    int getHeight();

    /**
     * Check if an entry is already present
     * @param entry
     */
    boolean contains(K entry);

    /**
     * Return an entry from the tree, uses {@link #equals(Object)} for equality-checks
     * @param entry
     * @return
     */
    K get(K entry);

    /**
     * Returns the Entry at a specific index
     * @param index
     * @return
     */
    KeyIndex<K> get(int index);

    /**
     * Add a new entry
     * @param entry
     * @return
     */
    boolean add(K entry);

    /**
     * Remove an entry
     * @param entry
     * @return
     */
    RemoveResult<K> remove(K entry);

    /**
     * Returns the Root-Hash and the amount of hashes
     */
    HashWithCount getHashWithCount();

    /**
     * Returns the root-hash
     */
    byte[] getRootHash();

    /**
     * Returns the Root-Node
     */
    MerkleNode<K> getRoot();

    /**
     * Pretty prints this tree for debugging: ((1 2) (3 4))
     */
    String toPrettyString();

    /**
     * Iterate over every node. check Leafnodes with node.isLeafNode()
     * @param function
     * @return <code>false</code> when done
     */
    boolean iterateNodes(IterateFunction<K> function);
}