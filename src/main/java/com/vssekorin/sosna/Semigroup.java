package com.vssekorin.sosna;

@FunctionalInterface
public interface Semigroup<T> {

    T combine(T x, T y);

    abstract class Int {
        public static final Semigroup<Integer> sum = Integer::sum;
        public static final Semigroup<Integer> prod = (x, y) -> x * y;
    }

    abstract class Str {
        public static final Semigroup<String> concat = (x, y) -> x + y;
    }

    abstract class Bool {
        public static final Semigroup<Boolean> and = (x, y) -> x && y;
        public static final Semigroup<Boolean> or = (x, y) -> x || y;
    }
}
