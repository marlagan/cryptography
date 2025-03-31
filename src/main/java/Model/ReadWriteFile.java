package Model;

import java.nio.charset.StandardCharsets;
import java.io.*;


public class ReadWriteFile {

    //The String containing the path to the GUI directory.
    //private static final String directory = System.getProperty("user.dir") + "\\src\\GUI";
    private static final String directory = System.getProperty("user.dir") + "\\src\\main\\java\\GUI";

    /**
     * Method reading the text from the file
     * @param path The path where the file we want to read the text from is located
     * @return Text from the file
     * @throws IOException exception
     */
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

    /**
     * Method responsible for saving text to the file
     * @param path the name of the file
     * @param text the text we want to save
     * @throws IOException exception
     */
    public static void writeText(String path, String text) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(directory + "\\" + path), StandardCharsets.UTF_8))) {
            writer.write(text);
        }

    }

}



