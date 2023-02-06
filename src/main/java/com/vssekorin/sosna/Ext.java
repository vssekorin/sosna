package com.vssekorin.sosna;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Ext<T> {

    @SuppressWarnings("unchecked")
    default void run(Consumer<? super T> consumer) {
        consumer.accept((T) this);
    }

    @SuppressWarnings("unchecked")
    default void runIf(Predicate<? super T> cond, Consumer<? super T> consumer) {
        if (cond.test((T) this)) {
            consumer.accept((T) this);
        }
    }

    @SuppressWarnings("unchecked")
    default <E> E let(Function<? super T, ? extends E> f) {
        return f.apply((T) this);
    }

    @SuppressWarnings("unchecked")
    default T letIf(Predicate<? super T> cond, Function<? super T, ? extends T> f) {
        return cond.test((T) this) ? f.apply((T) this) : (T) this;
    }

    @SuppressWarnings("unchecked")
    default T also(Consumer<? super T> consumer) {
        consumer.accept((T) this);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T alsoIf(Predicate<? super T> cond, Consumer<? super T> consumer) {
        if (cond.test((T) this)) {
            consumer.accept((T) this);
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T takeIf(Predicate<? super T> predicate) {
        return predicate.test((T) this) ? (T) this : null;
    }

    @SuppressWarnings("unchecked")
    default T takeUnless(Predicate<? super T> predicate) {
        return !predicate.test((T) this) ? (T) this : null;
    }

    @SuppressWarnings("unchecked")
    default <E> Tuple2<T, E> to(E that) {
        return Tuple.of((T) this, that);
    }

    @SuppressWarnings("unchecked")
    default Optional<T> some() {
        return Optional.of((T) this);
    }

    @SuppressWarnings("unchecked")
    default <L> Either<L, T> asRight() {
        return Either.right((T) this);
    }

    @SuppressWarnings("unchecked")
    default <R> Either<T, R> asLeft() {
        return Either.left((T) this);
    }
}
