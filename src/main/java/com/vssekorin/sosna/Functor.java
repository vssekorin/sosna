package com.vssekorin.sosna;

import java.util.function.Function;

@FunctionalInterface
public interface Functor<T> {

    <U> Functor<U> map(Function<T, ? extends U> mapper);
}
