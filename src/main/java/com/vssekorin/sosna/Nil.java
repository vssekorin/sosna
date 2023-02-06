package com.vssekorin.sosna;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Nil<T> extends List<T> {

    private static final Nil<?> INSTANCE = new Nil<>();

    private Nil() {
    }

    @SuppressWarnings("unchecked")
    static <T> Nil<T> instance() {
        return (Nil<T>) INSTANCE;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public T head() {
        throw new NoSuchElementException("head() of empty list");
    }

    @Override
    public Optional<T> headOpt() {
        return Optional.empty();
    }

    @Override
    public List<T> tail() {
        throw new UnsupportedOperationException("tail() of empty list");
    }

    @Override
    public Tuple2<T, List<T>> uncons() {
        throw new UnsupportedOperationException("uncons() of empty list");
    }

    @Override
    public List<T> append(T value) {
        return new Cons<>(value, this);
    }

    @Override
    public List<T> appendAll(Iterable<? extends T> values) {
        return List.ofAll(values);
    }

    @Override
    public T last() {
        throw new NoSuchElementException("last() of empty list");
    }

    @Override
    public Optional<T> lastOpt() {
        return Optional.empty();
    }

    @Override
    public T get(int n) {
        throw new NoSuchElementException("get() of empty list");
    }

    @Override
    public Optional<T> getOpt(int n) {
        return Optional.empty();
    }

    @Override
    public T getOr(int n, T defaultValue) {
        return defaultValue;
    }

    @Override
    public T getOrGet(int n, Supplier<T> defaultValue) {
        return defaultValue.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> or(Iterable<? extends T> other) {
        return other instanceof List ? (List<T>) other : List.ofAll(other);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> or(Supplier<? extends Iterable<? extends T>> supplier) {
        Iterable<? extends T> other = supplier.get();
        return other instanceof List ? (List<T>) other : List.ofAll(other);
    }

    @Override
    public <U> U match(Supplier<? extends U> ifNil, BiFunction<T, Seq<T>, ? extends U> ifCons) {
        return ifNil.get();
    }

    @Override
    public <U> U match(
        Supplier<? extends U> ifNil,
        Function<T, ? extends U> ifSingle,
        Function<T, Function<T, Function<Seq<T>, ? extends U>>> ifMultiple
    ) {
        return ifNil.get();
    }

    @Override
    public T reduceLeft(BiFunction<? super T, ? super T, ? extends T> func) {
        throw new NoSuchElementException("reduceLeft of empty list");
    }

    @Override
    public T reduceRight(BiFunction<? super T, ? super T, ? extends T> func) {
        throw new NoSuchElementException("reduceRight of empty list");
    }
}
