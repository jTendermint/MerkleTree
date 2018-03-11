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

import java.util.Arrays;

public class MerkleTree<K extends IByteable> implements IMerkleTree<K> {

    private MerkleNode<K> rootNode;

    @Override
    public int size() {
        return rootNode == null ? 0 : rootNode.getSize();
    }

    @Override
    public int getHeight() {
        return rootNode == null ? 0 : rootNode.getHeight();
    }

    @Override
    public boolean contains(K key) {
        return rootNode == null ? false : rootNode.contains(key);
    }

    @Override
    public K get(K entry) {
        return rootNode == null ? null : rootNode.get(entry);
    }

    @Override
    public KeyIndex<K> get(int index) {
        return rootNode == null ? null : rootNode.get(index);
    }

    @Override
    public boolean add(K entry) {
        if (rootNode == null) {
            rootNode = createNode(entry);
            return false;
        } else {
            AddResult<K> result = rootNode.add(entry);
            rootNode = result.getNode();
            return result.wasUpdated();
        }
    }

    @Override
    public RemoveResult<K> remove(K key) {
        if (rootNode == null) {
            return null;
        }

        boolean result = rootNode.remove(key);
        // rootNode = result.getRootNode();
        // TODO Implement
        return null;
    }

    @Override
    public HashWithCount getHashWithCount() {
        HashWithCount result = new HashWithCount(null, 0);
        if (rootNode != null) {
            result = rootNode.getHashWithCount();
        }
        return result;
    }

    @Override
    public byte[] getRootHash() {
        if (rootNode == null) {
            return null;
        } else {
            byte[] rootHash = rootNode.getHashWithCount().hash;
            return rootHash != null ? Arrays.copyOf(rootHash, rootHash.length) : null;
        }
    }

    @Override
    public MerkleNode<K> getRoot() {
        return rootNode;
    }

    @Override
    public String toPrettyString() {
        if (rootNode == null) {
            return "()";
        }
        return rootNode.toPrettyString();
    }

    @Override
    public boolean iterateNodes(IterateFunction<K> function) {
        if (rootNode != null) {
            return rootNode.iterateNodes(function);
        }
        return false;
    }
    
    protected MerkleNode<K> createNode(K entry) {
       return new MerkleNode<K>(entry);
    }

}
