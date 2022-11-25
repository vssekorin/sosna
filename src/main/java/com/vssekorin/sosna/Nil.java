package com.vssekorin.sosna;

import java.util.ArrayList;
import java.util.NoSuchElementException;

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
    public T get(int n) {
        throw new IllegalArgumentException("get(): n is more than length");
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
    public List<T> with(int pos, T value) {
        throw new IllegalArgumentException("with(): position is more than length");
    }
}
