package zad3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProgLang {
    private Map<String, Set<String>> langsMap = new LinkedHashMap<>();
    private Map<String, Set<String>> progsMap = new LinkedHashMap<>();

    public ProgLang(String fileName) throws IOException, FileNotFoundException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        for (String line : lines) {
            String[] parts = line.split("\t");
            if(parts.length > 1){
                String lang = parts[0];
                Set<String> progs = new LinkedHashSet<>(Arrays.asList(parts).subList(1, parts.length));
                langsMap.put(lang, progs);

                for(String prog : progs){
                    progsMap.computeIfAbsent(prog, k -> new LinkedHashSet<>()).add(lang);
                }
            }
        }
    }

    public Map<String, Set<String>> getLangsMap() {
        return new LinkedHashMap<>(langsMap);
    }

    public Map<String, Set<String>> getProgsMap() {
        return new LinkedHashMap<>(progsMap);
    }

    public Map<String, Set<String>> getLangsMapSortedByNumOfProgs() {
        return sorted(
                langsMap,
                Comparator.<Map.Entry<String, Set<String>>>comparingInt(e -> -e.getValue().size())
                        .thenComparing(Map.Entry::getKey)
        );
    }

    public Map<String, Set<String>> getProgsMapSortedByNumOfLangs() {
        return sorted(
                progsMap,
                Comparator.<Map.Entry<String, Set<String>>>comparingInt(e -> -e.getValue().size())
                        .thenComparing(Map.Entry::getKey)
        );
    }

    public Map<String, Set<String>> getProgsMapForNumOfLangsGreaterThan(int n) {
        return filtered(
                progsMap,
                e -> e.getValue().size() > n
        );
    }

    public static <K, V> Map<K, V> sorted(Map<K, V> map, Comparator<Map.Entry<K, V>> comp) {
        return map.entrySet().stream()
                .sorted(comp)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public static <K, V> Map<K, V> filtered(Map<K, V> map, Predicate<Map.Entry<K, V>> pred) {
        return map.entrySet().stream()
                .filter(pred)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
