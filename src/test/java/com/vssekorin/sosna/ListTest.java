package com.vssekorin.sosna;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.vssekorin.sosna.List.nil;
import static com.vssekorin.sosna.Monoid.Int.sum;
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
        assertFalse(nil().nonEmpty());
    }

    @Test
    void testConsIsNonEmpty() {
        List<Integer> list = new Cons<>(1, nil());
        assertTrue(list.nonEmpty());
    }

    @Test
    void testNilLengthIsZero() {
        assertEquals(0, nil().size());
    }

    @Test
    void testConsSize() {
        assertEquals(1, new Cons<>(1, nil()).size());
        assertEquals(2, new Cons<>(1, new Cons<>(2, nil())).size());
    }

    @Test
    void testHead() {
        assertEquals(1, new Cons<>(1, nil()).head());
        assertEquals(1, new Cons<>(1, new Cons<>(2, nil())).head());
        assertThrowsExactly(NoSuchElementException.class, () -> nil().head());
    }

    @Test
    void testHeadOpt() {
        assertEquals(Optional.of(1), new Cons<>(1, nil()).headOpt());
        assertEquals(Optional.of(1), new Cons<>(1, new Cons<>(2, nil())).headOpt());
        assertEquals(Optional.empty(), nil().headOpt());
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
    void testContainsAllTrueIfAllElementExists() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertTrue(list.containsAll(List.of(1, 3, 2)));
        assertTrue(list.containsAll(List.of(3, 2)));
        assertTrue(list.containsAll(List.empty()));
    }

    @Test
    void testContainsAllFalseIfSomeElementNotExists() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertFalse(list.containsAll(List.of(1, 3, 4, 2)));
    }

    @Test
    void testContainsAnyTrueIfSomeElementExists() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertTrue(list.containsAny(List.of(1, 3, 2)));
        assertTrue(list.containsAny(List.of(4, 6, 3, 7)));
    }

    @Test
    void testContainsAnyFalseIfAllElementsNotExist() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertFalse(list.containsAny(List.of(4, 5, 6)));
        assertFalse(list.containsAny(List.empty()));
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
    void testGetOptFirst() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(Optional.of(1), list.getOpt(0));
    }

    @Test
    void testGetOptLast() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(Optional.of(5), list.getOpt(4));
    }

    @Test
    void testGetOptPenultimate() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(Optional.of(4), list.getOpt(3));
    }

    @Test
    void testGetOptIfWrongIndex() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(Optional.empty(), list.getOpt(10));
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
        assertEquals(1, list.getOrGet(0, () -> -1));
        assertEquals(2, list.getOrGet(1, () -> -1));
        assertEquals(3, list.getOrGet(2, () -> -1));
    }

    @Test
    void testGetOrSupplierReturnDefaultIfIndexIsNotCorrect() {
        List<Integer> list = new Cons<>(1, new Cons<>(2, new Cons<>(3, nil())));
        assertThrows(IndexOutOfBoundsException.class, () -> list.getOrGet(-1, () -> -1));
        assertEquals(-1, list.getOrGet(3, () -> -1));
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
        assertTrue(list.nonEmpty());
        assertArrayEquals(new Integer[]{6}, list.asJava().toArray());
    }

    @Test
    void testOfElements() {
        List<Integer> list = List.of(1, 2, 3);
        assertTrue(list.nonEmpty());
        assertArrayEquals(new Integer[]{1, 2, 3}, list.asJava().toArray());
    }

    @Test
    void testOfAll() {
        List<Integer> list = List.ofAll(Arrays.asList(1, 2, 3));
        assertTrue(list.nonEmpty());
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
    void testLastOptCons() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(Optional.of(3), list.lastOpt());
    }

    @Test
    void testLastOptNil() {
        assertEquals(Optional.empty(), nil().lastOpt());
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
    void testMapIndexed() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(List.of(11, 13, 15), list.mapIndexed((i, v) -> v + 10 + i));
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
        assertEquals(List.of(6), list.append(6));
    }

    @Test
    void testAppendCons() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(List.of(1, 2, 3, 6), list.append(6));
    }

    @Test
    void testAppendAllNilNil() {
        assertEquals(nil(), nil().appendAll(nil()));
    }

    @Test
    void testAppendAllConsNil() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(list, list.appendAll(nil()));
    }

    @Test
    void testAppendAllNilCons() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(list, List.<Integer>nil().appendAll(list));
    }

    @Test
    void testAppendAllConsCons() {
        List<Integer> first = List.of(1, 2, 3);
        List<Integer> second = List.of(4, 5, 6);
        assertEquals(List.of(1, 2, 3, 4, 5, 6), first.appendAll(second));
    }

    @Test
    void testPrependNil() {
        List<Integer> list = List.nil();
        assertEquals(List.of(6), list.prepend(6));
    }

    @Test
    void testPrependCons() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(List.of(6, 1, 2, 3), list.prepend(6));
    }

    @Test
    void testPrependAllNilNil() {
        assertEquals(nil(), nil().prependAll(nil()));
    }

    @Test
    void testPrependAllConsNil() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(list, list.prependAll(nil()));
    }

    @Test
    void testPrependAllNilCons() {
        List<Integer> list = List.of(1, 2, 3);
        assertEquals(list, List.<Integer>nil().prependAll(list));
    }

    @Test
    void testPrependAllConsCons() {
        List<Integer> first = List.of(1, 2, 3);
        List<Integer> second = List.of(4, 5, 6);
        assertEquals(List.of(4, 5, 6, 1, 2, 3), first.prependAll(second));
    }

    @Test
    void testTakeNil() {
        assertEquals(nil(), nil().take(3));
    }

    @Test
    void testTakeConsCorrect() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(List.of(1, 2, 3), list.take(3));
    }

    @Test
    void testTakeConsMore() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(list, list.take(10));
    }

    @Test
    void testTakeWhileNil() {
        assertEquals(nil(), List.<Integer>nil().takeWhile(v -> v <= 3));
    }

    @Test
    void testTakeWhileConsCorrect() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(List.of(1, 2, 3), list.takeWhile(v -> v <= 3));
    }

    @Test
    void testTakeWhileConsMore() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(list, list.takeWhile(v -> v <= 30));
    }

    @Test
    void testTakeRightNil() {
        assertEquals(nil(), nil().takeRight(3));
    }

    @Test
    void testTakeRightConsCorrect() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(List.of(3, 4, 5), list.takeRight(3));
    }

    @Test
    void testTakeRightConsMore() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(list, list.takeRight(10));
    }

    @Test
    void testTakeRightWhileNil() {
        assertEquals(nil(), List.<Integer>nil().takeRightWhile(v -> v >= 3));
    }

    @Test
    void testTakeRightWhileConsCorrect() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(List.of(3, 4, 5), list.takeRightWhile(v -> v >= 3));
    }

    @Test
    void testTakeRightWhileConsMore() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(list, list.takeRightWhile(v -> v <= 10));
    }

    @Test
    void testEqualTrue() {
        assertEquals(List.of(1, 2, 3), List.of(1, 2, 3));
        assertEquals(nil(), nil());
    }

    @Test
    void testEqualFalse() {
        assertNotEquals(List.of(1, 2, 3), List.of(1, 2, 3, 4));
        assertNotEquals(List.of(1, 2, 3), List.of(1, 4, 3));
        assertNotEquals(List.of(1, 2, 3), nil());
        assertNotEquals(nil(), List.of(1, 2, 3));
    }

    @Test
    void testFoldLeft() {
        assertEquals(16, List.of(1, 2, 3).foldLeft(10, Integer::sum));
    }

    @Test
    void testFoldRight() {
        assertEquals(16, List.of(1, 2, 3).foldRight(10, Integer::sum));
    }

    @Test
    void testFoldLeftCurry() {
        assertEquals(16, List.of(1, 2, 3).foldLeft(10, acc -> v -> acc + v));
    }

    @Test
    void testFoldRightCurry() {
        assertEquals(16, List.of(1, 2, 3).foldRight(10, v -> acc -> acc + v));
    }

    @Test
    void testReduceLeft() {
        assertEquals(10, List.of(1, 2, 3, 4).reduceLeft(Integer::sum));
    }

    @Test
    void testReduceLeftNil() {
        assertThrowsExactly(NoSuchElementException.class, () -> List.<Integer>nil().reduceLeft(Integer::sum));
    }

    @Test
    void testReduceRight() {
        assertEquals(10, List.of(1, 2, 3, 4).reduceRight(Integer::sum));
    }

    @Test
    void testReduceRightNil() {
        assertThrowsExactly(NoSuchElementException.class, () -> List.<Integer>nil().reduceRight(Integer::sum));
    }

    @Test
    void testReduceLeftCurry() {
        assertEquals(10, List.of(1, 2, 3, 4).reduceLeft(acc -> v -> acc + v));
    }

    @Test
    void testReduceLeftNilCurry() {
        List<Integer> list = List.nil();
        assertThrowsExactly(NoSuchElementException.class, () -> list.reduceLeft(acc -> v -> acc + v));
    }

    @Test
    void testReduceRightCurry() {
        assertEquals(10, List.of(1, 2, 3, 4).reduceRight(v -> acc -> acc + v));
    }

    @Test
    void testReduceRightNilCurry() {
        List<Integer> list = List.nil();
        assertThrowsExactly(NoSuchElementException.class, () -> list.reduceRight(v -> acc -> acc + v));
    }

    @Test
    void testFold() {
        assertEquals(10, List.of(1, 2, 3, 4).fold(sum));
    }

    @Test
    void testReduce() {
        assertEquals(10, List.of(1, 2, 3, 4).reduce(Integer::sum));
    }

    @Test
    void testFilterFirstAndLastAreGood() {
        List<Integer> list = List.of(2, 2, 2, 3, 4, 5, 6);
        assertEquals(List.of(2, 2, 2, 4, 6), list.filter(v -> v % 2 == 0));
    }

    @Test
    void testFilterFirstIsGood() {
        List<Integer> list = List.of(2, 3, 4, 5, 6, 7, 9);
        assertEquals(List.of(2, 4, 6), list.filter(v -> v % 2 == 0));
    }

    @Test
    void testFilterLastIsGood() {
        List<Integer> list = List.of(1, 3, 4, 5, 6, 8);
        assertEquals(List.of(4, 6, 8), list.filter(v -> v % 2 == 0));
    }

    @Test
    void testFilterFirstAndLastAreBad() {
        List<Integer> list = List.of(1, 3, 4, 5, 6, 8, 7, 9);
        assertEquals(List.of(4, 6, 8), list.filter(v -> v % 2 == 0));
    }

    @Test
    void testFilterIndexed() {
        List<Integer> list = List.of(2, 2, 2, 3, 4, 4, 5, 6, 7);
        assertEquals(List.of(2, 2, 4), list.filterIndexed((i, v) -> v % 2 == 0 && i % 2 == 0));
    }

    @Test
    void testFilterNot() {
        List<Integer> list = List.of(1, 3, 4, 5, 6, 8);
        assertEquals(List.of(1, 3, 5), list.filterNot(v -> v % 2 == 0));
    }

    @Test
    void testFilterNonNull() {
        List<Integer> list = List.of(1, null, null, 2, 3, null, 4);
        assertEquals(List.of(1, 2, 3, 4), list.filterNonNull());
    }

    @Test
    void testAllTrue() {
        List<Integer> list = List.of(5, 6, 7, 8, 9);
        assertTrue(list.all(v -> v > 2));
    }

    @Test
    void testAllFalse() {
        List<Integer> list = List.of(5, 6, 7, 8, 9);
        assertFalse(list.all(v -> v < 7));
    }

    @Test
    void testAnyTrue() {
        List<Integer> list = List.of(5, 6, 7, 8, 9);
        assertTrue(list.any(v -> v == 7));
    }

    @Test
    void testAnyFalse() {
        List<Integer> list = List.of(5, 6, 7, 8, 9);
        assertFalse(list.any(v -> v < 5));
    }

    @Test
    void testCount() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        assertEquals(5, list.count(v -> v % 2 == 0));
    }

    @Test
    void testCountZero() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        assertEquals(0, list.count(v -> v < 0));
    }

    @Test
    void testCountValue() {
        List<Integer> list = List.of(1, 2, 3, 1, 4, 5, 1, 6, 6, 1);
        assertEquals(4, list.count(1));
    }

    @Test
    void testCountEq() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        assertEquals(3, list.count((x, y) -> (x - y) % 2 == 0, 1));
    }

    @Test
    void testZipWithIndex() {
        List<String> list = List.of("a", "b", "c");
        List<Tuple2<String, Integer>> expected = List.of(
            Tuple.of("a", 0), Tuple.of("b", 1), Tuple.of("c", 2)
        );
        assertEquals(expected, list.zipWithIndex());
    }

    @Test
    void testZipSameSize() {
        List<Integer> first = List.of(2, 4, 6);
        List<String> second = List.of("a", "bc", "def");
        List<Tuple2<Integer, String>> expected = List.of(
            Tuple.of(2, "a"), Tuple.of(4, "bc"), Tuple.of(6, "def")
        );
        assertEquals(expected, first.zip(second));
    }

    @Test
    void testZipFirstLonger() {
        List<Integer> first = List.of(2, 4, 6, 8, 10);
        List<String> second = List.of("a", "bc", "def");
        List<Tuple2<Integer, String>> expected = List.of(
            Tuple.of(2, "a"), Tuple.of(4, "bc"), Tuple.of(6, "def")
        );
        assertEquals(expected, first.zip(second));
    }

    @Test
    void testZipSameSecondLonger() {
        List<Integer> first = List.of(2, 4, 6);
        List<String> second = List.of("a", "bc", "def", "z", "x", "y");
        List<Tuple2<Integer, String>> expected = List.of(
            Tuple.of(2, "a"), Tuple.of(4, "bc"), Tuple.of(6, "def")
        );
        assertEquals(expected, first.zip(second));
    }

    @Test
    void testMapIfOneFunction() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        List<Integer> result = list.mapIf(v -> v % 2 == 0, v -> v + 10);
        assertEquals(List.of(1, 12, 3, 14, 5), result);
    }

    @Test
    void testMapIfTwoFunction() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        List<Integer> result = list.mapIf(v -> v % 2 == 0, v -> v + 10, v -> v - 10);
        assertEquals(List.of(-9, 12, -7, 14, -5), result);
    }

    @Test
    void testMapIfFirst() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        List<Integer> result = list.mapIfFirst(v -> v % 2 == 0, v -> v + 10);
        assertEquals(List.of(1, 12, 3, 4, 5), result);
    }

    @Test
    void testUncons() {
        List<Integer> list = List.of(1, 2, 3);
        Tuple2<Integer, List<Integer>> result = list.uncons();
        assertEquals(1, result._1());
        assertEquals(List.of(2, 3), result._2());
    }

    @Test
    void testReplicatePositive() {
        List<Integer> result = List.replicate(3, 6);
        assertEquals(List.of(6, 6, 6), result);
    }

    @Test
    void testReplicateZero() {
        List<Integer> result = List.replicate(0, 6);
        assertEquals(List.empty(), result);
    }

    @Test
    void testReplicateNegative() {
        List<Integer> result = List.replicate(-3, 6);
        assertEquals(List.empty(), result);
    }

    @Test
    void testMapNotNull() {
        List<String> list = List.of("12a", "45", "", "3");
        Function<String, Integer> toIntOrNull = s -> {
            try {
                return Integer.valueOf(s);
            } catch (NumberFormatException exc) {
                return null;
            }
        };
        assertEquals(List.of(45, 3), list.mapNotNull(toIntOrNull));
    }

    @Test
    void testMapIndexedNotNull() {
        List<String> list = List.of("12a", "45", "", "3");
        BiFunction<Integer, String, Integer> toIntOrNull = (index, s) -> {
            try {
                return Integer.parseInt(s) + index * 1000;
            } catch (NumberFormatException exc) {
                return null;
            }
        };
        assertEquals(List.of(1045, 3003), list.mapIndexedNotNull(toIntOrNull));
    }
}
