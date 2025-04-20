package Model;

import java.util.ArrayList;

import static Model.Data.*;

public class Key {
    /**
     * Generating subKeys for 16 round
     * @param key the key given by a user
     * @return list containing subkeys for 16 rounds
     */
    public ArrayList<byte[]> generateSubKeys(byte[] key) {
        Data data = new Data();
        byte[] key56 = data.permute(PC1, key); 
        ArrayList<byte[]> subKeys = new ArrayList<>();
        int half = key56.length / 2;
        byte[] key28L = new byte[half];
        byte[] key28R = new byte[half];
        System.arraycopy(key56, 0, key28L, 0, half);
        System.arraycopy(key56, half, key28R, 0, half);

        for (int i = 0; i < 16; i++) { 

            key28L = shiftLeft(key28L, shiftBits[i]);
            key28R = shiftLeft(key28R, shiftBits[i]);

            System.arraycopy(key28L, 0, key56, 0, half);
            System.arraycopy(key28R, 0, key56, half, half);

            subKeys.add(data.permute(PC2, key56)); 
        }

        return subKeys;
    }

    /**
     * Method shifting bits to the left
     * @param bits bits we want to shift
     * @param shiftAmount amount of shifts we have to do
     * @return altered bits
     */
    private byte[] shiftLeft(byte[] bits, int shiftAmount){ 
        
        int bitField = 0; 
        for (int i = 0; i < bits.length; i++) {
            bitField = (bitField << 1) | (bits[i] & 1);
        }


        bitField &= 0x0FFFFFFF;
        bitField = ((bitField << shiftAmount) | (bitField >>> (28 - shiftAmount))) & 0x0FFFFFFF;
        byte[] newBits = new byte[bits.length];
        for (int i = bits.length - 1; i >= 0; i--) {
            newBits[i] = (byte) (bitField & 0xFF);
            bitField >>= 8;
        }

        return newBits;
    }

}
