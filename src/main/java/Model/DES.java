package main.java.Model;

import main.java.Model.Tools;
import main.java.Model.Key;

import java.util.ArrayList;
import java.util.Collections;

import static main.java.Model.Data.*;

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

    /**
     * Method encrypting the message with a key given by a user, It repeats block64Encryption on very 64 bits block
     * @param plainText text given by a user
     * @param key key given by a user
     * @return encrypted message
     */
    public String encrypt(String plainText, String key) {

        ArrayList<byte[]> blocks64Bits  = tools.stringToBits(plainText);

        this.subKeys = this.key.generateSubKeys(tools.stringToBits(key).get(0)); //get(0) -> wyciagamy pierwszy blok 64-bitowy (tablice 8 bajtow), bo tak dlugi jest klucz, a .blocks64bits() zwraca liste tych tablic

        ArrayList<byte[]> encryptedBlocks64Bits = new ArrayList<>(); //kazdy blok (64bit) tekstu szyfrujemy za pomoca block64Encryption()
        for (byte[] block : blocks64Bits) {
            byte[] encrypted = block64Encryption(block);
            encryptedBlocks64Bits.add(encrypted);
        }

        return tools.bitsToString(encryptedBlocks64Bits);
    }

    /**
     * Applying DES encryption to 64 bits block
     * @param bits bits - 64 bits (64 bytes - one byte = one bit)
     * @return  bits after 16 rounds and the final permutation
     */
    private byte[] block64Encryption(byte[] bits) {

        byte[] afterIP = this.data.permute(Data.IP, bits);
        int half = afterIP.length / 2;

        byte[] bits32L = new byte[half];
        byte[] bits32R = new byte[half];

        System.arraycopy(afterIP, 0, bits32L, 0, half);
        System.arraycopy(afterIP, half, bits32R, 0, half);

        for (int i = 0; i < 16; i++) {

            byte[] newR = functionF(bits32R, subKeys.get(i));

            byte[] oldR = bits32R.clone();

            for (int j = 0; j < bits32R.length; j++) {
                bits32R[j] = (byte) (newR[j] ^ bits32L[j]);
            }

            bits32L = oldR;

        }

        byte[] oldL = bits32L.clone();
        bits32L = bits32R.clone();
        bits32R = oldL;

        byte[] newBits = new byte[bits32L.length + bits32R.length];
        System.arraycopy(bits32L, 0, newBits, 0, bits32L.length);
        System.arraycopy(bits32R, 0, newBits, half, bits32R.length);

        byte[] finalE = data.permute(IP1, newBits);

        return finalE;
    }

    /**
     * Feistel function using such techniques as Expansion Permutation, XOR , SBOX permutation, P permutation
     * @param bitsR the right half of bits (at first 32 bits)
     * @param key the subkey for current round
     * @return bits after Feistel Function
     */
    private byte[] functionF(byte[] bitsR, byte[] key) {


        byte[] expBitsR = data.permute(EP, bitsR);

        byte[] XORBits = new byte[expBitsR.length];

        for (int i = 0; i < expBitsR.length; i++) {
            XORBits[i] = (byte) (expBitsR[i] ^ key[i]);
        }

        byte[] afterSBOX = data.useSBOX(XORBits);

        byte[] lastPermute = data.permute(P, afterSBOX);

        return lastPermute;
    }

    /**
     * Method decrypting the message given by a user
     * @param encryptedText encypted text given by a user
     * @param key the key for this decryption
     * @return decrypted text
     */
    public String decrypt(String encryptedText, String key) {

        ArrayList<byte[]> blocks64Bits = tools.stringToBits(encryptedText);

        this.subKeys = this.key.generateSubKeys(tools.stringToBits(key).get(0));
        Collections.reverse(subKeys);

        ArrayList<byte[]> decryptedBlocks64Bits = new ArrayList<>();
        for (byte[] block : blocks64Bits) {
            byte[] encrypted = block64Encryption(block);
            decryptedBlocks64Bits.add(encrypted);

        }

        return tools.bitsToString(decryptedBlocks64Bits);
    }

}
