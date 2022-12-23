package com.vssekorin.sosna;

import java.util.Optional;
import java.util.function.Predicate;

public interface FiniteSeq<T> extends Seq<T> {

    int size();

    FiniteSeq<T> append(T value);

    FiniteSeq<T> appendAll(Iterable<? extends T> values);

    FiniteSeq<T> reverse();

    T last();

    Optional<T> lastOpt();

    FiniteSeq<T> takeRight(int n);

    FiniteSeq<T> takeRightWhile(Predicate<T> cond);
}
