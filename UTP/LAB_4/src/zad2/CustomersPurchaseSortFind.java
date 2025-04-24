/**
 *
 *  @author Tarkowska Milena S30638
 *
 */

package zad2;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CustomersPurchaseSortFind {
    private List<Purchase> purchases = new ArrayList<>();

    public void readFile(String filename) {
        try(Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                purchases.add(new Purchase(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showSortedBy(String criteria){
        if(criteria.equals("Nazwiska")){
            System.out.println("Nazwiska");
            purchases.stream()
                    .sorted(Comparator.comparing(Purchase::getSurname).thenComparing(Purchase::getID))
                    .forEach(p -> System.out.println(p.toString()));
            System.out.println();
        } else if(criteria.equals("Koszty")){
            System.out.println("Koszty");
            purchases.stream()
                    .sorted(Comparator.comparing(Purchase::getCost).reversed().thenComparing(Purchase::getID))
                    .forEach(p -> System.out.println(p.toStringWithCost() + " (koszt: " + p.getCost() + ")"));
            System.out.println();
        }
    }

    public void showPurchaseFor(String id){
        System.out.println("Klient " + id);
        purchases.stream()
                .filter(p -> p.getID().equals(id))
                .forEach(System.out::println);
        System.out.println();
    }
}
