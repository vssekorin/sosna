package com.vssekorin.sosna;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public record Tuple2<A, B>(A _1, B _2) implements Tuple, Bifunctor<A, B>, Map.Entry<A, B>, Ext<Tuple2<A, B>> {

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean contains(Eq<Object> equiv, Object value) {
        return equiv.eq(_1, value) || equiv.eq(_2, value);
    }

    @Override
    public <T> Tuple3<T, A, B> prepend(T value) {
        return new Tuple3<>(value, _1, _2);
    }

    @Override
    public <T> Tuple3<A, B, T> append(T value) {
        return new Tuple3<>(_1, _2, value);
    }

    @Override
    public List<Object> toList() {
        return List.of(_1, _2);
    }

    public Map.Entry<A, B> toEntry() {
        return new AbstractMap.SimpleEntry<>(_1, _2);
    }

    public <T> Tuple2<T, B> with1(T value) {
        return new Tuple2<>(value, _2);
    }

    public <T> Tuple2<A, T> with2(T value) {
        return new Tuple2<>(_1, value);
    }

    public Tuple1<B> drop1() {
        return new Tuple1<>(_2);
    }

    public Tuple1<A> drop2() {
        return new Tuple1<>(_1);
    }

    public <Z> Tuple2<Z, B> map1(Function<? super A, ? extends Z> mapper) {
        return new Tuple2<>(mapper.apply(_1), _2);
    }

    public <Z> Tuple2<A, Z> map2(Function<? super B, ? extends Z> mapper) {
        return new Tuple2<>(_1, mapper.apply(_2));
    }

    public Tuple2<B, A> swap() {
        return new Tuple2<>(_2, _1);
    }

    public <U> U applied(BiFunction<? super A, ? super B, ? extends U> func) {
        return func.apply(_1, _2);
    }

    @Override
    public <U, V> Tuple2<U, V> bimap(Function<A, ? extends U> f, Function<B, ? extends V> g) {
        return new Tuple2<>(f.apply(_1), g.apply(_2));
    }

    @Override
    public A getKey() {
        return _1;
    }

    @Override
    public B getValue() {
        return _2;
    }

    @Override
    public B setValue(B value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equals(_1, tuple2._1()) && Objects.equals(_2, tuple2._2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2);
    }
}
