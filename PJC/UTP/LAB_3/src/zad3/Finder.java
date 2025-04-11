/**
 *
 *  @author Tarkowska Milena S30638
 *
 */

package zad3;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {
    private final String filePath;

    public Finder(String filePath) {
        this.filePath = filePath;
    }

    private String removeCommentsAndStrings(String line, boolean[] inBlockComment) {
        StringBuilder result = new StringBuilder();
        boolean inString = false;
        int i = 0;

        while (i < line.length()) {
            if (inBlockComment[0]) {
                if (i + 1 < line.length() && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                    inBlockComment[0] = false;
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            if (!inString && i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                inBlockComment[0] = true;
                i += 2;
                continue;
            }

            if (!inString && i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                break;
            }

            if (line.charAt(i) == '"') {
                inString = !inString;
                i++;
                continue;
            }

            if (!inString) {
                result.append(line.charAt(i));
            }
            i++;
        }

        return result.toString();
    }

    private int countPattern(String regex, boolean ignoreCommentsAndStrings) throws IOException {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new IOException("File does not exist: " + filePath);
        }

        Pattern pattern = Pattern.compile(regex);
        int counter = 0;
        boolean[] inBlockComment = {false};

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = ignoreCommentsAndStrings
                        ? removeCommentsAndStrings(line, inBlockComment)
                        : line;
                Matcher matcher = pattern.matcher(processedLine);
                while (matcher.find()) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public int getIfCount() throws IOException {
        return countPattern("\\bif\\s*\\(", true);
    }

    public int getStringCount(String word) throws IOException {
        return countPattern(Pattern.quote(word), false);
    }
}