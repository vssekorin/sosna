package com.vssekorin.sosna;

import java.util.Objects;

@FunctionalInterface
public interface Eq<T> {

    boolean eq(T x, T y);

    default boolean neq(T x, T y) {
        return !eq(x, y);
    }

    Eq<?> refEq = (x, y) -> x == y;

    @SuppressWarnings("unchecked")
    static <T> Eq<T> refEq() {
        return (Eq<T>) refEq;
    }

    Eq<?> objectEq = Objects::equals;

    @SuppressWarnings("unchecked")
    static <T> Eq<T> objectEq() {
        return (Eq<T>) objectEq;
    }
}
