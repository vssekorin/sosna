package com.vssekorin.sosna;

import java.util.function.BiFunction;
import java.util.function.Function;
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
    public T head() {
        return head;
    }

    @Override
    public List<T> tail() {
        return tail;
    }

    @Override
    public List<T> append(T value) {
        List<T> list = new Cons<>(value, List.nil());
        for (List<T> cur = reverse(); cur.isNonEmpty(); cur = cur.tail()) {
            list = new Cons<>(cur.head(), list);
        }
        return list;
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
}
