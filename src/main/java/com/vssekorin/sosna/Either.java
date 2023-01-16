package com.vssekorin.sosna;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Either<L, R> implements Functor<R>, Bifunctor<L, R>, Supplier<R> {

    private Either() {
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    public static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    public static <L, R> Either<L, R> cond(boolean test, R right, L left) {
        return test ? new Right<>(right) : new Left<>(left);
    }

    public static <L, R> Either<L, R> cond(boolean test, Supplier<R> right, Supplier<L> left) {
        return test ? new Right<>(right.get()) : new Left<>(left.get());
    }

    public static <L, R> Either<L, R> ofNullable(R value) {
        return value != null ? new Right<>(value) : new Left<>(null);
    }

    public static <L, R> Either<L, R> ofNullable(R value, L ifNull) {
        return value != null ? new Right<>(value) : new Left<>(ifNull);
    }

    public static <L, R> Either<L, R> ofNullable(R value, Supplier<? extends L> ifNull) {
        return value != null ? new Right<>(value) : new Left<>(ifNull.get());
    }

    public static <R> Either<Throwable, R> caught(Supplier<? extends R> value) {
        try {
            return new Right<>(value.get());
        } catch (Throwable thr) {
            if (thr instanceof Error) {
                throw thr;
            } else {
                return new Left<>(thr);
            }
        }
    }

    public boolean isLeft() {
        return !isRight();
    }

    public abstract boolean isRight();

    public abstract L left();

    public abstract R right();

    @Override
    public R get() {
        return right();
    }

    public R getOrNull() {
        return getOr(null);
    }

    public abstract <R1 extends R> R getOr(R1 defaultValue);

    public abstract R getOrGet(int n, Supplier<? extends R> defaultValue);

    public abstract Optional<R> toOpt();

    public abstract List<R> toList();

    public void run(Consumer<? super R> consumer) {
        if (isRight()) {
            consumer.accept(right());
        }
    }

    public abstract Either<R, L> swap();

    public abstract <U> Either<U, R> mapLeft(Function<L, ? extends U> mapper);

    public boolean all(Predicate<R> predicate) {
        return isLeft() || predicate.test(get());
    }

    public boolean any(Predicate<R> predicate) {
        return !isLeft() && predicate.test(get());
    }

    @SuppressWarnings("unchecked")
    public <U> Either<L, U> mapRight(Function<R, ? extends U> mapper) {
        return (Either<L, U>) map(mapper);
    }

    public abstract <U> U fold(Function<? super L, ? extends U> fl, Function<? super R, ? extends U> fr);

    public abstract <Z> Either<L, Tuple2<R, Z>> zip(Z value);

    private static final class Left<L, R> extends Either<L, R> implements Serializable {

        private final L value;

        private Left(L value) {
            this.value = value;
        }

        @Override
        public R right() {
            throw new NoSuchElementException("get() of Left");
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Either<L, U> map(Function<R, ? extends U> mapper) {
            return (Either<L, U>) this;
        }

        @Override
        public <U, V> Either<U, V> bimap(Function<L, ? extends U> f, Function<R, ? extends V> g) {
            return new Left<>(f.apply(value));
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public L left() {
            return value;
        }

        @Override
        public <R1 extends R> R getOr(R1 defaultValue) {
            return defaultValue;
        }

        @Override
        public R getOrGet(int n, Supplier<? extends R> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public Optional<R> toOpt() {
            return Optional.empty();
        }

        @Override
        public List<R> toList() {
            return List.empty();
        }

        @Override
        public Either<R, L> swap() {
            return new Right<>(value);
        }

        @Override
        public <U> Either<U, R> mapLeft(Function<L, ? extends U> f) {
            return new Left<>(f.apply(value));
        }

        @Override
        public <U> U fold(Function<? super L, ? extends U> fl, Function<? super R, ? extends U> fr) {
            return fl.apply(value);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <Z> Either<L, Tuple2<R, Z>> zip(Z value) {
            return (Either<L, Tuple2<R, Z>>) this;
        }
    }

    private static final class Right<L, R> extends Either<L, R> implements Serializable {

        private final R value;

        private Right(R value) {
            this.value = value;
        }

        @Override
        public R right() {
            return value;
        }

        @Override
        public <U> Either<L, U> map(Function<R, ? extends U> mapper) {
            return new Right<>(mapper.apply(value));
        }

        @Override
        public <U, V> Either<U, V> bimap(Function<L, ? extends U> f, Function<R, ? extends V> g) {
            return new Right<>(g.apply(value));
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public L left() {
            throw new NoSuchElementException("getLeft() of Right");
        }

        @Override
        public <R1 extends R> R getOr(R1 defaultValue) {
            return value;
        }

        @Override
        public R getOrGet(int n, Supplier<? extends R> defaultValue) {
            return value;
        }

        @Override
        public Optional<R> toOpt() {
            return Optional.ofNullable(value);
        }

        @Override
        public List<R> toList() {
            return List.of(value);
        }

        @Override
        public Either<R, L> swap() {
            return new Left<>(value);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Either<U, R> mapLeft(Function<L, ? extends U> mapper) {
            return (Either<U, R>) this;
        }

        @Override
        public <U> U fold(Function<? super L, ? extends U> fl, Function<? super R, ? extends U> fr) {
            return fr.apply(value);
        }

        @Override
        public <Z> Either<L, Tuple2<R, Z>> zip(Z z) {
            return new Right<>(Tuple.of(value, z));
        }
    }
}
