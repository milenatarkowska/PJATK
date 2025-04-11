/**
 *
 *  @author Tarkowska Milena S30638
 *
 */

package zad1;

/*<--
 *  niezbędne importy
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class Main {
  public static void main(String[] args) {
    /*<--
     *  definicja operacji w postaci lambda-wyrażeń:
     *  - flines - zwraca listę wierszy z pliku tekstowego
     *  - join - łączy napisy z listy (zwraca napis połączonych ze sobą elementów listy napisów)
     *  - coll-ectInts - zwraca listę liczb całkowitych zawartych w napisie
     *  - sum  zwraca sumę elmentów listy liczb całkowitych
     */

     Function<String, List<String>> flines = filename -> {
      try{
        return Files.readAllLines(Paths.get(filename));
      } catch (IOException e){
        e.printStackTrace();
        return Collections.emptyList();
      }
    };

    Function<List<String>, String> join = list -> String.join(" ", list);

    Function<String, List<Integer>> collectInts = text -> {
      List<Integer> nums = new ArrayList<>();
      String cleanedText = text.replaceAll("[^0-9\\s]", "");

      String[] tokens = cleanedText.split("\\s+");

      for (String token : tokens) {
        try {
          nums.add(Integer.parseInt(token));
        } catch (NumberFormatException e) {
        }
      }
      return nums;
    };

    Function<List<Integer>, Integer> sum = list -> list.stream().reduce(0, Integer::sum);

    String fname = System.getProperty("user.home") + "/LamComFile.txt"; 
    InputConverter<String> fileConv = new InputConverter<>(fname);
    List<String> lines = fileConv.convertBy(flines);
    String text = fileConv.convertBy(flines, join);
    List<Integer> ints = fileConv.convertBy(flines, join, collectInts);
    Integer sumints = fileConv.convertBy(flines, join, collectInts, sum);

    System.out.println(lines);
    System.out.println(text);
    System.out.println(ints);
    System.out.println(sumints);

    List<String> arglist = Arrays.asList(args);
    InputConverter<List<String>> slistConv = new InputConverter<>(arglist);  
    sumints = slistConv.convertBy(join, collectInts, sum);
    System.out.println(sumints);

  }
}
