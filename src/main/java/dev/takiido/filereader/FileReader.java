package dev.takiido.filereader;

import java.io.InputStream;
import java.util.Scanner;

public class FileReader {
    public static String readFile(String filePath) {
        InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(filePath);

        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                return line;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }
}
