package Model;

import java.io.*;

public class ReadWriteFile {

    public static String readText(String path) throws IOException {

        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String lineInFile;

            while ((lineInFile = reader.readLine()) != null) {
                text.append(lineInFile).append(System.lineSeparator());
            }

        } catch (IOException e) {
            throw new IOException(e);
        }

        return text.toString();
    }

    public static void writeText(String path, String text) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(text);
        }
    }

}



