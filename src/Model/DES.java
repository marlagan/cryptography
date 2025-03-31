package Model;

import java.util.ArrayList;
import java.util.Collections;

import static Model.Data.*;

public class DES {

    private Tools tools;
    private Key key;
    private Data data;
    private ArrayList<byte[]> subKeys;

    public DES(){
        this.tools = new Tools();
        this.key = new Key();
        this.data = new Data();

    }

    public String encrypt(String plainText, String key) {

        ArrayList<byte[]> blocks64Bits  = tools.stringToBits(plainText);
        System.out.println("PRZED SZYFROWANIEM:");
        for (byte[] block : blocks64Bits) {
            tools.printOneBlock(block);
            System.out.println("\n");
        }


        //zamiana na bity i tworzenie podkluczy

        this.subKeys = this.key.generateSubKeys(tools.stringToBits(key).get(0)); //get(0) -> wyciagamy pierwszy blok 64-bitowy (tablice 8 bajtow), bo tak dlugi jest klucz, a .blocks64bits() zwraca liste tych tablic

        ArrayList<byte[]> encryptedBlocks64Bits = new ArrayList<>(); //kazdy blok (64bit) tekstu szyfrujemy za pomoca block64Encryption()
        for (byte[] block : blocks64Bits) {
            byte[] encrypted = block64Encryption(block);
            encryptedBlocks64Bits.add(encrypted);
        }

        System.out.println("PO SZYFROWANIU:");
        for (byte[] block : encryptedBlocks64Bits) {
            tools.printOneBlock(block);
            System.out.println("\n");
        }

        return tools.bitsToString(encryptedBlocks64Bits);
    }

    /**
     *
     * @param bits Tablica z obiektami byte[], dokladnie 8 bajtów
     * @return
     */
    private byte[] block64Encryption(byte[] bits) {

        byte[] afterIP = this.data.permute(Data.IP, bits);
        int half = afterIP.length / 2;

        byte[] bits32L = new byte[half];
        byte[] bits32R = new byte[half];

        System.arraycopy(afterIP, 0, bits32L, 0, half);
        System.arraycopy(afterIP, half, bits32R, 0, half);

        for (int i = 0; i < 16; i++) {
            //System.out.println("Runda: " + (i+1));
            byte[] newR = functionF(bits32R, subKeys.get(i));
            //System.out.println("ilosc po funkcji, nowa tymczasowa R: " + (newR.length) + " /32");
            byte[] oldR = bits32R.clone();
            //System.out.println("klon starej R: " + (oldR.length) + " /32");

            for (int j = 0; j < bits32R.length; j++) {
                bits32R[j] = (byte) (newR[j] ^ bits32L[j]);
            }
            //System.out.println("nowa, stała R: " + (bits32R.length) + " /32");

            bits32L = oldR;
            //System.out.println("nowa, stała L (kopia oldR): " + (bits32L.length) + " /32");
        }

        byte[] oldL = bits32L.clone();
        bits32L = bits32R.clone();
        bits32R = oldL;

        byte[] newBits = new byte[bits32L.length + bits32R.length];
        System.arraycopy(bits32L, 0, newBits, 0, bits32L.length);
        System.arraycopy(bits32R, 0, newBits, half, bits32R.length);

        byte[] finalE = data.permute(IP1, newBits);
        //System.out.println("koncowa : " + (finalE.length) + " /64");

        return finalE;
    }

    private byte[] functionF(byte[] bitsR, byte[] key) {

        //System.out.println("ilosc przed perm. ext.: " + (bitsR.length) + " /32");
        byte[] expBitsR = data.permute(EP, bitsR);  //permutacja z rozszerzeniem 32 -> 48 bitów
        //System.out.println("ilosc po perm. ext.: " + (expBitsR.length) + " /48");
        byte[] XORBits = new byte[expBitsR.length];

        for (int i = 0; i < expBitsR.length; i++) {
            XORBits[i] = (byte) (expBitsR[i] ^ key[i]);
        }
        //System.out.println("ilosc po XOR: " + (XORBits.length) + " /48");

        byte[] afterSBOX = data.useSBOX(XORBits);
        //System.out.println("ilosc po SBOX: " + (afterSBOX.length) + " /32");

        byte[] lastPermute = data.permute(P, afterSBOX);
        //System.out.println("ilosc po P: " + (lastPermute.length) + " /32");

        return lastPermute;
    }

    public String decrypt(String encryptedText, String key) {

        ArrayList<byte[]> blocks64Bits = tools.stringToBits(encryptedText);

        System.out.println("PRZED DESZYFROWANIEM:");
        for (byte[] block : blocks64Bits) {
            tools.printOneBlock(block);
            System.out.println("\n");
        }

        this.subKeys = this.key.generateSubKeys(tools.stringToBits(key).get(0));
        Collections.reverse(subKeys);


        ArrayList<byte[]> decryptedBlocks64Bits = new ArrayList<>();
        for (byte[] block : blocks64Bits) {
            byte[] encrypted = block64Encryption(block);
            decryptedBlocks64Bits.add(encrypted);
            System.out.println("supi wazne");
            tools.printOneBlock(encrypted);
        }


        System.out.println("PO DESZYFROWANIU:");
        for (byte[] block : decryptedBlocks64Bits) {
            tools.printOneBlock(block);
            System.out.println("\n");
        }

        return tools.bitsToString(decryptedBlocks64Bits);
    }

}
