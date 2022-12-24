package com.vssekorin.sosna;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T>, Functor<T>, Serializable {

    private transient volatile Supplier<? extends T> supplier;
    private volatile T value;

    private Lazy() {
    }

    private Lazy(Supplier<? extends T> supplier) {
        this(supplier, null);
    }

    public Lazy(Supplier<? extends T> supplier, T value) {
        this.supplier = supplier;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }
        if (supplier instanceof Lazy) {
            return (Lazy<T>) supplier;
        } else {
            return new Lazy<>(supplier);
        }
    }

    public static <T> Lazy<T> val(T value) {
        return new Lazy<>(null, value);
    }

    @Override
    public <U> Lazy<U> map(Function<T, ? extends U> mapper) {
        return new Lazy<>(() -> mapper.apply(get()));
    }

    @Override
    public T get() {
        return supplier == null ? value : compute();
    }

    private synchronized T compute() {
        if (supplier != null) {
            value = supplier.get();
            supplier = null;
        }
        return value;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Lazy<?> lazy = (Lazy<?>) that;
        return Objects.equals(get(), lazy.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }
}
