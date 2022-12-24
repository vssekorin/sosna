package com.vssekorin.sosna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LazyTest {

    @Test
    void testGetOf() {
        Lazy<Integer> six = Lazy.of(() -> 6);
        assertEquals(6, six.get());
        assertEquals(6, six.get());
    }

    @Test
    void testGetVal() {
        Lazy<Integer> six = Lazy.val(6);
        assertEquals(6, six.get());
        assertEquals(6, six.get());
    }

    @Test
    void testMap() {
        Lazy<Integer> six = Lazy.of(() -> 6);
        assertEquals("6", six.map(Object::toString).get());
    }
}
