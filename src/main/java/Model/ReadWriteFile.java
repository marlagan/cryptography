package Model;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class ReadWriteFile {

    private static final String directory = System.getProperty("user.dir") + "\\src\\main\\java\\GUI";

    public static String readText(String path) throws IOException {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(directory + "\\" + path), StandardCharsets.UTF_8))) {
            String lineInFile;
            while ((lineInFile = reader.readLine()) != null) {
                text.append(lineInFile);
            }
        }

        String textString = text.toString();
        textString = textString.replace(System.lineSeparator(), "");
        return textString;
    }

    public static void writeText(String path, String text) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(directory + "\\" + path), StandardCharsets.UTF_8))) {
            writer.write(text);
        }

    }

    public static byte[] readFileBytes(String path) throws IOException {
        return Files.readAllBytes(Path.of(path));
    }

    public static void writeFileBytes(String path, byte[] data) throws IOException {
        Files.write(Path.of(path), data);
    }

}



