package com.vssekorin.sosna;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Seq<T>
    extends Traversable<T>, Reducible<T>, Function<Integer, T>, IntFunction<T>, Serializable {

    boolean isEmpty();

    default boolean nonEmpty() {
        return !isEmpty();
    }

    T head();

    Optional<T> headOpt();

    Seq<T> tail();

    int size();

    Tuple2<T, ? extends Seq<T>> uncons();

    java.util.List<T> asJava();

    Seq<T> prepend(T value);

    Seq<T> prependAll(Iterable<? extends T> values);

    Seq<T> append(T value);

    Seq<T> appendAll(Iterable<? extends T> values);

    Seq<T> reverse();

    T last();

    Optional<T> lastOpt();

    @Override
    default Iterator<T> iterator() {
        final Seq<T> that = this;
        return new Iterator<>() {
            private Seq<T> seq = that;

            @Override
            public boolean hasNext() {
                return !seq.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                final T result = seq.head();
                seq = seq.tail();
                return result;
            }
        };
    }

    boolean contains(Eq<T> equiv, T value);

    default boolean contains(T value) {
        return contains(Objects::equals, value);
    }

    default boolean containsAll(Seq<T> seq) {
        for (T value : seq) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    default boolean containsAny(Seq<T> seq) {
        for (T value : seq) {
            if (contains(value)) {
                return true;
            }
        }
        return false;
    }

    T get(int n);

    Optional<T> getOpt(int n);

    default T getOrNull(int n) {
        return getOr(n, null);
    }

    T getOr(int n, T defaultValue);

    T getOrGet(int n, Supplier<T> defaultValue);

    @Override
    default T apply(Integer value) {
        return getOrNull(value);
    }

    @Override
    default T apply(int value) {
        return getOrNull(value);
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default void orRun(Runnable runnable) {
        if (isEmpty()) {
            runnable.run();
        }
    }

    Seq<T> or(Iterable<? extends T> other);

    Seq<T> or(Supplier<? extends Iterable<? extends T>> supplier);

    Seq<T> insert(int pos, T value);

    Seq<T> with(int pos, T value);

    @Override
    <U> Seq<U> map(Function<T, ? extends U> mapper);

    <U> Seq<U> mapIndexed(BiFunction<Integer, T, ? extends U> mapper);

    Seq<T> mapIf(Predicate<? super T> condition, Function<? super T, ? extends T> f);

    <U> Seq<U> mapIf(
        Predicate<? super T> condition,
        Function<? super T, ? extends U> thenF,
        Function<? super T, ? extends U> elseF
    );

    Seq<T> mapIfFirst(Predicate<? super T> condition, Function<? super T, ? extends T> f);

    <U> Seq<U> mapNotNull(Function<T, ? extends U> f);

    <U> Seq<U> mapIndexedNotNull(BiFunction<Integer, T, ? extends U> f);

    Seq<T> filter(Predicate<? super T> predicate);

    Seq<T> filterNot(Predicate<? super T> predicate);

    Seq<T> filterNonNull();

    Seq<T> filterIndexed(BiPredicate<Integer, T> predicate);

    boolean all(Predicate<T> predicate);

    boolean any(Predicate<T> predicate);

    int count(Predicate<T> predicate);

    default int count(Eq<T> equiv, T value) {
        return count(v -> equiv.eq(v, value));
    }

    default int count(T value) {
        return count(v -> v == value);
    }

    <U> U match(Supplier<? extends U> ifNil, BiFunction<T, Seq<T>, ? extends U> ifCons);

    <U> U match(
        Supplier<? extends U> ifNil,
        Function<T, ? extends U> ifSingle,
        Function<T, Function<T, Function<Seq<T>, ? extends U>>> ifMultiple
    );

    Seq<T> take(int n);

    Seq<T> takeWhile(Predicate<T> cond);

    Seq<T> takeRight(int n);

    Seq<T> takeRightWhile(Predicate<T> cond);

    <U> Seq<Tuple2<T, U>> zip(Iterable<? extends U> that);

    Seq<Tuple2<T, Integer>> zipWithIndex();
}
