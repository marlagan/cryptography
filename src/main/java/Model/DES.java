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
        this.data = new Model.Data();
    }

    /**
     * Method encrypting the message with a key given by a user, It repeats block64Encryption on very 64 bits block
     * @param blocks64Bits text given by a user
     * @param key key given by a user
     * @return encrypted message
     */
    public ArrayList<byte[]> encrypt(ArrayList<byte[]> blocks64Bits, byte[] key) {

        this.subKeys = this.key.generateSubKeys(key); 
        ArrayList<byte[]> encryptedBlocks64Bits = new ArrayList<>(); 
        for (byte[] block : blocks64Bits) {
            byte[] encrypted = block64Encryption(block);
            encryptedBlocks64Bits.add(encrypted);
        }

        return encryptedBlocks64Bits;
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
     * @param blocks64Bits encrypted text given by a user
     * @param key the key for this decryption
     * @return decrypted text
     */
    public ArrayList<byte[]> decrypt(ArrayList<byte[]> blocks64Bits, byte[] key) {

        //ArrayList<byte[]> blocks64Bits = tools.stringToBits(encryptedText);

        this.subKeys = this.key.generateSubKeys(key);
        Collections.reverse(subKeys);

        ArrayList<byte[]> decryptedBlocks64Bits = new ArrayList<>();
        for (byte[] block : blocks64Bits) {
            byte[] encrypted = block64Encryption(block);
            decryptedBlocks64Bits.add(encrypted);

        }

        return decryptedBlocks64Bits;
    }

}
