/**
 *
 *  @author Tarkowska Milena S30638
 *
 */

package zad3;


import java.util.ArrayList;
import java.util.List;

public class ListCreator<A, B> {
    // Uwaga: klasa musi być sparametrtyzowana
    private List<A> src;
    private Selector<A> selector;

    private ListCreator(List<A> src){
        this.src = src;
    }

    public static <A, B> ListCreator<A, B> collectFrom(List<A> src){
        return new ListCreator<>(src);
    }

    public ListCreator<A, B> when(Selector<A> selector){
        this.selector = selector;
        return this;
    }

    public <B> List<B> mapEvery(Mapper<A, B> mapper){
        List<B> result = new ArrayList<>();
        for (A item : src) {
            if (selector.select(item)) {
                result.add(mapper.map(item));
            }
        }
        return result;
    }

}  
