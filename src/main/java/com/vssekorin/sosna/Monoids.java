package com.vssekorin.sosna;

public final class Monoids {

    private Monoids() {
    }

    public static final Monoid<Integer> IntSum = new Monoid<>() {
        @Override
        public Integer empty() {
            return 0;
        }

        @Override
        public Integer combine(Integer x, Integer y) {
            return x + y;
        }
    };

    public static final Monoid<Integer> IntProd = new Monoid<>() {
        @Override
        public Integer empty() {
            return 1;
        }

        @Override
        public Integer combine(Integer x, Integer y) {
            return x * y;
        }
    };
}
