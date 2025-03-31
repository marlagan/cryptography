package Model;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReadWriteFile {

    private static final String directory = System.getProperty("user.dir") + "\\src\\GUI";


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

    public static void main(String[] args) throws IOException {
        System.out.println(directory);
        DES DES = new DES();
        writeText("wielkitest.txt", "Dochodze do konkluzji, ze mam dosc");
        String r = readText("wielkitest.txt");
        System.out.println(r);
        byte [] rr = r.getBytes();
        System.out.println("feegeg");
        Tools tools = new Tools();
        tools.printOneBlock(rr);
        String o1 = DES.encrypt(r, "123abcd");
        String o2 = DES.decrypt(o1, "123abcd");
        System.out.println(o1);
        System.out.println(o2);
    }

}



