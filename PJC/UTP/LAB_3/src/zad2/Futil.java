package zad2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    public static void processDir(String dirName, String resultFileName){

        Path start = Paths.get(dirName);
        Path end = Paths.get(resultFileName);

        try(BufferedWriter bw = Files.newBufferedWriter(end, StandardCharsets.UTF_8)){

            Files.walkFileTree(start, new SimpleFileVisitor<Path>() {

                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {

                    if(file.toString().endsWith(".txt")){
                        try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file), Charset.forName("Cp1250")))){
                            String line;
                            while((line = br.readLine()) != null){
                                bw.write(line);
                                bw.newLine();
                            }
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
