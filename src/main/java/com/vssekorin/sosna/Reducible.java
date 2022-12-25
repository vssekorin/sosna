package com.vssekorin.sosna;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Reducible<T> extends Foldable<T> {

    default T reduce(Semigroup<T> semigroup) {
        return reduceLeft(semigroup::combine);
    }

    default T reduceLeft(Function<? super T, Function<? super T, ? extends T>> func) {
        return reduceLeft((a, b) -> func.apply(a).apply(b));
    }

    T reduceLeft(BiFunction<? super T, ? super T, ? extends T> func);

    default T reduceRight(Function<? super T, Function<? super T, ? extends T>> func) {
        return reduceRight((a, b) -> func.apply(a).apply(b));
    }

    T reduceRight(BiFunction<? super T, ? super T, ? extends T> func);
}
