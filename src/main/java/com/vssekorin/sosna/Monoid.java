package com.vssekorin.sosna;

public interface Monoid<T> extends Semigroup<T> {

    T empty();

    abstract class Int {
        public static final Monoid<Integer> sum = new Monoid<>() {
            @Override public Integer empty() { return 0; }
            @Override public Integer combine(Integer x, Integer y) { return x + y; }
        };

        public static final Monoid<Integer> prod = new Monoid<>() {
            @Override public Integer empty() { return 1; }
            @Override public Integer combine(Integer x, Integer y) { return x * y; }
        };
    }

    abstract class Str {
        public static final Monoid<String> concat = new Monoid<>() {
            @Override public String empty() { return ""; }
            @Override public String combine(String x, String y) { return x + y; }
        };
    }

    abstract class Bool {
        public static final Monoid<Boolean> and = new Monoid<>() {
            @Override public Boolean empty() { return true; }
            @Override public Boolean combine(Boolean x, Boolean y) { return x && y; }
        };

        public static final Monoid<Boolean> or = new Monoid<>() {
            @Override public Boolean empty() { return false; }
            @Override public Boolean combine(Boolean x, Boolean y) { return x || y; }
        };
    }
}
