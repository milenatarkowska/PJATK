/**
 *
 *  @author Tarkowska Milena S30638
 *
 */

package zad1;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//konstruktor klasy
public class Anagrams {
    private Map<String, List<String>> anagrams = new LinkedHashMap<>();
    private Set<String> allWords = new LinkedHashSet<>();

    public Anagrams(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNext()) {
            String word = scanner.next();
            allWords.add(word);
            String sortedWord = sortLetters(word);
            anagrams.computeIfAbsent(sortedWord, k -> new ArrayList<>()).add(word);
        }
        scanner.close();
    }

    //metoda sortujaca litery w slowie
    private String sortLetters(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    //metoda sortujaca
    public List<List<String>> getSortedByAnQty(){
        List<List<String>> res = new ArrayList<>();

        for(List<String> list : anagrams.values()){
            if(!list.isEmpty()){
                Collections.sort(list);
                res.add(list);
            }
        }
        res.sort((x, y) -> {
            int compare = Integer.compare(y.size(), x.size());
            if(compare != 0){
                return compare;
            }
            return x.get(0).compareTo(y.get(0));
        });
        return res;
    }

    //metoda get anagrams
    public String getAnagramsFor(String word){
        String sortedWord = sortLetters(word);
        List<String> list = anagrams.getOrDefault(sortedWord, new ArrayList<>());
        List<String> res = new ArrayList<>();
        for(String s : list){
            if(!s.equals(word)){
                res.add(s);
            }
        }
        Collections.sort(res);
        return word + ": " + res.toString();
    }

}  
