package com.vssekorin.sosna;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public sealed abstract class List<T> implements Seq<T> permits Nil, Cons {

    static <T> List<T> nil() {
        return Nil.instance();
    }

    public boolean isNil() {
        return isEmpty();
    }

    @Override
    public int size() {
        int size = 0;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            size++;
        }
        return size;
    }

    @Override
    public abstract T head();

    @Override
    public abstract List<T> tail();

    @Override
    public java.util.List<T> asJava() {
        final java.util.List<T> javaList = new ArrayList<>();
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            javaList.add(cur.head());
        }
        return javaList;
    }

    @Override
    public List<T> prepend(T value) {
        return new Cons<>(value, this);
    }

    @Override
    public abstract List<T> append(T value);

    @Override
    public boolean contains(T value) {
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            if (Objects.equals(cur.head(), value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        final List<T> that = this;
        return new Iterator<>() {
            private List<T> list = that;

            @Override
            public boolean hasNext() {
                return !list.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                final T result = list.head();
                list = list.tail();
                return result;
            }
        };
    }

    static <T> List<T> empty() {
        return List.nil();
    }

    static <T> List<T> of(T value) {
        return new Cons<>(value, List.nil());
    }

    @SafeVarargs
    static <T> List<T> of(T... values) {
        List<T> result = List.nil();
        for (int i = values.length - 1; i >= 0; i--) {
            result = new Cons<>(values[i], result);
        }
        return result;
    }

    static <T> List<T> ofAll(Iterable<? extends T> values) {
        List<T> result = nil();
        for (T value : values) {
            result = new Cons<>(value, result);
        }
        return result.reverse();
    }

    @Override
    public List<T> reverse() {
        List<T> origin = this;
        List<T> result = nil();
        while (origin.nonEmpty()) {
            result = new Cons<>(origin.head(), result);
            origin = origin.tail();
        }
        return result;
    }

    @Override
    public List<T> insert(int pos, T value) {
        List<T> result;
        List<T> prefix = nil();
        List<T> cur = this;
        for (int i = 0; i < pos; i++, cur = cur.tail()) {
            if (cur.isNil()) {
                throw new IndexOutOfBoundsException("insert() with pos = " + pos);
            }
            prefix = new Cons<>(cur.head(), prefix);
        }
        result = new Cons<>(value, cur);
        for (List<T> p = prefix; p.nonEmpty(); p = p.tail()) {
            result = new Cons<>(p.head(), result);
        }
        return result;
    }

    @Override
    public List<T> with(int pos, T value) {
        List<T> result;
        List<T> prefix = nil();
        List<T> cur = this;
        for (int i = 0; i < pos; i++, cur = cur.tail()) {
            if (cur.isNil()) {
                throw new IndexOutOfBoundsException("with() with pos = " + pos);
            }
            prefix = new Cons<>(cur.head(), prefix);
        }
        result = new Cons<>(value, cur.tail());
        for (List<T> p = prefix; p.nonEmpty(); p = p.tail()) {
            result = new Cons<>(p.head(), result);
        }
        return result;
    }

    @Override
    public <U> List<U> map(Function<T, ? extends U> mapper) {
        List<U> result = nil();
        for (List<T> cur = reverse(); cur.nonEmpty(); cur = cur.tail()) {
            result = new Cons<>(mapper.apply(cur.head()), result);
        }
        return result;
    }

    @Override
    public <U> List<U> mapIndexed(BiFunction<Integer, T, ? extends U> mapper) {
        List<U> result = nil();
        int i = 0;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail(), i++) {
            result = new Cons<>(mapper.apply(i, cur.head()), result);
        }
        return result.reverse();
    }

    @Override
    public List<T> plus(final List<T> other) {
        List<T> result = other;
        for (List<T> cur = reverse(); cur.nonEmpty(); cur = cur.tail()) {
            result = new Cons<>(cur.head(), result);
        }
        return result;
    }
}
