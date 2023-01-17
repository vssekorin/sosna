package com.vssekorin.sosna;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class EitherTest {

    @Test
    void testCondRight() {
        Either<String, Integer> right = Either.cond(true, 6, "abc");
        assertTrue(right.isRight());
        assertEquals(6, right.right());
    }

    @Test
    void testCondLeft() {
        Either<String, Integer> left = Either.cond(false, 6, "abc");
        assertTrue(left.isLeft());
        assertEquals("abc", left.left());
    }

    @Test
    void testCondSupplierRight() {
        Either<String, Integer> right = Either.cond(true, () -> 6, () -> "abc");
        assertTrue(right.isRight());
        assertEquals(6, right.get());
    }

    @Test
    void testCondSupplierLeft() {
        Either<String, Integer> left = Either.cond(false, () -> 6, () -> "abc");
        assertTrue(left.isLeft());
        assertEquals("abc", left.left());
    }

    @Test
    void testOfNullableNull() {
        final Integer v = null;
        Either<String, Integer> either = Either.ofNullable(v);
        assertTrue(either.isLeft());
    }

    @Test
    void testOfNullableSame() {
        final Integer v = 6;
        Either<String, Integer> either = Either.ofNullable(v);
        assertTrue(either.isRight());
        assertEquals(v, either.get());
    }

    @Test
    void testOfNullableOrNull() {
        final Integer v = null;
        Either<String, Integer> either = Either.ofNullable(v, "abc");
        assertTrue(either.isLeft());
        assertEquals("abc", either.left());
    }

    @Test
    void testOfNullableOrSame() {
        final Integer v = 6;
        Either<String, Integer> either = Either.ofNullable(v, "abc");
        assertTrue(either.isRight());
        assertEquals(v, either.get());
    }

    @Test
    void testOfNullableOrSupplierNull() {
        final Integer v = null;
        Either<String, Integer> either = Either.ofNullable(v, () -> "abc");
        assertTrue(either.isLeft());
        assertEquals("abc", either.left());
    }

    @Test
    void testOfNullableOrSupplierSame() {
        final Integer v = 6;
        Either<String, Integer> either = Either.ofNullable(v, () -> "abc");
        assertTrue(either.isRight());
        assertEquals(v, either.get());
    }

    @Test
    void caught() {
        Either<Throwable, Integer> either = Either.caught(
            () -> Arrays.asList(1, 2, 3).get(10)
        );
        assertTrue(either.isLeft());
        assertTrue(either.left() instanceof ArrayIndexOutOfBoundsException);
    }

    @Test
    void leftIsLeft() {
        Either<Integer, String> either = Either.left(6);
        assertTrue(either.isLeft());
        assertFalse(either.isRight());
    }

    @Test
    void rightIsRight() {
        Either<String, Integer> either = Either.right(6);
        assertTrue(either.isRight());
        assertFalse(either.isLeft());
    }

    @Test
    void getOrNullRight() {
        Either<String, Integer> either = Either.right(6);
        assertEquals(6, either.getOrNull());
    }

    @Test
    void getOrNullLeft() {
        Either<String, Integer> either = Either.left("6");
        assertNull(either.getOrNull());
    }

    @Test
    void getOrRight() {
        Either<String, Integer> either = Either.right(6);
        assertEquals(6, either.getOr(10));
    }

    @Test
    void getOrLeft() {
        Either<String, Integer> either = Either.left("6");
        assertEquals(10, either.getOr(10));
    }

    @Test
    void toOptRight() {
        Either<String, Integer> either = Either.right(6);
        Optional<Integer> result = either.toOpt();
        assertTrue(result.isPresent());
        assertEquals(6, result.get());
    }

    @Test
    void toOptLeft() {
        Either<String, Integer> either = Either.left("6");
        Optional<Integer> result = either.toOpt();
        assertTrue(result.isEmpty());
    }

    @Test
    void toListRight() {
        Either<String, Integer> either = Either.right(6);
        assertEquals(List.of(6), either.toList());
    }

    @Test
    void toListLeft() {
        Either<String, Integer> either = Either.left("6");
        assertEquals(List.empty(), either.toList());
    }

    @Test
    void swapRight() {
        Either<String, Integer> either = Either.right(6);
        Either<Integer, String> result = either.swap();
        assertTrue(result.isLeft());
        assertEquals(6, result.left());
    }

    @Test
    void swapLeft() {
        Either<String, Integer> either = Either.left("6");
        Either<Integer, String> result = either.swap();
        assertTrue(result.isRight());
        assertEquals("6", result.right());
    }

    @Test
    void mapLeftRight() {
        Either<String, Integer> either = Either.right(6);
        Either<List<String>, Integer> result = either.mapLeft(List::of);
        assertTrue(result.isRight());
        assertEquals(6, result.right());
    }

    @Test
    void mapLeftLeft() {
        Either<String, Integer> either = Either.left("6");
        Either<List<String>, Integer> result = either.mapLeft(List::of);
        assertTrue(result.isLeft());
        assertEquals(List.of("6"), result.left());
    }

    @Test
    void mapRightRight() {
        Either<String, Integer> either = Either.right(6);
        Either<String, List<Integer>> result = either.mapRight(List::of);
        assertTrue(result.isRight());
        assertEquals(List.of(6), result.right());
    }

    @Test
    void mapRightLeft() {
        Either<String, Integer> either = Either.left("6");
        Either<String, List<Integer>> result = either.mapRight(List::of);
        assertTrue(result.isLeft());
        assertEquals("6", result.left());
    }

    @Test
    void allRightCorrect() {
        Either<String, Integer> either = Either.right(6);
        assertTrue(either.all(v -> v > 0));
    }

    @Test
    void allRightIncorrect() {
        Either<String, Integer> either = Either.right(6);
        assertFalse(either.all(v -> v > 10));
    }

    @Test
    void allLeft() {
        Either<String, Integer> either = Either.left("6");
        assertTrue(either.all(v -> v > 0));
    }

    @Test
    void anyRightCorrect() {
        Either<String, Integer> either = Either.right(6);
        assertTrue(either.any(v -> v > 0));
    }

    @Test
    void anyRightIncorrect() {
        Either<String, Integer> either = Either.right(6);
        assertFalse(either.any(v -> v > 10));
    }

    @Test
    void anyLeft() {
        Either<String, Integer> either = Either.left("6");
        assertFalse(either.any(v -> v > 0));
    }

    @Test
    void foldRight() {
        Either<String, Integer> either = Either.right(6);
        Integer result = either.fold(Integer::valueOf, r -> r + 2);
        assertEquals(8, result);
    }

    @Test
    void foldLeft() {
        Either<String, Integer> either = Either.left("6");
        Integer result = either.fold(Integer::valueOf, r -> r + 2);
        assertEquals(6, result);
    }

    @Test
    void zipRight() {
        Either<String, Integer> either = Either.right(6);
        Either<String, Tuple2<Integer, String>> result = either.zip("abc");
        assertTrue(result.isRight());
        assertEquals(Tuple.of(6, "abc"), result.right());
    }

    @Test
    void zipLeft() {
        Either<String, Integer> either = Either.left("6");
        Either<String, Tuple2<Integer, String>> result = either.zip("abc");
        assertTrue(result.isLeft());
        assertSame(either, result);
    }

    @Test
    void testRunRight() {
        Either<String, Integer> either = Either.right(6);
        AtomicInteger i = new AtomicInteger(10);
        either.run(i::addAndGet);
        assertEquals(16, i.get());
        assertEquals(6, either.right());
    }

    @Test
    void testRunLeft() {
        Either<String, Integer> either = Either.left("6");
        AtomicInteger i = new AtomicInteger(10);
        either.run(i::addAndGet);
        assertEquals(10, i.get());
        assertEquals("6", either.left());
    }

    @Test
    void testOrRunRight() {
        Either<String, Integer> either = Either.right(6);
        AtomicInteger i = new AtomicInteger(10);
        either.orRun(l -> i.addAndGet(10));
        assertEquals(10, i.get());
        assertEquals(6, either.right());
    }

    @Test
    void testOrRunLeft() {
        Either<String, Integer> either = Either.left("6");
        AtomicInteger i = new AtomicInteger(10);
        either.orRun(l -> i.addAndGet(10));
        assertEquals(20, i.get());
        assertEquals("6", either.left());
    }

    @Test
    void testOrRunLeftWithValue() {
        Either<String, Integer> either = Either.left("6");
        AtomicInteger i = new AtomicInteger(10);
        either.orRun(l -> i.addAndGet(Integer.parseInt(l)));
        assertEquals(16, i.get());
        assertEquals("6", either.left());
    }

    @Test
    void testOrRunRunnable() {
        Either<String, Integer> either = Either.left("6");
        AtomicInteger i = new AtomicInteger(10);
        either.orRun(() -> i.addAndGet(10));
        assertEquals(20, i.get());
        assertEquals("6", either.left());
    }
}
