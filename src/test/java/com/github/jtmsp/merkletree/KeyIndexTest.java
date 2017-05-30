package com.github.jtmsp.merkletree;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class KeyIndexTest {

    @Test
    public void testAll() {
        KeyIndex<String> k = new KeyIndex<String>("test", true, 1);

        assertEquals("test", k.getEntry());
        assertEquals(true, k.doesExist());
        assertEquals(1, k.getIndex());

    }

}
