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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

import com.github.jtendermint.crypto.ByteUtil;
import com.github.jtendermint.crypto.HashFunction;
import com.github.jtendermint.crypto.RipeMD160;
import com.github.jtendermint.merkletree.byteable.types.IByteable;

public class MerkleNode<K extends IByteable> {

    private K key;
    private int height;
    private int size;

    private byte[] hash;
    private byte[] leftChildHash;
    private byte[] rightChildHash;

    private MerkleNode<K> leftChildNode;
    private MerkleNode<K> rightChildNode;
    
    protected HashFunction hashFunction = new RipeMD160();

    public MerkleNode(K key) {
        this(key, 0, 1, null, null, null, null);
    }

    public MerkleNode(K key, MerkleNode<K> leftNode, MerkleNode<K> rightNode) {
        this(key, 1, 2, null, leftNode, null, rightNode);
    }

    private MerkleNode(K key, int height, int size, byte[] leftHash, MerkleNode<K> leftNode, byte[] rightHash, MerkleNode<K> rightNode) {
        this.key = key;
        this.height = height;
        this.size = size;
        this.hash = null;
        this.leftChildHash = leftHash == null ? null : Arrays.copyOf(leftHash, leftHash.length);
        this.leftChildNode = leftNode;
        this.rightChildHash = rightHash == null ? null : Arrays.copyOf(rightHash, rightHash.length);
        this.rightChildNode = rightNode;
    }

    public K getKey() {
        return key;
    }

