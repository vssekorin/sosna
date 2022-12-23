package com.vssekorin.sosna;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Cons<T> extends List<T> {

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
    public Optional<T> headOpt() {
        return Optional.of(head);
    }

    @Override
    public List<T> tail() {
        return tail;
    }

    @Override
    public List<T> append(T value) {
        List<T> list = new Cons<>(value, List.nil());
        for (List<T> cur = reverse(); cur.nonEmpty(); cur = cur.tail()) {
            list = new Cons<>(cur.head(), list);
        }
        return list;
    }

    @Override
    public List<T> appendAll(Iterable<? extends T> values) {
        return List.<T>ofAll(values).prependAll(this);
    }

    @Override
    public T get(int n) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("get(): n is negative");
        }
        List<T> cur = this;
        for (int i = 0; i < n; i++) {
            cur = cur.tail();
            if (cur.isNil()) {
                throw new IndexOutOfBoundsException("get(): n is more than length");
            }
        }
        return cur.head();
    }

    @Override
    public Optional<T> getOpt(int n) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("get(): n is negative");
        }
        List<T> cur = this;
        for (int i = 0; i < n; i++) {
            cur = cur.tail();
            if (cur.isNil()) {
                return Optional.empty();
            }
        }
        return Optional.ofNullable(cur.head());
    }

    @Override
    public T getOr(int n, T defaultValue) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("get(): n is negative");
        }
        List<T> cur = this;
        for (int i = 0; i < n; i++) {
            cur = cur.tail();
            if (cur.isNil()) {
                return defaultValue;
            }
        }
        return cur.head();
    }

    @Override
    public T getOrGet(int n, Supplier<T> defaultValue) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("get(): n is negative");
        }
        List<T> cur = this;
        for (int i = 0; i < n; i++) {
            cur = cur.tail();
            if (cur.isNil()) {
                return defaultValue.get();
            }
        }
        return cur.head();
    }

    @Override
    public List<T> or(Iterable<? extends T> other) {
        return this;
    }

    @Override
    public List<T> or(Supplier<? extends Iterable<? extends T>> supplier) {
        return this;
    }

    @Override
    public T last() {
        List<T> cur = this;
        while (cur.tail().nonEmpty()) {
            cur = cur.tail();
        }
        return cur.head();
    }

    @Override
    public Optional<T> lastOpt() {
        return Optional.of(last());
    }

    @Override
    public <U> U match(Supplier<? extends U> ifNil, BiFunction<T, Seq<T>, ? extends U> ifCons) {
        return ifCons.apply(head, tail);
    }

    @Override
    public <U> U match(
        Supplier<? extends U> ifNil,
        Function<T, ? extends U> ifSingle,
        Function<T, Function<T, Function<Seq<T>, ? extends U>>> ifMultiple
    ) {
        if (tail.isEmpty()) {
            return ifSingle.apply(head);
        } else {
            return ifMultiple.apply(head).apply(tail.head()).apply(tail.tail());
        }
    }
}
