package com.vssekorin.sosna;

import java.util.Optional;

public final class Tuple0 implements Tuple, Ext<Tuple0> {

    private static final Tuple0 INSTANCE = new Tuple0();

    private Tuple0() {
    }

    static Tuple0 instance() {
        return INSTANCE;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(Eq<Object> equiv, Object value) {
        return false;
    }

    @Override
    public <T> Tuple1<T> prepend(T value) {
        return Tuple.of(value);
    }

    @Override
    public <T> Tuple1<T> append(T value) {
        return Tuple.of(value);
    }

    @Override
    public List<Object> toList() {
        return List.empty();
    }

    public <T> Optional<T> toOpt() {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
