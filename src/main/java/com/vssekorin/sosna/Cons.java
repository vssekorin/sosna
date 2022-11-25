package com.vssekorin.sosna;

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
    public T get(int n) {
        return n == 0 ? head : tail.get(n - 1);
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
    public List<T> with(int pos, T value) {
        return pos == 0
            ? new Cons<>(value, tail)
            : new Cons<>(head, tail.with(pos - 1, value));
    }
}
