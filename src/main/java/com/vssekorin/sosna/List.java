package com.vssekorin.sosna;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
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

    int length();

    T head();

    List<T> tail();

    java.util.List<T> asJava();

    boolean contains(final T elem);

    default T getOrNull(int n) {
        if (n < 0) return null;
        List<T> list = this;
        for (int i = 0; i < n && list.isNonEmpty(); i++) {
            list = list.tail();
        }
        return list.isNil() ? null : list.head();
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
}
