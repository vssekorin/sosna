package com.vssekorin.sosna;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class ListTestUtil {

    private ListTestUtil() {
    }

    public static <T> void assertListEquals(List<T> expected, List<T> actual) {
        assertArrayEquals(expected.asJava().toArray(), actual.asJava().toArray());
    }
}
