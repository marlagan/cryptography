package GUI;
import Model.DES;
import Model.ReadWriteFile;

import java.io.IOException;

public class Interaction {
    public static void main(String[] args) throws IOException {
        String currentDirectoryPath = System.getProperty("user.dir");
        System.out.println("Current directory: " + currentDirectoryPath);
        String fromFile = ReadWriteFile.readText(currentDirectoryPath+ "\\src\\file.txt");
        DES des = new DES();
        String encrypted = des.encrypt(fromFile, "123abcd");
        /*
        ReadWriteFile.writeText(encrypted, currentDirectoryPath+"\\src\\file1.txt");

        String fromFileTwo = ReadWriteFile.readText(currentDirectoryPath+"\\src\\file1.txt");
        String test = des.decrypt(fromFileTwo, "123abcd");
        ReadWriteFile.writeText(test, currentDirectoryPath+"\\src\\test.txt");
         */
    }
}
