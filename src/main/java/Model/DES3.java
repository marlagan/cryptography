package Model;

import java.util.ArrayList;

public class DES3 {


    private DES DES;
    private Tools tools;
    public DES3(){
        this.DES = new DES();
        this.tools = new Tools();
    }
    public String encryptDES3(String text, ArrayList<String> keys){

        ArrayList<byte[]> bits = tools.stringToBits(text);
        String firstKey = keys.get(0);
        String secondKey = keys.get(1);
        String thirdKey = keys.get(2);

        byte[] keyFirstBits  = tools.stringToBits(firstKey).get(0);
        byte[] keySecondBits = tools.stringToBits(secondKey).get(0);
        byte[] keyThirdBits = tools.stringToBits(thirdKey).get(0);

        ArrayList<byte[]> bitsOne= DES.encrypt(bits, keyFirstBits);
        ArrayList<byte[]> bitsTwo= DES.decrypt(bitsOne, keySecondBits);
        ArrayList<byte[]> bitsThree= DES.encrypt(bitsTwo, keyThirdBits);

        return tools.bitsToString(bitsThree);
    }

    public String decryptDES3(String text, ArrayList<String> keys){


        ArrayList<byte[]> bits = tools.stringToBits(text);

        String firstKey = keys.get(0);
        String secondKey = keys.get(1);
        String thirdKey = keys.get(2);

        byte[] keyFirstBits  = tools.stringToBits(firstKey).get(0);
        byte[] keySecondBits = tools.stringToBits(secondKey).get(0);
        byte[] keyThirdBits = tools.stringToBits(thirdKey).get(0);

        ArrayList<byte[]> bitsOne= DES.decrypt(bits, keyThirdBits);
        ArrayList<byte[]> bitsTwo= DES.encrypt(bitsOne, keySecondBits);
        ArrayList<byte[]> bitsThree= DES.decrypt(bitsTwo, keyFirstBits);
        String decrypted = tools.bitsToString(bitsThree);
        StringBuilder finalString = new StringBuilder();
        for (char c : decrypted.toCharArray()) {
            if(c != 0){
                finalString.append(c);
            }
        }
        return finalString.toString();

    }
}
