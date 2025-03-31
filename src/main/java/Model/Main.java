package Model;

import java.util.ArrayList;
import static java.nio.charset.StandardCharsets.UTF_16LE;

public class Main {
    public static void main(String[] args) {
//        DES des = new DES();
//        String test = "Store leftover pancakes in an airtight container in the fridge for about a week. Refrain from adding toppings (such as syrup) until right before you serve them so the pancakes don't get soggy.";
//        String output = des.encrypt(test, "123abcd");
//        System.out.println("Szyfr: ");
//        System.out.println(output);
//        String decrypted = des.decrypt(output, "123abcd");
//        System.out.println("Test po: ");
//        System.out.println(decrypted);

        DES3 des3 = new DES3();

        ArrayList<String> keys3 = new ArrayList<>();
        keys3.add("abcabcab");
        keys3.add("abcabcab");
        keys3.add("abcabcab");

        //String test3 = "Store leftover pancakes in an airtight container in the fridge for about a week. Refrain from adding toppings (such as syrup) until right before you serve them so the pancakes don't get soggy.";
        String test3 = "kot";

        System.out.println("Tekst jawny: ");
        System.out.println(test3);
        String output3 = des3.encryptDES3(test3, keys3);
        System.out.println("Szyfr: ");
        System.out.println(output3);
        String decrypted3 = des3.decryptDES3(output3, keys3);
        System.out.println("Uzyskany tekst jawny: ");
        System.out.println(decrypted3);
    }

}

