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
import com.github.jtendermint.merkletree.byteable.types.ByteableString;
import org.junit.Test;

import com.github.jtendermint.crypto.ByteUtil;

public class ByteableStringTest {

    @Test
    public void testByteAbleString() {

        ByteableString string1 = new ByteableString("test");

        assertEquals(string1.string, "test");

        assertEquals(0, string1.compareTo(new ByteableString("test")));
        assertEquals('t' - 'x', string1.compareTo(new ByteableString("x")));
        assertEquals('t' - 'a', string1.compareTo(new ByteableString("a")));
        assertEquals(new ByteableString("test"), string1);
        assertEquals(ByteUtil.toString00(new byte[] { 0x74, 0x65, 0x73, 0x74 }), ByteUtil.toString00(string1.toByteArray()));
        assertEquals(-1, string1.compareTo(null));
    }

    @Test
    public void testEqualsHashCodeToString() {
        ByteableString string1 = new ByteableString("test");

        assertFalse(string1.equals(null));
        assertFalse(string1.equals((ByteableString) null));
        assertFalse(string1.equals(new String()));
        assertFalse(string1.equals(new ByteableString("not test")));

        assertEquals("test", string1.toString());
        assertEquals(new ByteableLong(0l), new ByteableLong(0l));

        assertEquals("test".hashCode(), string1.hashCode());

        assertEquals(string1, new ByteableString(new byte[] { 0x74, 0x65, 0x73, 0x74 }));

    }

}
