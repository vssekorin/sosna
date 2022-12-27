package com.vssekorin.sosna;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public sealed abstract class List<T> implements FiniteSeq<T> permits Nil, Cons {

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
    public boolean contains(T value) {
        for (List<T> cur = this; cur.nonEmpty(); cur = cur.tail()) {
            if (Objects.equals(cur.head(), value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        final List<T> that = this;
        return new Iterator<>() {
            private List<T> list = that;

            @Override
            public boolean hasNext() {
                return !list.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                final T result = list.head();
                list = list.tail();
                return result;
            }
        };
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
        for (List<T> cur = reverse(); cur.nonEmpty(); cur = cur.tail()) {
            result = new Cons<>(mapper.apply(cur.head()), result);
        }
        return result;
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
    public List<T> filter(Predicate<? super T> predicate) {
        return filterIndexed((__, v) -> predicate.test(v));
    }

    @Override
    public List<T> filterNot(Predicate<? super T> predicate) {
        return filter(predicate.negate());
    }

    @Override
    public Seq<T> filterNonNull() {
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
