package com.vssekorin.sosna;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public sealed abstract class List<T> implements Seq<T>, Ext<List<T>> permits Nil, Cons {

    static <T> List<T> nil() {
        return Nil.instance();
    }

    public boolean isNil() {
        return isEmpty();
    }

    @Override
    public int size() {
        int size = 0;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            size++;
        }
        return size;
    }

    @Override
    public abstract T head();

    @Override
    public abstract List<T> tail();

    @Override
    public abstract Tuple2<T, List<T>> uncons();

    @Override
    public java.util.List<T> asJava() {
        final java.util.List<T> javaList = new ArrayList<>();
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            javaList.add(cur.head());
        }
        return javaList;
    }

    @Override
    public List<T> prepend(T value) {
        return new Cons<>(value, this);
    }

    @Override
    public List<T> prependAll(Iterable<? extends T> values) {
        List<T> result = this;
        for (T value : List.ofAll(values).reverse()) {
            result = result.prepend(value);
        }
        return result;
    }

    @Override
    public abstract List<T> append(T value);

    @Override
    public abstract List<T> appendAll(Iterable<? extends T> values);

    @Override
    public boolean contains(Eq<T> equiv, T value) {
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            if (equiv.eq(cur.head(), value)) {
                return true;
            }
        }
        return false;
    }

    static <T> List<T> empty() {
        return List.nil();
    }

    static <T> List<T> of(T value) {
        return new Cons<>(value, List.nil());
    }

    @SafeVarargs
    static <T> List<T> of(T... values) {
        List<T> result = List.nil();
        for (int i = values.length - 1; i >= 0; i--) {
            result = new Cons<>(values[i], result);
        }
        return result;
    }

    static <T> List<T> ofAll(Iterable<? extends T> values) {
        List<T> result = nil();
        for (T value : values) {
            result = new Cons<>(value, result);
        }
        return result.reverse();
    }

    static <T> List<T> replicate(int n, T value) {
        List<T> result = nil();
        for (int i = 0; i < n; i++) {
            result = new Cons<>(value, result);
        }
        return result;
    }

    @Override
    public List<T> reverse() {
        List<T> origin = this;
        List<T> result = nil();
        while (origin.nonEmpty()) {
            result = new Cons<>(origin.head(), result);
            origin = origin.tail();
        }
        return result;
    }

    @Override
    public List<T> insert(int pos, T value) {
        List<T> result;
        List<T> prefix = nil();
        List<T> cur = this;
        for (int i = 0; i < pos; i++, cur = cur.tail()) {
            if (cur.isNil()) {
                throw new IndexOutOfBoundsException("insert() with pos = " + pos);
            }
            prefix = new Cons<>(cur.head(), prefix);
        }
        result = new Cons<>(value, cur);
        for (List<T> p = prefix; p.nonEmpty(); p = p.tail()) {
            result = new Cons<>(p.head(), result);
        }
        return result;
    }

    @Override
    public List<T> with(int pos, T value) {
        List<T> result;
        List<T> prefix = nil();
        List<T> cur = this;
        for (int i = 0; i < pos; i++, cur = cur.tail()) {
            if (cur.isNil()) {
                throw new IndexOutOfBoundsException("with() with pos = " + pos);
            }
            prefix = new Cons<>(cur.head(), prefix);
        }
        result = new Cons<>(value, cur.tail());
        for (List<T> p = prefix; p.nonEmpty(); p = p.tail()) {
            result = new Cons<>(p.head(), result);
        }
        return result;
    }

    @Override
    public <U> List<U> map(Function<T, ? extends U> mapper) {
        List<U> result = nil();
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            result = new Cons<>(mapper.apply(cur.head()), result);
        }
        return result.reverse();
    }

    @Override
    public <U> List<U> mapIndexed(BiFunction<Integer, T, ? extends U> mapper) {
        List<U> result = nil();
        int i = 0;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail(), i++) {
            result = new Cons<>(mapper.apply(i, cur.head()), result);
        }
        return result.reverse();
    }

    @Override
    public List<T> mapIf(Predicate<? super T> condition, Function<? super T, ? extends T> f) {
        List<T> result = nil();
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            final T value = condition.test(cur.head()) ? f.apply(cur.head()) : cur.head();
            result = new Cons<>(value, result);
        }
        return result.reverse();
    }

    @Override
    public <U> List<U> mapIf(
        Predicate<? super T> condition,
        Function<? super T, ? extends U> thenF,
        Function<? super T, ? extends U> elseF
    ) {
        List<U> result = nil();
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            final U value = condition.test(cur.head()) ? thenF.apply(cur.head()) : elseF.apply(cur.head());
            result = new Cons<>(value, result);
        }
        return result.reverse();
    }

    @Override
    public List<T> mapIfFirst(Predicate<? super T> condition, Function<? super T, ? extends T> f) {
        boolean should = true;
        List<T> result = nil();
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            if (should && condition.test(cur.head())) {
                result = new Cons<>(f.apply(cur.head()), result);
                should = false;
            } else {
                result = new Cons<>(cur.head(), result);
            }
        }
        return result.reverse();
    }

    public <U> List<U> mapNotNull(Function<T, ? extends U> f) {
        List<U> result = nil();
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            U value = f.apply(cur.head());
            if (value != null) {
                result = new Cons<>(value, result);
            }
        }
        return result.reverse();
    }

    @Override
    public <U> List<U> mapIndexedNotNull(BiFunction<Integer, T, ? extends U> f) {
        List<U> result = nil();
        int i = 0;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail(), i++) {
            U value = f.apply(i, cur.head());
            if (value != null) {
                result = new Cons<>(value, result);
            }
        }
        return result.reverse();
    }

    @Override
    public List<T> filter(Predicate<? super T> predicate) {
        return filterIndexed((__, v) -> predicate.test(v));
    }

    @Override
    public List<T> filterNot(Predicate<? super T> predicate) {
        return filter(predicate.negate());
    }

    @Override
    public List<T> filterNonNull() {
        return filter(Objects::nonNull);
    }

    @Override
    public List<T> filterIndexed(BiPredicate<Integer, T> predicate) {
        List<T> result = this;
        List<T> left = nil();
        int i = 0;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail(), i++) {
            if (!predicate.test(i, cur.head())) {
                while (result != cur) {
                    left = left.prepend(result.head());
                    result = result.tail();
                }
                result = result.tail();
            }
        }
        if (result == this) {
            return this;
        } else {
            for (List<T> cur = left; cur.nonEmpty(); cur = cur.tail()) {
                result = new Cons<>(cur.head(), result);
            }
            return result;
        }
    }

    @Override
    public boolean all(Predicate<T> predicate) {
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            if (!predicate.test(cur.head())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean any(Predicate<T> predicate) {
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            if (predicate.test(cur.head())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int count(Predicate<T> predicate) {
        int count = 0;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            if (predicate.test(cur.head())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<T> take(int n) {
        List<T> result = nil();
        int i = 0;
        for (List<T> cur = this; i < n && cur.nonEmpty(); i++, cur = cur.tail()) {
            result = new Cons<>(cur.head(), result);
        }
        return result.reverse();
    }

    @Override
    public List<T> takeWhile(Predicate<T> cond) {
        List<T> result = nil();
        for (List<T> cur = this; cur.nonEmpty() && cond.test(cur.head()); cur = cur.tail()) {
            result = new Cons<>(cur.head(), result);
        }
        return result.reverse();
    }

    @Override
    public List<T> takeRight(int n) {
        List<T> result = this;
        List<T> cur = this;
        for (int i = 0; i < n && cur.nonEmpty(); i++) {
            cur = cur.tail();
        }
        while (cur.nonEmpty()) {
            result = result.tail();
            cur = cur.tail();
        }
        return result;
    }

    @Override
    public List<T> takeRightWhile(Predicate<T> cond) {
        List<T> start = nil();
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            if (cond.test(cur.head())) {
                if (start.isNil()) {
                    start = cur;
                }
            } else {
                start = nil();
            }
        }
        return start;
    }

    @Override
    public <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> func) {
        U result = zero;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            result = func.apply(result, cur.head());
        }
        return result;
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> func) {
        return reverse().foldLeft(zero, (acc, v) -> func.apply(v, acc));
    }

    @Override
    public <U> List<Tuple2<T, U>> zip(Iterable<? extends U> that) {
        List<Tuple2<T, U>> result = nil();
        Iterator<? extends U> iterator = that.iterator();
        for (List<T> cur = this; cur.nonEmpty() && iterator.hasNext(); cur = cur.tail()) {
            result = new Cons<>(new Tuple2<>(cur.head(), iterator.next()), result);
        }
        return result.reverse();
    }

    @Override
    public List<Tuple2<T, Integer>> zipWithIndex() {
        List<Tuple2<T, Integer>> result = nil();
        int i = 0;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail(), i++) {
            result = new Cons<>(new Tuple2<>(cur.head(), i), result);
        }
        return result.reverse();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        List<?> list = (List<?>) that;
        List<T> cur = this;
        for (; cur.nonEmpty() && list.nonEmpty(); cur = cur.tail(), list = list.tail()) {
            if (!Objects.equals(cur.head(), list.head())) {
                return false;
            }
        }
        return cur.isNil() && list.isNil();
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            hashCode += 31 * hashCode + (cur.head() == null ? 0 : Objects.hashCode(cur.head()));
        }
        return hashCode;
    }
}
