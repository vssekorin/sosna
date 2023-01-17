package com.vssekorin.sosna;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SeqTest {

    @Test
    void testOrRunEmpty() {
        List<Integer> list = List.empty();
        AtomicInteger i = new AtomicInteger(10);
        list.orRun(() -> i.addAndGet(6));
        assertEquals(16, i.get());
    }

    @Test
    void testOrRunNonEmpty() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);
        AtomicInteger i = new AtomicInteger(10);
        list.orRun(() -> i.addAndGet(6));
        assertEquals(10, i.get());
    }
}
