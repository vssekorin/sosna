package com.vssekorin.sosna;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static com.vssekorin.sosna.List.nil;
import static com.vssekorin.sosna.ListTestUtil.assertListEquals;
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
        assertEquals(0, nil().size());
    }

    @Test
    void testConssize() {
        assertEquals(1, new Cons<>(1, nil()).size());
        assertEquals(2, new Cons<>(1, new Cons<>(2, nil())).size());
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
        assertEquals(2, tail.size());
        assertEquals(list.size() - 1, tail.size());
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
    void testGetFirst() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(1, list.get(0));
    }

    @Test
    void testGetLast() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(5, list.get(4));
    }

    @Test
    void testGetPenultimate() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(4, list.get(3));
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
        assertThrows(IndexOutOfBoundsException.class, () -> list.getOrNull(-1));
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
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
    }

    @Test
    void testGetOrReturnElementIfIndexIsCorrect() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertEquals(1, list.getOr(0, -1));
        assertEquals(2, list.getOr(1, -1));
        assertEquals(3, list.getOr(2, -1));
    }

    @Test
    void testGetOrReturnDefaultIfIndexIsNotCorrect() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertThrows(IndexOutOfBoundsException.class, () -> list.getOr(-1, -1));
        assertEquals(-1, list.getOr(3, -1));
    }

    @Test
    void testGetOrSupplierReturnElementIfIndexIsCorrect() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertEquals(1, list.getOr(0, () -> -1));
        assertEquals(2, list.getOr(1, () -> -1));
        assertEquals(3, list.getOr(2, () -> -1));
    }

    @Test
    void testGetOrSupplierReturnDefaultIfIndexIsNotCorrect() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertThrows(IndexOutOfBoundsException.class, () -> list.getOr(-1, () -> -1));
        assertEquals(-1, list.getOr(3, () -> -1));
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
        assertThrows(IndexOutOfBoundsException.class, () -> list.apply(-1));
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
    void testInsertConsFirst() {
        List<Integer> list = List.of(1, 2, 3).insert(0, 6);
        assertArrayEquals(new Integer[]{6, 1, 2, 3}, list.asJava().toArray());
    }

    @Test
    void testInsertConsLast() {
        List<Integer> list = List.of(1, 2, 3).insert(3, 6);
        assertArrayEquals(new Integer[]{1, 2, 3, 6}, list.asJava().toArray());
    }

    @Test
    void testInsertConsPenultimate() {
        List<Integer> list = List.of(1, 2, 3).insert(2, 6);
        assertArrayEquals(new Integer[]{1, 2, 6, 3}, list.asJava().toArray());
    }

    @Test
    void testInsertNil() {
        List<Integer> list = List.nil();
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(2, 6));
    }

    @Test
    void testWith() {
        List<Integer> list = List.of(1, 2, 3, 4, 5).with(2, 10);
        assertArrayEquals(new Integer[]{1, 2, 10, 4, 5}, list.asJava().toArray());
    }

    @Test
    void testWithFirst() {
        List<Integer> list = List.of(1, 2, 3, 4, 5).with(0, 10);
        assertArrayEquals(new Integer[]{10, 2, 3, 4, 5}, list.asJava().toArray());
    }

    @Test
    void testWithLast() {
        List<Integer> list = List.of(1, 2, 3, 4, 5).with(4, 10);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 10}, list.asJava().toArray());
    }

    @Test
    void testWithNil() {
        List<Integer> list = List.<Integer>nil();
        assertThrows(IndexOutOfBoundsException.class, () -> list.with(20, 10));
    }

    @Test
    void testWithWithException() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertThrows(IndexOutOfBoundsException.class, () -> list.with(20, 10));
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
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(2, list.match(() -> 1, (h, t) -> 2));
    }

    @Test
    void testConsPlusCons() {
        List<Integer> list = List.of(1, 2, 3);
        List<Integer> other = List.of(4, 5, 6);
        assertListEquals(List.of(1, 2, 3, 4, 5, 6), list.plus(other));
    }

    @Test
    void testNilPlusCons() {
        List<Integer> list = List.nil();
        List<Integer> other = List.of(4, 5, 6);
        assertListEquals(List.of(4, 5, 6), list.plus(other));
    }

    @Test
    void testConsPlusNil() {
        List<Integer> list = List.of(1, 2, 3);
        List<Integer> other = List.nil();
        assertListEquals(List.of(1, 2, 3), list.plus(other));
    }

    @Test
    void testNilPlusNil() {
        List<Integer> list = List.nil();
        List<Integer> other = List.nil();
        assertListEquals(List.nil(), list.plus(other));
    }

    @Test
    void testMatch2Nil() {
        List<Integer> list = nil();
        String result = list.match(() -> "1", h -> "2", h1 -> h2 -> xs -> "3");
        assertEquals("1", result);
    }

    @Test
    void testMatch2OneCons() {
        List<Integer> list = List.of(6);
        String result = list.match(() -> "1", h -> "2", h1 -> h2 -> xs -> "3");
        assertEquals("2", result);
    }

    @Test
    void testMatch2MoreThanOneCons() {
        List<Integer> list = List.of(1, 2, 3);
        String result = list.match(() -> "1", h -> "2", h1 -> h2 -> xs -> "3");
        assertEquals("3", result);
    }

    @Test
    void avoidStackOverflowError() {
        final int size = 1_000_000;
        List<Integer> list = List.nil();
        for (int i = 0; i < size; i++) {
            list = new Cons<>(i, list);
        }
        assertEquals(size, list.size());
    }

    @Test
    void testAppendNil() {
        List<Integer> list = List.nil();
        assertListEquals(List.of(6), list.append(6));
    }

    @Test
    void testAppendCons() {
        List<Integer> list = List.of(1, 2, 3);
        assertListEquals(List.of(1, 2, 3, 6), list.append(6));
    }

    @Test
    void testPrependNil() {
        List<Integer> list = List.nil();
        assertListEquals(List.of(6), list.prepend(6));
    }

    @Test
    void testPrependCons() {
        List<Integer> list = List.of(1, 2, 3);
        assertListEquals(List.of(6, 1, 2, 3), list.prepend(6));
    }
}
