package com.vssekorin.sosna;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Nil<T> implements List<T> {

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
    public int length() {
        return 0;
    }

    @Override
    public T head() {
        throw new NoSuchElementException("head() of empty list");
    }

    @Override
    public List<T> tail() {
        throw new UnsupportedOperationException("tail() of empty list");
    }

    @Override
    public java.util.List<T> asJava() {
        return new ArrayList<>();
    }

    @Override
    public boolean contains(final T elem) {
        return false;
    }

    @Override
    public T getOrNull(final int n) {
        return null;
    }

    @Override
    public T getOr(final int n, final T defaultValue) {
        return defaultValue;
    }

    @Override
    public T getOr(final int n, final Supplier<T> defaultValue) {
        return defaultValue.get();
    }

    @Override
    public T get(int n) {
        throw new IllegalArgumentException("get(): n is more than length");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> or(final Iterable<? extends T> other) {
        return other instanceof List ? (List<T>) other : List.ofAll(other);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> or(Supplier<? extends Iterable<? extends T>> supplier) {
        final Iterable<? extends T> other = supplier.get();
        return other instanceof List ? (List<T>) other : List.ofAll(other);
    }

    @Override
    public T last() {
        throw new NoSuchElementException("last() of empty list");
    }

    @Override
    public List<T> insert(final int pos, final T elem) {
        return new Cons<>(elem, List.nil());
    }

    @Override
    public List<T> insert(final Predicate<T> pred, final T value) {
        return new Cons<>(value, List.nil());
    }

    @Override
    public List<T> with(int pos, T value) {
        throw new IllegalArgumentException("with(): position is more than length");
    }

    @Override
    public <U> List<U> map(final Function<T, ? extends U> mapper) {
        return instance();
    }

    @Override
    public <U> U match(final Supplier<? extends U> ifNil, final BiFunction<T, List<T>, ? extends U> ifCons) {
        return ifNil.get();
    }

    @Override
    public <U> U match(
        final Supplier<? extends U> ifNil,
        final Function<T, ? extends U> ifSingle,
        final Function<T, Function<T, Function<List<T>, ? extends U>>> ifMultiple
    ) {
        return ifNil.get();
    }

    @Override
    public List<T> plus(final List<T> other) {
        return other;
    }
}
