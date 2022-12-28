package com.vssekorin.sosna;

import java.util.function.Function;

@FunctionalInterface
public interface Bifunctor<T, E> {

    <U, V> Bifunctor<U, V> bimap(Function<T, ? extends U> f, Function<E, ? extends V> g);
}
