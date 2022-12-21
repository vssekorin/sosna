package com.vssekorin.sosna;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Seq<T> extends Serializable, Iterable<T>, Function<Integer, T>, IntFunction<T> {

    boolean isEmpty();

    default boolean nonEmpty() {
        return !isEmpty();
    }

    int size();

    T head();

    Optional<T> headOpt();

    Seq<T> tail();

    java.util.List<T> asJava();

    Seq<T> prepend(T value);

    Seq<T> append(T value);

    boolean contains(T value);

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

    Seq<T> or(Iterable<? extends T> other);

    Seq<T> or(Supplier<? extends Iterable<? extends T>> supplier);

    Seq<T> reverse();

    T last();

    Optional<T> lastOpt();

    Seq<T> insert(int pos, T value);

    Seq<T> with(int pos, T value);

    <U> List<U> map(Function<T, ? extends U> mapper);

    <U> List<U> mapIndexed(BiFunction<Integer, T, ? extends U> mapper);

    <U> U match(Supplier<? extends U> ifNil, BiFunction<T, Seq<T>, ? extends U> ifCons);

    <U> U match(
        Supplier<? extends U> ifNil,
        Function<T, ? extends U> ifSingle,
        Function<T, Function<T, Function<Seq<T>, ? extends U>>> ifMultiple
    );

    Seq<T> plus(final List<T> other);
}
