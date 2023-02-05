package com.vssekorin.sosna;

import java.util.Objects;
import java.util.function.Function;

public record Tuple3<A, B, C>(A _1, B _2, C _3) implements Tuple, Ext<Tuple3<A, B, C>> {

    @Override
    public int size() {
        return 3;
    }

    @Override
    public boolean contains(Eq<Object> equiv, Object value) {
        return equiv.eq(_1, value) || equiv.eq(_2, value) || equiv.eq(_3, value);
    }

    @Override
    public <T> Tuple prepend(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Tuple append(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object> toList() {
        return List.of(_1, _2, _3);
    }

    public <T> Tuple3<T, B, C> with1(T value) {
        return new Tuple3<>(value, _2, _3);
    }

    public <T> Tuple3<A, T, C> with2(T value) {
        return new Tuple3<>(_1, value, _3);
    }

    public <T> Tuple3<A, B, T> with3(T value) {
        return new Tuple3<>(_1, _2, value);
    }

    public Tuple2<B, C> drop1() {
        return new Tuple2<>(_2, _3);
    }

    public Tuple2<A, C> drop2() {
        return new Tuple2<>(_1, _3);
    }

    public Tuple2<A, B> drop3() {
        return new Tuple2<>(_1, _2);
    }

    public <Z> Tuple3<Z, B, C> map1(Function<? super A, ? extends Z> mapper) {
        return new Tuple3<>(mapper.apply(_1), _2, _3);
    }

    public <Z> Tuple3<A, Z, C> map2(Function<? super B, ? extends Z> mapper) {
        return new Tuple3<>(_1, mapper.apply(_2), _3);
    }

    public <Z> Tuple3<A, B, Z> map3(Function<? super C, ? extends Z> mapper) {
        return new Tuple3<>(_1, _2, mapper.apply(_3));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
        return Objects.equals(_1, tuple3._1()) && Objects.equals(_2, tuple3._2()) && Objects.equals(_3, tuple3._3());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3);
    }
}
