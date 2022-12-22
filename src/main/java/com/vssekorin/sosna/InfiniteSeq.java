package com.vssekorin.sosna;

public interface InfiniteSeq<T> extends Seq<T> {

    @Override
    default boolean isEmpty() {
        return false;
    }
}
