package main.java.Model;

import java.util.ArrayList;

public class DES3 {

    DES DES;
    public DES3(){
        this.DES = new DES();
    }
    public String encryptDES3(String text, ArrayList<String> keys){

        String firstRound = DES.encrypt(text, keys.get(0));
        String secondRound = DES.decrypt(text, keys.get(1));

        return DES.encrypt(text, keys.get(2));
    }
    public String decryptDES3(String text, ArrayList<String> keys){

        String firstRound = DES.decrypt(text, keys.get(0));
        String secondRound = DES.decrypt(text, keys.get(1));

        return DES.decrypt(text, keys.get(2));

    }
}
