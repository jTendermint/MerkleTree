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

/**
 * 
 * @author wolfposd
 */
public class RemoveResult<K extends IByteable> {
    private MerkleNode<K> node;
    private byte[] hash;
    private final IByteable byteable;
    private final boolean removed;

    public RemoveResult(byte[] hash, MerkleNode<K> node, K byteable, boolean removed) {
        this.hash = Arrays.copyOf(hash, hash.length);
        this.node = node;
        this.byteable = byteable;
        this.removed = removed;
    }

    public RemoveResult(K byteable, boolean removed) {
        this.byteable = byteable == null ? null : byteable;
        this.removed = removed;
    }

    public MerkleNode<K> getNode() {
        return node;
    }

    public byte[] getHash() {
        return hash != null ? Arrays.copyOf(hash, hash.length) : null;
    }

    public boolean wasRemoved() {
        return removed;
    }

    public IByteable getByteable() {
        return byteable;
    }

}
