/**
 *
 *  @author Tarkowska Milena S30638
 *
 */

package zad2;
import java.util.*;

public class Main {

  static List<String> getPricesInPLN(List<String> destinations, double xrate) {
    return ListCreator.collectFrom(destinations)
            /*<-- lambda wyrażenie
             *  selekcja wylotów z Warszawy (zaczynających się od WAW)
             */
                       .when(dest -> dest.startsWith("WAW"))
            /*<-- lambda wyrażenie
             *  wyliczenie ceny przelotu w PLN
             *  i stworzenie wynikowego napisu
             */
                       .mapEvery(dest -> {
                         String[] splitStr = dest.split(" ");
                         String destination = splitStr[1];
                         int priceEUR = Integer.parseInt(splitStr[2]);
                         int pricePLN = (int)(priceEUR * xrate);
                         return "to " + destination + " " + "- price in PLN: " + pricePLN;
                       });
  }

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
    List<String> result = getPricesInPLN(dest, ratePLNvsEUR);
    for (String r : result) System.out.println(r);
  }
}
