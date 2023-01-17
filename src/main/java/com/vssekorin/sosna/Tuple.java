package com.vssekorin.sosna;

import java.io.Serializable;
import java.util.Iterator;

public sealed interface Tuple extends Serializable, Iterable<Object>
    permits Tuple0, Tuple1, Tuple2 {

    int size();

    default boolean contains(Object obj) {
        return contains(Object::equals, obj);
    }

    boolean contains(Eq<Object> equiv, Object value);

    <T> Tuple prepend(T value);

    <T> Tuple append(T value);

    List<Object> toList();

    @Override
    default Iterator<Object> iterator() {
        return toList().iterator();
    }

    default void orRun(Runnable runnable) {
        if (size() == 0) {
            runnable.run();
        }
    }

    static Tuple0 empty() {
        return Tuple0.instance();
    }

    static <A> Tuple1<A> of(A value) {
        return new Tuple1<>(value);
    }

    static <A, B> Tuple2<A, B> of(A v1, B v2) {
        return new Tuple2<>(v1, v2);
    }
}
