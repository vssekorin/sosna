package com.vssekorin.sosna;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public record Tuple1<A>(A _1) implements Tuple, Functor<A> {

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean contains(Eq<Object> equiv, Object value) {
        return equiv.eq(_1, value);
    }

    @Override
    public <T> Tuple2<T, A> prepend(T value) {
        return new Tuple2<>(value, _1);
    }

    @Override
    public <T> Tuple2<A, T> append(T value) {
        return new Tuple2<>(_1, value);
    }

    @Override
    public List<Object> toList() {
        return null;
    }

    public Optional<A> toOpt() {
        return Optional.ofNullable(_1);
    }

    public Tuple1<A> with1(A value) {
        return new Tuple1<>(value);
    }

    public Tuple0 drop1() {
        return Tuple0.instance();
    }

    public <Z> Tuple1<Z> map1(Function<? super A, ? extends Z> mapper) {
        return new Tuple1<>(mapper.apply(_1));
    }

    @Override
    public <U> Tuple1<U> map(Function<A, ? extends U> mapper) {
        return map1(mapper);
    }

    public <Z> Z applied(Function<? super A, ? extends Z> func) {
        return func.apply(_1);
    }

    public <Z, R> Function<R, Z> applied(BiFunction<? super A, ? super R, ? extends Z> func) {
        return v -> func.apply(_1, v);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tuple1<?> tuple1 = (Tuple1<?>) obj;
        return Objects.equals(_1, tuple1._1());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1);
    }
}
