package com.vssekorin.sosna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public sealed interface List<T>
    extends Serializable, Iterable<T>, Function<Integer, T>
    permits Nil, Cons {

    static <T> List<T> nil() {
        return Nil.instance();
    }

    boolean isEmpty();

    default boolean isNil() {
        return isEmpty();
    }

    default boolean isNonEmpty() {
        return !isEmpty();
    }

    default int size() {
        int size = 0;
        for (List<T> cur = this; cur.isNonEmpty(); cur = cur.tail()) {
            size++;
        }
        return size;
    }

    T head();

    List<T> tail();

    default List<T> prepend(T value) {
        return new Cons<>(value, this);
    }

    List<T> append(T value);

    default java.util.List<T> asJava() {
        final java.util.List<T> javaList = new ArrayList<>();
        for (List<T> cur = this; cur.isNonEmpty(); cur = cur.tail()) {
            javaList.add(cur.head());
        }
        return javaList;
    }

    default boolean contains(T value) {
        for (List<T> cur = this; cur.isNonEmpty(); cur = cur.tail()) {
            if (Objects.equals(cur.head(), value)) {
                return true;
            }
        }
        return false;
    }

    default T get(int n) {
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

    default T getOrNull(int n) {
        return getOr(n, () -> null);
    }

    default T getOr(int n, T defaultValue) {
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

    default T getOr(int n, Supplier<T> defaultValue) {
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
    default Iterator<T> iterator() {
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

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    default T apply(final Integer integer) {
        return getOrNull(integer);
    }

    static <T> List<T> empty() {
        return List.nil();
    }

    static <T> List<T> of(final T elem) {
        return new Cons<>(elem, List.nil());
    }

    @SafeVarargs
    static <T> List<T> of(final T... elems) {
        List<T> result = List.nil();
        for (int i = elems.length - 1; i >= 0; i--) {
            result = new Cons<>(elems[i], result);
        }
        return result;
    }

    static <T> List<T> ofAll(final Iterable<? extends T> elems) {
        List<T> result = nil();
        for (T elem : elems) {
            result = new Cons<>(elem, result);
        }
        return result.reverse();
    }

    List<T> or(final Iterable<? extends T> other);

    List<T> or(final Supplier<? extends Iterable<? extends T>> supplier);

    default List<T> reverse() {
        List<T> origin = this;
        List<T> result = nil();
        while (origin.isNonEmpty()) {
            result = new Cons<>(origin.head(), result);
            origin = origin.tail();
        }
        return result;
    }

    default T last() {
        List<T> cur = this;
        while (cur.tail().isNonEmpty()) {
            cur = cur.tail();
        }
        return cur.head();
    }

    default List<T> insert(final int pos, final T value) {
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
        for (List<T> p = prefix; p.isNonEmpty(); p = p.tail()) {
            result = new Cons<>(p.head(), result);
        }
        return result;
    }

    default List<T> with(final int pos, final T value) {
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
        for (List<T> p = prefix; p.isNonEmpty(); p = p.tail()) {
            result = new Cons<>(p.head(), result);
        }
        return result;
    }

    default <U> List<U> map(final Function<T, ? extends U> mapper) {
        List<U> result = nil();
        for (List<T> cur = reverse(); cur.isNonEmpty(); cur = cur.tail()) {
            result = new Cons<>(mapper.apply(cur.head()), result);
        }
        return result;
    }

    <U> U match(final Supplier<? extends U> ifNil, final BiFunction<T, List<T>, ? extends U> ifCons);

    <U> U match(
        final Supplier<? extends U> ifNil,
        final Function<T, ? extends U> ifSingle,
        final Function<T, Function<T, Function<List<T>, ? extends U>>> ifMultiple
    );

    default List<T> plus(final List<T> other) {
        List<T> result = other;
        for (List<T> cur = reverse(); cur.isNonEmpty(); cur = cur.tail()) {
            result = new Cons<>(cur.head(), result);
        }
        return result;
    }
}
