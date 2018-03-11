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
package com.github.jtendermint.merkletree.iavl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import com.github.jtendermint.merkletree.iavl.IterateFunct.Loop;
import com.github.jtendermint.merkletree.HashWithCount;

public class Node<K extends Comparable<K>> {

    private K value;
    private int height;
    private int size;

    private byte[] hash;
    private byte[] leftChildHash;
    private byte[] rightChildHash;

    private Node<K> leftChildNode;
    private Node<K> rightChildNode;

    private Hashing<K> hashFunction;

    public Node<K> init(K value) {
        return init(value, 0, 1, null, null, null, null);
    }
    public Node<K> init(K key, Node<K> leftNode, Node<K> rightNode) {
        return init(key, 1, 2, null, leftNode, null, rightNode);
    }

    public Node<K> init(K value, int height, int size, byte[] leftHash, Node<K> leftNode, byte[] rightHash, Node<K> rightNode) {
        this.value = value;
        this.height = height;
        this.size = size;
        this.hash = null;
        this.leftChildHash = leftHash == null ? null : Arrays.copyOf(leftHash, leftHash.length);
        this.leftChildNode = leftNode;
        this.rightChildHash = rightHash == null ? null : Arrays.copyOf(rightHash, rightHash.length);
        this.rightChildNode = rightNode;
        return this;
    }

    public K getValue() {
        return value;
    }

    public Node<K> setHashFunction(Hashing<K> hash) {
        this.hashFunction = hash;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return size;
    }

    public boolean contains(K value) {
        return get(value) != null;
    }

    public K get(K entry) {
        if (entry != null && entry.equals(value)) {
            return value;
        } else if (this.height == 0) {
            return null;
        } else {
            if (entry.compareTo(value) < 0) {
                return this.leftChildNode.get(entry);
            } else {
                return this.rightChildNode.get(entry);
            }
        }
    }

    public KeyIndex<K> get(int index) {
        if (this.height == 0) {
            if (index == 0) {
                return new KeyIndex<K>(value, true, 0);
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

    public AddResult<K> add(K value) {
        int compareResult = value.compareTo(this.value);
        if (height == 0) {
            if (compareResult < 0) {
                Node<K> newNode = new Node<>();
                newNode.init(this.value, this.newNode().init(value).setHashFunction(hashFunction), this).setHashFunction(hashFunction);
                return new AddResult<K>(newNode, false);
            } else if (compareResult == 0) {
                return new AddResult<K>(this.newNode().init(value).setHashFunction(hashFunction), true);
            } else {
                Node<K> newNode = new Node<>();
                newNode.init(value, this, this.newNode().init(value).setHashFunction(hashFunction)).setHashFunction(hashFunction);
                return new AddResult<K>(newNode, false);
            }
        } else {
            Node<K> newNode = this.createCopy();
            AddResult<K> newNodeResult;
            if (value.compareTo(newNode.value) < 0) {
                newNodeResult = newNode.leftChildNode.add(value);
                newNode.leftChildNode = newNodeResult.getNode(); // newNodeResult.getNode();
                newNode.leftChildHash = null;
            } else {
                newNodeResult = newNode.rightChildNode.add(value);
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
    public boolean remove(K entry) {
        return false;
    }

    private Node<K> balance() {
        int balance = this.getBalance();
        if (balance > 1) {
            if (this.leftChildNode.getBalance() >= 0) {
                // Left Left Case
                return this.rotateRight();
            } else {
                // Left Right Case
                Node<K> newNode = this.createCopy();
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
                Node<K> newNode = this.createCopy();
                newNode.rightChildHash = null;
                newNode.rightChildNode = newNode.rightChildNode.rotateRight();
                return newNode.rotateLeft();
            }
        }
        // no changes - balanced
        return this;
    }

    private Node<K> rotateLeft() {
        Node<K> newNode = this.createCopy();
        Node<K> rightCopy = newNode.rightChildNode.createCopy();

        newNode.rightChildHash = rightCopy.leftChildHash;
        newNode.rightChildNode = rightCopy.leftChildNode;
        rightCopy.leftChildNode = newNode;

        newNode.updateHeightAndSize();
        rightCopy.updateHeightAndSize();
        return rightCopy;
    }

    private Node<K> rotateRight() {
        Node<K> newNode = this.createCopy();
        Node<K> leftCopy = newNode.leftChildNode.createCopy();

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

    public Node<K> createCopy() {
        if (this.height == 0) {
            throw new RuntimeException("Cannot copy Value-Nodes");
        } else {
            return newNode().init(value, this.height, this.size, this.leftChildHash, this.leftChildNode, this.rightChildHash,
                    this.rightChildNode).setHashFunction(hashFunction);
        }
    }

    public String toPrettyString() {
        if (this.height == 0) {
            return String.valueOf(value);
        } else {
            return "(" + this.leftChildNode.toPrettyString() + " " + this.rightChildNode.toPrettyString() + ")";
        }
    }

    public boolean isLeafNode() {
        return height == 0;
    }

    public Loop iterateNodes(IterateFunct<K> func) {
        Loop stop = func.currentNode(this);
        if (stop == Loop.STOP) {
            return stop;
        }
        if (this.height > 0) {
            stop = this.leftChildNode.iterateNodes(func);
            if (stop == Loop.STOP) {
                return Loop.STOP;
            }
            stop = this.rightChildNode.iterateNodes(func);
            if (stop == Loop.STOP) {
                return Loop.STOP;
            }
        }
        return Loop.CONTINUE;
    }

    protected Node<K> newNode() {
        return new Node<K>();
    }

    public HashWithCount getHashWithCount() {
        if (this.hash != null) {
            return new HashWithCount(this.hash, 0);
        }
        try (ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream()) {
            int hashCount = this.writeHashBytes(byteOutStream);
            this.hash = hashFunction.hashBytes(byteOutStream.toByteArray());
            return new HashWithCount(this.hash, hashCount + 1);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private int writeHashBytes(ByteArrayOutputStream bos) throws IOException {
        int hashCount = 0;
        // ByteUtil.writeWithVarint(BigInteger.valueOf(size).toByteArray(),
        // bos);
        if (this.height == 0) {
            byte[] hashBytes = hashFunction.hashBytes(this.value);
            bos.write(hashBytes);
        } else {
            if (this.leftChildNode != null) {
                HashWithCount leftHashCount = this.leftChildNode.getHashWithCount();
                this.leftChildHash = Objects.requireNonNull(leftHashCount.hash, "this.leftHash was null in writeHashBytes");
                hashCount += leftHashCount.count;
            }
            if (this.rightChildNode != null) {
                HashWithCount rightHashCount = this.rightChildNode.getHashWithCount();
                this.rightChildHash = Objects.requireNonNull(rightHashCount.hash, "this.rightHash was null in writeHashBytes");
                hashCount += rightHashCount.count;
            }
            byte[] hashBytesRight = hashFunction.hashBytes(this.rightChildHash);
            byte[] hashBytesLeft = hashFunction.hashBytes(this.leftChildHash);
            bos.write(hashBytesLeft);
            bos.write(hashBytesRight);

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

}
