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
     * @return the tree size (elements in tree)
     */
    int size();

    /**
     * @return the tree-height
     */
    int getHeight();

    /**
     * Check if an entry is already present
     * 
     * @param entry
     *            the Entry to search for in this tree. Should not be null
     * @return true if the entry is in this tree
     */
    boolean contains(K entry);

    /**
     * Return an entry from the tree, uses {@link Object#equals(Object)} for equality-checks
     * 
     * @param entry
     *            a non-null entry to lookup in the tree
     * @return the entry from the tree or null if this entry is not in the tree
     */
    K get(K entry);

    /**
     * @param index
     *            the index of the node where the entry should be retrieved
     * @return a KeyIndex representing the element at the requested index. Never null.
     */
    KeyIndex<K> get(int index);

    /**
     * Add or update the entry to/in the tree. If this entry already existed, the existing node will be updated
     * 
     * @param entry
     *            the non-null entry to add
     * @return true if the entry has been updated in the tree, false if this entry has been newly added
     */
    boolean add(K entry);

    /**
     * Remove an entry from the tree
     * 
     * @param entry
     *            the entry to remove
     * @return a RemoveResult representing the result of the remove operation. Never null.
     */
    RemoveResult<K> remove(K entry);

    /**
     * @return the Root-Hash and the amount of hashes
     */
    HashWithCount getHashWithCount();

    /**
     * @return the root-hash. Will be null if the root-node is or if the rootHash has never been calculated.
     */
    byte[] getRootHash();

    /**
     * @return the Root-Node. May be null.
     */
    MerkleNode<K> getRoot();

    /**
     * @return a prettify string representation of this tree for debugging: ((1 2) (3 4))
     */
    String toPrettyString();

    /**
     * Iterate over every node. check Leafnodes with node.isLeafNode()
     * 
     * @param function
     *            a IterateFunction that should be executed for node
     * @return <code>false</code> when nothing has been iterated. true when iteration ended after at least one leaf
     */
    boolean iterateNodes(IterateFunction<K> function);

    /**
     * Clear this tree by removing the root node. This expectes GC to free all resources, i.e. leafs and nodes
     */
    void removeAll();
}