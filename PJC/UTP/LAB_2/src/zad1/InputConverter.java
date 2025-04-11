package zad1;

import java.util.function.Function;

public class InputConverter<T> {
    private final T data;

    public InputConverter(T data) {
        this.data = data;
    }

    public <R> R convertBy(Function<?, ?>... functions){
        Object result = data;
        for(Function<?, ?> f : functions){
            result = ((Function<Object, ?>) f).apply(result);
        }
        return (R) result;
    }
}
