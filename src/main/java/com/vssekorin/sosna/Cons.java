package com.vssekorin.sosna;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Cons<T> implements List<T> {

    private final T head;
    private final List<T> tail;

    public Cons(T head, List<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int length() {
        return 1 + tail.length();
    }

    @Override
    public T head() {
        return head;
    }

    @Override
    public List<T> tail() {
        return tail;
    }

    @Override
    public java.util.List<T> asJava() {
        final java.util.List<T> javaList = tail.asJava();
        javaList.add(0, head);
        return javaList;
    }

    @Override
    public boolean contains(final T elem) {
        return elem.equals(head) || tail.contains(elem);
    }

    @Override
    public T getOrNull(final int n) {
        return n == 0 ? head : tail.getOrNull(n - 1);
    }

    @Override
    public T getOr(final int n, final T defaultValue) {
        return n == 0 ? head : tail.getOr(n - 1, defaultValue);
    }

    @Override
    public T getOr(final int n, final Supplier<T> defaultValue) {
        return n == 0 ? head : tail.getOr(n - 1, defaultValue);
    }

    @Override
    public T get(final int n) {
        return n == 0 ? head : tail.get(n - 1);
    }

    @Override
    public List<T> or(final Iterable<? extends T> other) {
        return this;
    }

    @Override
    public List<T> or(final Supplier<? extends Iterable<? extends T>> supplier) {
        return this;
    }

    @Override
    public T last() {
        return tail.isNil() ? head : tail.last();
    }

    @Override
    public List<T> insert(final int pos, final T elem) {
        return pos == 0
            ? new Cons<>(elem, this)
            : new Cons<>(head, tail.insert(pos - 1, elem));
    }

    @Override
    public List<T> insert(final Predicate<T> pred, final T value) {
        return pred.test(head)
            ? new Cons<>(value, this)
            : new Cons<>(head, tail.insert(pred, value));
    }

    @Override
    public List<T> with(int pos, T value) {
        return pos == 0
            ? new Cons<>(value, tail)
            : new Cons<>(head, tail.with(pos - 1, value));
    }

    @Override
    public <U> List<U> map(final Function<T, ? extends U> mapper) {
        return new Cons<>(mapper.apply(head), tail.map(mapper));
    }

    @Override
    public <U> U match(final Supplier<? extends U> ifNil, final BiFunction<T, List<T>, ? extends U> ifCons) {
        return ifCons.apply(head, tail);
    }

    @Override
    public <U> U match(
        final Supplier<? extends U> ifNil,
        final Function<T, ? extends U> ifSingle,
        final Function<T, Function<T, Function<List<T>, ? extends U>>> ifMultiple
    ) {
        if (tail.isEmpty()) {
            return ifSingle.apply(head);
        } else {
            return ifMultiple.apply(head).apply(tail.head()).apply(tail.tail());
        }
    }

    @Override
    public List<T> plus(final List<T> other) {
        return new Cons<>(head, tail.plus(other));
    }
}
