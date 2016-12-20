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
package com.github.jtmsp.merkletree.byteable;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteableLong implements IByteable {

    public final long value;

    private final byte[] bytes;

    public ByteableLong(long value) {
        this.value = value;
        this.bytes = calcByteArray(value);
    }

    @Override
    public byte[] toByteArray() {
        return bytes;
    }

    private byte[] calcByteArray(long value) {
        byte[] b = ByteBuffer.allocate(Long.BYTES).putLong(value).array();
        int startOffset = 0;
        for (int i = 0; i < b.length; i++) {
            if (b[i] != 0) {
                startOffset = i;
                break;
            }
        }
        return Arrays.copyOfRange(b, startOffset, b.length);
    }

    @Override
    public int compareTo(IByteable other) {
        if (other instanceof ByteableLong)
            return Long.compare(this.value, ((ByteableLong) other).value);
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ByteableLong) {
            return value == ((ByteableLong) obj).value;
        }
        return false;
    }

    @Override
    public String toString() {
        return "" + value;
    }

}
