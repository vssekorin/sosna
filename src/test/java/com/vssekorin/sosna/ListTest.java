package com.vssekorin.sosna;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static com.vssekorin.sosna.List.nil;
import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @Test
    void testNilIsEmpty() {
        assertTrue(nil().isEmpty());
    }

    @Test
    void testConsIsNotEmpty() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, nil()));
        assertFalse(list.isEmpty());
    }

    @Test
    void testNilIsNil() {
        assertTrue(nil().isNil());
    }

    @Test
    void testConsIsNotNil() {
        List<Integer> list = new Cons<>(1, nil());
        assertFalse(list.isNil());
    }

    @Test
    void testNilIsNotNonEmpty() {
        assertFalse(nil().isNonEmpty());
    }

    @Test
    void testConsIsNonEmpty() {
        List<Integer> list = new Cons<>(1, nil());
        assertTrue(list.isNonEmpty());
    }

    @Test
    void testNilLengthIsZero() {
        assertEquals(0, nil().length());
    }

    @Test
    void testConsLength() {
        assertEquals(1, new Cons<>(1, nil()).length());
        assertEquals(2, new Cons<>(1, new Cons<>(2, nil())).length());
    }

    @Test
    void testHead() {
        assertEquals(1, new Cons<>(1, nil()).head());
        assertEquals(1, new Cons<>(1, new Cons<>(2, nil())).head());
    }

    @Test
    void testTail() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        List<Integer> tail = list.tail();
        assertEquals(2, tail.length());
        assertEquals(list.length() - 1, tail.length());
        assertEquals(2, tail.head());
    }

    @Test
    void testAsJava() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        java.util.List<Integer> javaList = list.asJava();
        assertEquals(3, javaList.size());
        assertArrayEquals(new Integer[]{1, 2, 3}, javaList.toArray());
    }

    @Test
    void testContainsTrueIfElementExists() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertTrue(list.contains(1));
        assertTrue(list.contains(2));
        assertTrue(list.contains(3));
    }

    @Test
    void testContainsFalseIfElementNotExists() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertFalse(list.contains(-1));
        assertFalse(list.contains(4));
    }

    @Test
    void testGetOrNullReturnElementIfIndexIsCorrect() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertEquals(1, list.getOrNull(0));
        assertEquals(2, list.getOrNull(1));
        assertEquals(3, list.getOrNull(2));
    }

    @Test
    void testGetOrNullReturnNullIfIndexIsNotCorrect() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertNull(list.getOrNull(-1));
        assertNull(list.getOrNull(3));
    }

    @Test
    void testGetReturnElementIfIndexIsCorrect() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void testGetThrowExceptionIfIndexIsNotCorrect() {
        List<Integer> list = List.of(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> list.get(-1));
        assertThrows(IllegalArgumentException.class, () -> list.get(3));
    }

    @Test
    void testStream() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertEquals(3, list.stream().count());
        assertArrayEquals(new Integer[]{1, 2, 3}, list.stream().toArray());
    }

    @Test
    void testApplyReturnElementIfIndexIsCorrect() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertEquals(1, list.apply(0));
        assertEquals(2, list.apply(1));
        assertEquals(3, list.apply(2));
    }

    @Test
    void testApplyReturnNullIfIndexIsNotCorrect() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertNull(list.apply(-1));
        assertNull(list.apply(3));
    }

    @Test
    void testEmpty() {
        List<Integer> list = List.empty();
        assertTrue(list.isEmpty());
    }

    @Test
    void testOfSingleElement() {
        List<Integer> list = List.of(6);
        assertTrue(list.isNonEmpty());
        assertArrayEquals(new Integer[]{6}, list.asJava().toArray());
    }

    @Test
    void testOfElements() {
        List<Integer> list = List.of(1, 2, 3);
        assertTrue(list.isNonEmpty());
        assertArrayEquals(new Integer[]{1, 2, 3}, list.asJava().toArray());
    }

    @Test
    void testOfAll() {
        List<Integer> list = List.ofAll(Arrays.asList(1, 2, 3));
        assertTrue(list.isNonEmpty());
        assertArrayEquals(new Integer[]{1, 2, 3}, list.asJava().toArray());
    }

    @Test
    void testReverse() {
        List<Integer> list = List.of(1, 2, 3).reverse();
        assertArrayEquals(new Integer[]{3, 2, 1}, list.asJava().toArray());
    }

    @Test
    void testLastCons() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(3, list.last());
    }

    @Test
    void testLastNil() {
        assertThrows(NoSuchElementException.class, () -> nil().last());
    }

    @Test
    void testInsertCons() {
        List<Integer> list = List.of(1, 2, 3).insert(2, 6);
        assertArrayEquals(new Integer[]{1, 2, 6, 3}, list.asJava().toArray());
    }

    @Test
    void testInsertNil() {
        List<Integer> list = List.<Integer>nil().insert(2, 6);
        assertFalse(list.isNil());
        assertArrayEquals(new Integer[]{6}, list.asJava().toArray());
    }

    @Test
    void testWith() {
        List<Integer> list = List.of(1, 2, 3, 4, 5).with(2, 10);
        assertArrayEquals(new Integer[]{1, 2, 10, 4, 5}, list.asJava().toArray());
    }

    @Test
    void testWithWithException() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertThrows(IllegalArgumentException.class, () -> list.with(20, 10));
    }

    @Test
    void testOrIfEmpty() {
        List<Integer> list = List.nil();
        List<Integer> other = List.of(4, 5, 6);
        assertArrayEquals(new Integer[]{4, 5, 6}, list.or(other).asJava().toArray());
        assertEquals(other, list.or(other));
    }

    @Test
    void testOrJavaListIfEmpty() {
        List<Integer> list = List.nil();
        java.util.List<Integer> other = Arrays.asList(4, 5, 6);
        assertArrayEquals(new Integer[]{4, 5, 6}, list.or(other).asJava().toArray());
    }

    @Test
    void testOrIfNotEmpty() {
        List<Integer> list = List.of(1, 2, 3);
        List<Integer> other = List.of(4, 5, 6);
        assertArrayEquals(new Integer[]{1, 2, 3}, list.or(other).asJava().toArray());
        assertEquals(list, list.or(other));
    }

    @Test
    void testOrSupplierIfEmpty() {
        List<Integer> list = List.nil();
        Supplier<List<Integer>> other = () -> List.of(4, 5, 6);
        assertArrayEquals(new Integer[]{4, 5, 6}, list.or(other).asJava().toArray());
    }

    @Test
    void testOrSupplierJavaListIfEmpty() {
        List<Integer> list = List.nil();
        Supplier<java.util.List<Integer>> other = () -> Arrays.asList(4, 5, 6);
        assertArrayEquals(new Integer[]{4, 5, 6}, list.or(other).asJava().toArray());
    }

    @Test
    void testOrSupplierIfNotEmpty() {
        List<Integer> list = List.of(1, 2, 3);
        Supplier<List<Integer>> other = () -> List.of(4, 5, 6);
        assertArrayEquals(new Integer[]{1, 2, 3}, list.or(other).asJava().toArray());
    }

    @Test
    void testMap() {
        List<Integer> list = List.of(1, 2, 3);
        assertArrayEquals(new Integer[]{11, 12, 13}, list.map(v -> v + 10).asJava().toArray());
    }

    @Test
    void testMatchNil() {
        List<Integer> list = nil();
        assertEquals(1, list.match(() -> 1, (h, t) -> 2));
    }

    @Test
    void testMatchCons() {
        List<Integer> list = List.of(11, 12, 13);
        assertEquals(2, list.match(() -> 1, (h, t) -> 2));
    }
}
