package Model;

import java.util.ArrayList;
import static java.nio.charset.StandardCharsets.UTF_16LE;

public class Main {
    public static void main(String[] args) {
        DES des = new DES();
        String test = "Store leftover pancakes in an airtight container in the fridge for about a week. Refrain from adding toppings (such as syrup) until right before you serve them so the pancakes don't get soggy.";
        String output = des.encrypt(test, "123abcd");
        System.out.println(output);
        String decrypted = des.decrypt(output, "123abcd");
        System.out.println(decrypted);

    }

    }

