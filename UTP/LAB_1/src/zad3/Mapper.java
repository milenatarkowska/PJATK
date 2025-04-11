/**
 *
 *  @author Tarkowska Milena S30638
 *
 */

package zad3;


public interface Mapper<S, R> {
    // Uwaga: interfejs musi być sparametrtyzowany

    R map(S s);

}  
