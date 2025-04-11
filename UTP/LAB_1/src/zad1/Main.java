/**
 *
 *  @author Tarkowska Milena S30638
 *
 */

package zad1;


/*<-- niezbędne importy */

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {
    // Lista destynacji: port_wylotu port_przylotu cena_EUR 
    List<String> dest = Arrays.asList(
      "bleble bleble 2000",
      "WAW HAV 1200",
      "xxx yyy 789",
      "WAW DPS 2000",
      "WAW HKT 1000"
    );
    double ratePLNvsEUR = 4.30;
    List<String> result = dest.stream()
            .filter(data -> data.startsWith("WAW"))
            .map(data -> {
              String[] splitted = data.split(" ");
              int priceEUR = Integer.parseInt(splitted[2]);
              int pricePLN = (int)(priceEUR * ratePLNvsEUR);
              String destination = splitted[1];
              return "to " + destination + " " + "- price in PLN: " + pricePLN;
            })
            .collect(Collectors.toList());
    /*<-- tu należy dopisać fragment
     * przy czym nie wolno używać żadnych własnych klas, jak np. ListCreator
     * ani też żadnych własnych interfejsów
     * Podpowiedź: należy użyć strumieni
     */

    for (String r : result) System.out.println(r);
  }
}
