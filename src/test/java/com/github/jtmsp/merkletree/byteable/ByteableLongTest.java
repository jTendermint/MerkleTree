package com.github.jtmsp.merkletree.byteable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
