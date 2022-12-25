package com.vssekorin.sosna;

public interface Monoid<T> extends Semigroup<T> {

    T empty();
}
