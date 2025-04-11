package zad1;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XList<T> extends ArrayList<T> {
    public XList() {
        super();
    }

    public XList(Collection<? extends T> collection) {
        super(collection);
    }

    @SafeVarargs
    public XList(T... elements) {
        super(Arrays.asList(elements));
    }

    @SafeVarargs
    public static <T> XList<T> of(T... elements) {
        return new XList<>(elements);
    }

    public static <T> XList<T> of(Collection<? extends T> collection) {
        return new XList<>(collection);
    }

    public static XList<String> charsOf(String text) {
        return new XList<>(text.chars().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.toList()));
    }

    public static XList<String> tokensOf(String text, String separator) {
        return new XList<>(text.split(separator));
    }

    public static XList<String> tokensOf(String text) {
        return new XList<>(text.split("\\s+"));
    }

    public XList<T> union(Collection<? extends T> collection) {
        XList<T> newList = new XList<>(this);
        newList.addAll(collection);
        return newList;
    }

    @SafeVarargs
    public final XList<T> union(T... array) {
        XList<T> newList = new XList<>(this);
        newList.addAll(Arrays.asList(array));
        return newList;
    }

    public XList<T> diff(Collection<? extends T> collection) {
        XList<T> newList = new XList<>(this);
        newList.removeAll(collection);
        return newList;
    }

    public XList<T> unique() {
        return new XList<>(new LinkedHashSet<>(this));
    }

    public <R> XList<R> collect(Function<T, R> function) {
        return new XList<>(this.stream().map(function).collect(Collectors.toList()));
    }

    public String join() {
        return this.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public String join(String separator) {
        return this.stream().map(String::valueOf).collect(Collectors.joining(separator));
    }

    public void forEachWithIndex(BiConsumer<T, Integer> consumer) {
        for (int i = 0; i < this.size(); i++) {
            consumer.accept(this.get(i), i);
        }
    }

    public XList<XList<T>> combine() {
        XList<XList<T>> result = new XList<>();
        if (this.isEmpty()) {
            result.add(new XList<>());
            return result;
        }

        Object firstObj = this.get(0);
        if (!(firstObj instanceof List)) throw new IllegalArgumentException("combine() expects XList of Lists");

        List<T> first = (List<T>) firstObj;
        XList<XList<T>> restCombinations = new XList<>(this.subList(1, this.size())).combine();

        for (T element : first) {
            for (XList<T> combination : restCombinations) {
                XList<T> newCombination = new XList<>();
                newCombination.add(element);
                newCombination.addAll(combination);
                result.add(newCombination);
            }
        }
        return result;
    }
}
