package Model;

import java.util.ArrayList;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_16LE;

public class Main {
    public static void main(String[] args) {

       ArrayList<String> keys = new ArrayList<>(Arrays.asList("12345678", "12345678", "12345678"));
       DES3 DES3 = new DES3();
       //String message = "Me? I have my own addiction. It's not substances, it's not gambling, it's not something that ruins lives though some might disagree with me. My addiction is kebab. The smell of grilled meat, the fresh vegetables, the sauce that perfectly completes the whole thing its my ritual, my daily dose of pleasure. Without it, the day feels incomplete, and every break at work feels empty. Every bite is like a small moment of happiness that satisfies not just my hunger, but also my soul.";
        String message = "gowno z dupy";

       String encrypted = DES3.encryptDES3(message, keys);
       String decrypted = DES3.decryptDES3(encrypted, keys);
       System.out.println(encrypted);
       System.out.println(decrypted);


    }

}

