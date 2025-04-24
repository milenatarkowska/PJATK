/**
 *
 *  @author Tarkowska Milena S30638
 *
 */

package zad2;


public class Purchase {
    private String id;
    private String surname;
    private String name;
    private String purchasedItem;
    private double itemPrice;
    private double itemsQuantity;

    public Purchase(String lineFromFile){
        String[] parts = lineFromFile.split(";");
        this.id = parts[0];
        String[] nameAndSurname = parts[1].split(" ");
        this.surname = nameAndSurname[0];
        this.name = nameAndSurname[1];
        this.purchasedItem = parts[2];
        this.itemPrice = Double.parseDouble(parts[3]);
        this.itemsQuantity = Double.parseDouble(parts[4]);
    }

    public String getSurname() {
        return surname;
    }

    public String getID(){
        return id;
    }

    public double getCost(){
        return itemPrice * itemsQuantity;
    }

    public String toString() {
        return id + ";" + surname + " " + name + ";" + purchasedItem + ";" + itemPrice + ";" + itemsQuantity;
    }

    public String toStringWithCost() {
        return toString();
    }
}
