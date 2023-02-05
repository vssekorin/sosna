package com.vssekorin.sosna;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {

    @Test
    void testToList() {
        Tuple3<Integer, Double, BigInteger> tuple = Tuple.of(1, 2.6, BigInteger.TEN);
        List<Number> result = tuple.toList(Number.class);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(List.of(1, 2.6, BigInteger.TEN), result);
    }

    @Test
    void testToListIncorrect() {
        Tuple3<Integer, Double, String> tuple = Tuple.of(1, 2.6, "Text");
        List<Number> result = tuple.toList(Number.class);
        assertNull(result);
    }
}
