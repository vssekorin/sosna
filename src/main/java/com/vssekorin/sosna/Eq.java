package com.vssekorin.sosna;

@FunctionalInterface
public interface Eq<T> {

    boolean eq(T x, T y);

    default boolean neq(T x, T y) {
        return !eq(x, y);
    }
}
