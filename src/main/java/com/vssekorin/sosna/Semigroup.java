package com.vssekorin.sosna;

@FunctionalInterface
public interface Semigroup<T> {

    T combine(T x, T y);
}
