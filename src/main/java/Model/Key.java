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
        byte[] key56 = data.permute(PC1, key); //usuwamy bity parzystosci poprzez permutacje z pominięciem tych bitów, więc otrzymujemy 56 bity z 64 bitów klucza
        ArrayList<byte[]> subKeys = new ArrayList<>();
        int half = key56.length / 2;
        byte[] key28L = new byte[half];
        byte[] key28R = new byte[half];
        //System.out.println("kluczeee");
        System.arraycopy(key56, 0, key28L, 0, half);
        System.arraycopy(key56, half, key28R, 0, half);

        for (int i = 0; i < 16; i++) { //dla rund 1,2,9 i 16 rotujemy raz, dla pozostałych rotujemy 2

            key28L = shiftLeft(key28L, shiftBits[i]);
            key28R = shiftLeft(key28R, shiftBits[i]);

            System.arraycopy(key28L, 0, key56, 0, half); //łączenie spowrotem do jednej tablicy
            System.arraycopy(key28R, 0, key56, half, half);

            subKeys.add(data.permute(PC2, key56)); //permutacja 56 bitow klucza -> 48 bitów każdy podklucz
        }

        return subKeys;
    }

    /**
     * Method shifting bits to the left
     * @param bits bits we want to shift
     * @param shiftAmount amount of shifts we have to do
     * @return altered bits
     */
    private byte[] shiftLeft(byte[] bits, int shiftAmount){ //przesunięcie powinno być na bitach, a nie na bajtach (?)

        //to akurat głównie wypluł czat, ale trochę nie ogarniam tych przesunięć, więc pewnie poprawię zaraz:

        int bitField = 0; // Zmienna przechowująca 28 bitów jako liczba całkowita

        // Łączenie bajtów w jedną liczbę 28-bitową
        for (int i = 0; i < bits.length; i++) {
            bitField = (bitField << 1) | (bits[i] & 1);
        }

        // Usunięcie zbędnych bitów poza 28-bitowym zakresem
        bitField &= 0x0FFFFFFF;

        // Przesunięcie bitowe z zawinięciem (cykliczny shift)
        bitField = ((bitField << shiftAmount) | (bitField >>> (28 - shiftAmount))) & 0x0FFFFFFF;

        // Konwersja z powrotem na tablicę bajtów
        byte[] newBits = new byte[bits.length];
        for (int i = bits.length - 1; i >= 0; i--) {
            newBits[i] = (byte) (bitField & 0xFF);
            bitField >>= 8;
        }

        return newBits;
    }

}
