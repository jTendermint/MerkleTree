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
package com.github.jtendermint.merkletree.byteable.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.github.jtendermint.merkletree.byteable.types.ByteableLong;
import org.junit.Test;

import com.github.jtendermint.crypto.ByteUtil;

public class ByteableLongTest {

    @Test
    public void testByteAbleLong() {

        ByteableLong long1 = new ByteableLong(0l);

        assertEquals(long1.value, 0l);

        assertEquals(0, long1.compareTo(new ByteableLong(0l)));
        assertEquals(-1, long1.compareTo(new ByteableLong(1l)));
        assertEquals(1, long1.compareTo(new ByteableLong(-1l)));

        assertEquals(new ByteableLong(0l), long1);

        assertEquals(ByteUtil.toString00(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }), ByteUtil.toString00(long1.toByteArray()));

        assertEquals(-1, long1.compareTo(null));
    }

    @Test
    public void testEqualsHashCodeToString() {
        ByteableLong long1 = new ByteableLong(1l);

        assertFalse(long1.equals(null));
        assertFalse(long1.equals((ByteableLong) null));
        assertFalse(long1.equals(new String()));
        assertFalse(long1.equals(new ByteableLong(2l)));

        assertEquals("1", long1.toString());
        assertEquals(new ByteableLong(0l), new ByteableLong(0l));

        assertEquals(1, long1.hashCode());

    }

}
