package zad2;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Maybe<T> {
    private final T value;

    private Maybe(T value) {
        this.value = value;
    }

    public static <T> Maybe<T> empty() {
        return new Maybe<>(null);
    }

    public static <T> Maybe<T> of(T val) {
        return val == null ? empty() : new Maybe<>(val);
    }

    public void ifPresent(Consumer<? super T> cons) {
        if (isPresent()) cons.accept(value);
    }

    public <R> Maybe<R> map(Function<? super T, ? extends R> mapper) {
        return isPresent() ? Maybe.of(mapper.apply(value)) : empty();
    }

    public T get() {
        if (!isPresent()) throw new NoSuchElementException("maybe is empty");
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T orElse(T otherVal) {
        return isPresent() ? value : otherVal;
    }

    public Maybe<T> filter(Predicate<? super T> predicate) {
        return isPresent() && predicate.test(value) ? this : empty();
    }

    @Override
    public String toString() {
        return isPresent() ? "Maybe has value " + value : "Maybe is empty";
    }
}
