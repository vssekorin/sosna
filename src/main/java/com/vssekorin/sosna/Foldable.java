package com.vssekorin.sosna;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Foldable<T> {

    <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> func);

    default <U> U foldLeft(U zero, Function<? super U, Function<? super T, ? extends U>> func) {
        return foldLeft(zero, (a, b) -> func.apply(a).apply(b));
    }

    <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> func);

    default <U> U foldRight(U zero, Function<? super T, Function<? super U, ? extends U>> func) {
        return foldRight(zero, (a, b) -> func.apply(a).apply(b));
    }
}
