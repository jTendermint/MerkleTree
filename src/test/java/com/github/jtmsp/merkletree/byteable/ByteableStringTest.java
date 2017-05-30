package com.github.jtmsp.merkletree.byteable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
