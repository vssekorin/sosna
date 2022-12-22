package com.vssekorin.sosna;

import java.util.function.Function;

public interface Functor<T> {

    <U> Functor<U> map(Function<T, ? extends U> mapper);
}
