package com.vssekorin.sosna;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ExtTest {

    private static class Obj implements Ext<Obj> {
        public int m() {
            return 6;
        }
    }

    @Test
    void run() {
        AtomicInteger i = new AtomicInteger(10);
        new Obj().run(o -> i.addAndGet(o.m()));
        assertEquals(16, i.get());
    }

    @Test
    void let() {
        assertEquals(8, new Obj().<Integer>let(o -> o.m() + 2));
    }

    @Test
    void also() {
        AtomicInteger i = new AtomicInteger(10);
        Obj origin = new Obj();
        Obj result = origin.also(o -> i.addAndGet(o.m()));
        assertSame(origin, result);
        assertEquals(16, i.get());
    }

    @Test
    void takeIfConditionHold() {
        Obj obj = new Obj();
        Obj result = obj.takeIf(o -> o.m() > 2);
        assertNotNull(result);
        assertSame(obj, result);
    }

    @Test
    void takeIfConditionNotHold() {
        Obj obj = new Obj();
        Obj result = obj.takeIf(o -> o.m() > 20);
        assertNull(result);
    }

    @Test
    void takeUnlessConditionHold() {
        Obj obj = new Obj();
        Obj result = obj.takeUnless(o -> o.m() > 2);
        assertNull(result);
    }

    @Test
    void takeUnlessConditionNotHold() {
        Obj obj = new Obj();
        Obj result = obj.takeUnless(o -> o.m() > 20);
        assertNotNull(result);
        assertSame(obj, result);
    }

    @Test
    void to() {
        Obj obj = new Obj();
        Tuple2<Obj, Integer> pair = obj.to(10);
        assertSame(obj, pair._1());
        assertEquals(10, pair._2());
    }
}
