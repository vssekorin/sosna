package com.vssekorin.sosna;

import java.util.Objects;

public final class Eqs {

    private Eqs() {
    }

    public static Eq<?> RefEq = (x, y) -> x == y;

    @SuppressWarnings("unchecked")
    public static <T> Eq<T> refEq() {
        return (Eq<T>) RefEq;
    }

    public static Eq<?> ObjectEq = Objects::equals;

    @SuppressWarnings("unchecked")
    public static <T> Eq<T> objectEq() {
        return (Eq<T>) ObjectEq;
    }
}
