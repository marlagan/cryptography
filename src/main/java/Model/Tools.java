package Model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;


public class Tools {

    /**
     * Method converting all chars in text variable to bytes(U2)
     * @param text The whole text - more than 64 bits(8 bytes)
     * @return chars represented as bytes (U2 for example 180 = - 76)
     */
    public byte[] byteConversion(String text) {

        byte[] allBytes = new byte[text.length()];
        for (int i = 0; i < text.length(); i++) {
            allBytes[i] = (byte) text.charAt(i);
        }
        return allBytes;

    }

    /**
     * Method diving text into byte[] blocks, where one byte[] array is equivalent to 64 bits(one byte one bit)
     * @param text The text entered by a user (whole block, can be more than 64 bits)
     * @return ArrayList containing byte[] arrays - one byte[] array 64 bytes, where one byte stands for one bit
     */
    public ArrayList<byte[]> stringToBits(String text) {

        ArrayList<byte[]> blocks = new ArrayList<>();

        byte[] bytes = byteConversion(text);
        int blocksAmt = (int) Math.ceil(bytes.length / 8.0);

        for (int i = 0; i < blocksAmt; i++) {
            byte[] block64Bits = oneArray8Bytes(bytes, i);
            blocks.add(block64Bits);
        }
        return blocks;
    }

    /**
     *
     * @param bytes
     * @return
     */

    public static byte[] oneArray8Bytes(byte[] bytes, int blockIndex) {
        int count = 0;

        // Obliczamy liczbę bitów dla danego bloku
        int startIndex = blockIndex * 8;
        int remainingBytes = Math.min(8, bytes.length - startIndex);
        byte[] bits64 = new byte[64];

        for (int i = startIndex; i < startIndex + remainingBytes; i++) {
            byte[] bits8 = oneByteOneBit(bytes[i]);

            for (int j = 0; j < 8; j++) {
                bits64[count] = bits8[j];
                count++;
            }
        }

        if (remainingBytes < 8) {
            for (int i = count; i < 64; i++) {
                bits64[i] = 0;
            }
        }

        return bits64;
    }
    /**
     *
     * @param value
     * @return
     */
    public static byte[] oneByteOneBit(byte value) {

        byte[] bits8 = new byte[8];
        int number = value & 0xFF;

        for (int i = 7; i >= 0; i--) {
            bits8[i] = (byte) (number % 2);
            number = number / 2;
        }

        return bits8;
    }

    public String bitsToString(ArrayList<byte[]> blocks) {
        StringBuilder text = new StringBuilder();

        for (byte[] block : blocks) {
            text.append(oneByteOneBitToString(block));
        }

        return text.toString();
    }


    public static String oneByteOneBitToString(byte[] bits) {

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < bits.length; i += 8) {
            byte byteValue = 0;
            for (int j = 0; j < 8; j++) {
                byteValue |= (byte) (bits[i + j] << (7 - j));
            }
            if(byteValue != 0)  text.append((char) (byteValue & 0xFF));

        }

        return text.toString();
    }



    public void printOneBlock(byte[] oneBlock) {
        for (int i = 0; i < oneBlock.length; i++) {
            if (i % 8 == 0) {
                System.out.println();
            }
            System.out.print(oneBlock[i] + " ");
        }
    }

}