    public int getSize() {
        return size;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(K entry) {
        return get(entry) != null;
    }

    public KeyIndex<K> get(int index) {
        if (this.height == 0) {
            if (index == 0) {
                return new KeyIndex<K>(key, true, 0);
            } else {
                throw new RuntimeException("Asked for index > 0 with a height of 0");
            }
        } else {
            if (index < leftChildNode.size) {
                return leftChildNode.get(index);
            } else {
                return rightChildNode.get(index - leftChildNode.size);
            }
        }
    }

    public K get(K entry) {
        if (entry != null && entry.equals(key)) {
            return key;
        } else if (this.height == 0) {
            return null;
        } else {
            if (entry.compareTo(key) < 0) {
                return this.leftChildNode.get(entry);
            } else {
                return this.rightChildNode.get(entry);
            }
        }
    }

    public AddResult<K> add(K entry) {
        int compareResult = entry.compareTo(this.key);
        if (height == 0) {
            if (compareResult < 0) {
                MerkleNode<K> newNode = new MerkleNode<>(this.key, createNode(entry), this);
                return new AddResult<K>(newNode, false);
            } else if (compareResult == 0) {
                return new AddResult<K>(createNode(entry), true);
            } else {
                MerkleNode<K> newNode = new MerkleNode<>(entry, this, createNode(entry));
                return new AddResult<K>(newNode, false);
            }
        } else {
            MerkleNode<K> newNode = this.createCopy();
            AddResult<K> newNodeResult;
            if (entry.compareTo(newNode.key) < 0) {
                newNodeResult = newNode.leftChildNode.add(entry);
                newNode.leftChildNode = newNodeResult.getNode(); //newNodeResult.getNode();
                newNode.leftChildHash = null;
            } else {
                newNodeResult = newNode.rightChildNode.add(entry);
                newNode.rightChildNode = newNodeResult.getNode();
                newNode.rightChildHash = null;
            }

            if (newNodeResult.wasUpdated()) {
                return new AddResult<K>(newNode, true);
            } else {
                newNode.updateHeightAndSize();
                return new AddResult<K>(newNode.balance(), false);
            }
        }
    }

    private MerkleNode<K> balance() {
        int balance = this.getBalance();
        if (balance > 1) {
            if (this.leftChildNode.getBalance() >= 0) {
                // Left Left Case
                return this.rotateRight();
            } else {
                // Left Right Case
                MerkleNode<K> newNode = this.createCopy();
                newNode.leftChildHash = null;
                newNode.leftChildNode = newNode.leftChildNode.rotateLeft();
                return newNode.rotateRight();
            }
        }
        if (balance < -1) {
            if (this.rightChildNode.getBalance() <= 0) {
                // Right Right Case
                return this.rotateLeft();
            } else {
                // Right Left Case
                MerkleNode<K> newNode = this.createCopy();
                newNode.rightChildHash = null;
                newNode.rightChildNode = newNode.rightChildNode.rotateRight();
                return newNode.rotateLeft();
            }
        }
        // no changes - balanced
        return this;
    }

    private MerkleNode<K> rotateLeft() {
        MerkleNode<K> newNode = this.createCopy();
        MerkleNode<K> rightCopy = newNode.rightChildNode.createCopy();

        newNode.rightChildHash = rightCopy.leftChildHash;
        newNode.rightChildNode = rightCopy.leftChildNode;
        rightCopy.leftChildNode = newNode;

        newNode.updateHeightAndSize();
        rightCopy.updateHeightAndSize();
        return rightCopy;
    }

    private MerkleNode<K> rotateRight() {
        MerkleNode<K> newNode = this.createCopy();
        MerkleNode<K> leftCopy = newNode.leftChildNode.createCopy();

        newNode.leftChildHash = leftCopy.rightChildHash;
        newNode.leftChildNode = leftCopy.rightChildNode;
        leftCopy.rightChildHash = null;
        leftCopy.rightChildNode = newNode;

        newNode.updateHeightAndSize();
        leftCopy.updateHeightAndSize();
        return leftCopy;
    }

    private int getBalance() {
        return leftChildNode.height - rightChildNode.height;
    }

    private void updateHeightAndSize() {
        this.height = Math.max(leftChildNode.getHeight(), rightChildNode.getHeight()) + 1;
        this.size = leftChildNode.getSize() + rightChildNode.getSize();
    }

    public boolean remove(K entry) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public HashWithCount getHashWithCount() {
        //LOG.debug("Starting hashWithCount at height={}", height);
        if (this.hash != null) {
            //LOG.debug("Node already had a hash. Returning 0-hashcount");
            return new HashWithCount(this.hash, 0);
        }
        try (ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream()) {
            int hashCount = this.writeHashBytes(byteOutStream);
            //LOG.debug("Done hashWithCount at height: {} with hashcount={}", this.height, hashCount);
            this.hash = hashFunction.hashBytes(byteOutStream.toByteArray());
            return new HashWithCount(this.hash, hashCount + 1);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private int writeHashBytes(ByteArrayOutputStream bos) throws IOException {
        int hashCount = 0;
        // TODO overflow / negative ?
        bos.write((byte) this.height);
        ByteUtil.writeWithVarint(BigInteger.valueOf(size).toByteArray(), bos);

        // TODO what does the following mean?

        if (this.height == 0) {
            ByteUtil.writeWithVarint(this.key.toByteArray(), bos);
        } else {
            if (this.leftChildNode != null) {
                HashWithCount leftHashCount = this.leftChildNode.getHashWithCount();
                this.leftChildHash = Objects.requireNonNull(leftHashCount.hash, "this.leftHash was null in writeHashBytes");
                hashCount += leftHashCount.count;
            }
            ByteUtil.writeWithVarint(this.leftChildHash, bos);
            if (this.rightChildNode != null) {
                HashWithCount rightHashCount = this.rightChildNode.getHashWithCount();
                this.rightChildHash = Objects.requireNonNull(rightHashCount.hash, "this.rightHash was null in writeHashBytes");
                hashCount += rightHashCount.count;
            }
            ByteUtil.writeWithVarint(this.rightChildHash, bos);

        }
        return hashCount;
    }

    public byte[] save() {

        if (hash == null) {
            hash = getHashWithCount().hash;
        }

        if (leftChildNode != null) {
            leftChildHash = leftChildNode.save();
        }

        if (rightChildNode != null) {
            rightChildHash = rightChildNode.save();
        }

        return Arrays.copyOf(hash, hash.length);
    }

    public MerkleNode<K> createCopy() {
        if (this.height == 0) {
            throw new RuntimeException("Cannot copy Value-Nodes");
        } else {
            return new MerkleNode<K>(key, this.height, this.size, this.leftChildHash, this.leftChildNode, this.rightChildHash,
                    this.rightChildNode);
        }
    }

    public String toPrettyString() {
        if (this.height == 0) {
            return String.valueOf(new BigInteger(key.toByteArray()).intValue());
        } else {
            return "(" + this.leftChildNode.toPrettyString() + " " + this.rightChildNode.toPrettyString() + ")";
        }
    }

    public boolean isLeafNode() {
        return height == 0;
    }

    public boolean iterateNodes(IterateFunction<K> func) {
        boolean stop = func.currentNode(this);
        if (stop) {
            return true;
        }
        if (this.height > 0) {
            stop = this.leftChildNode.iterateNodes(func);
            if (stop) {
                return true;
            }
            stop = this.rightChildNode.iterateNodes(func);
            if (stop) {
                return true;
            }
        }
        return false;
    }
    
    protected MerkleNode<K> createNode(K entry) {
        return new MerkleNode<K>(entry);
    }

}
