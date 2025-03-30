package Model;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        DES des = new DES();

        String plainText = "aaaaaaaa"; //tekst do zaszyfrowania
        String key = "12345678"; //klucz des (64bit)

        System.out.println("=== DES ENCRYPTION/DECRYPTION ===");
        System.out.println("Plain Text: " + plainText);
        System.out.println("Key: " + key);

        //szyfrowanie
        String encryptedText = des.encrypt(plainText, key);
        System.out.println("\nEncrypted Text (String): ");
        System.out.println(encryptedText);

        //deszyfrowanie
        String decryptedText = des.decrypt(encryptedText, key);
        System.out.println("\nDecrypted Text: " + decryptedText);

        //sprawdzenie poprawności
        if (plainText.equals(decryptedText)) {
            System.out.println("\nDES działa poprawnie");
        } else {
            System.out.println("\nDES nie działa poprawnie (błąd w szyfrowaniu/deszyfrowaniu)");
        }


//        DES3 des3 = new DES3();
//
//        String plainText3 = "Hello";
//        ArrayList<String> keys3 = new ArrayList<>();
//        keys3.add("12345678");
//        keys3.add("12345678");
//        keys3.add("12345678");
//
//        System.out.println("=== TripleDES ENCRYPTION/DECRYPTION ===");
//        System.out.println("Plain Text: " + plainText);
//        System.out.println("Key: " + key);
//
//        //szyfrowanie
//        String encryptedText3 = des3.encryptDES3(plainText3, keys3);
//        System.out.println("\nEncrypted Text (String): ");
//        System.out.println(encryptedText3);
//
//        //deszyfrowanie
//        String decryptedText3 = des3.decryptDES3(encryptedText3, keys3);
//        System.out.println("\nDecrypted Text: " + decryptedText3);
//
//        //sprawdzenie poprawności
//        if (plainText3.equals(decryptedText3)) {
//            System.out.println("\nTripleDES działa poprawnie");
//        } else {
//            System.out.println("\nTripleDES nie działa poprawnie (błąd w szyfrowaniu/deszyfrowaniu)");
//        }


    }

}