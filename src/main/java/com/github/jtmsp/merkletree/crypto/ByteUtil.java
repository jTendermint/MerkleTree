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
package com.github.jtmsp.merkletree.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import com.github.jtmsp.merkletree.byteable.IByteable;

public final class ByteUtil {

    public enum ByteFormat {
        FORMAT_0x00, FORMAT_00
    }

    private ByteUtil() {
        // empty constructor for util class
    }

    public static String toString(IByteable bable, ByteFormat format) {
        return toString(bable.toByteArray(), format);
    }

    public static String toString(byte[] bArr, ByteFormat format) {
        switch (format) {
            case FORMAT_00:
                return toString00(bArr);
            case FORMAT_0x00:
            default:
                return toString0x00(bArr);
        }
    }

    /**
     * Formats the byte array into separated 0x00 blocks
     * <br>
     * [ca,fe,ba,be] =&gt; 0xca 0xfe 0xba 0xbe
     * 
     * @param bable
     * @return formatted string
     */
    public static String toString0x00(IByteable bable) {
        return toString0x00(bable.toByteArray());
    }

    /**
     * Formats the byte array into separated 0x00 blocks
     * <br>
     * [ca,fe,ba,be] =&gt; 0xca 0xfe 0xba 0xbe
     * 
     * @param bArr
     * @return formatted string
     */
    public static String toString0x00(byte[] bArr) {
        if (bArr == null)
            return null;

        StringBuilder buf = new StringBuilder();
        for (byte b : bArr) {
            buf.append(String.format("0x%x ", b));
        }
        return buf.toString();
    }

    /**
     * Formats the byte array into uppercase blocks
     * <br>
     * [ca,fe,ba,be] =&gt; CAFEBABE
     * 
     * @param bable
     * @return formatted string
     */
    public static String toString00(IByteable bable) {
        return toString00(bable.toByteArray());
    }

    /**
     * Formats the byte array into uppercase blocks
     * <br>
     * [ca,fe,ba,be] =&gt; CAFEBABE
     * 
     * @param bArr
     * @return formatted string
     */
    public static String toString00(byte[] bArr) {
        if (bArr == null)
            return null;

        final StringBuilder builder = new StringBuilder();
        for (byte b : bArr) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString().toUpperCase();
    }

    public static void writeWithVarint(byte[] bytes, ByteArrayOutputStream bos) throws IOException {
        if (bytes == null) {
            bytes = new byte[0];
        }
        long length = bytes.length;
        byte[] varint = BigInteger.valueOf(length).toByteArray();
        long varintLength = varint.length;
        byte[] varintPrefix = BigInteger.valueOf(varintLength).toByteArray();
        bos.write(varintPrefix);
        if (bytes.length > 0) {
            bos.write(varint);
            bos.write(bytes);
        }
    }

}
