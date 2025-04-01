package Model;

import java.io.ByteArrayOutputStream;
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
     * One block - 64 bits, creating an array representing 8 bytes
     * @param bytes bytes from the text
     * @return returning an array containing 8 bytes - 64 bits (it's 64 bytes because in our code one bit equals one bit)
     */

    public static byte[] oneArray8Bytes(byte[] bytes, int blockIndex) {
        int count = 0;

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
        //The case when the block is not fully filled, therefore, we have to add zeros
        if (remainingBytes < 8) {
            for (int i = count; i < 64; i++) {
                bits64[i] = 0;
            }
        }

        return bits64;
    }
    /**
     * Converting one byte to byte[] array where one byte equals to one bit.
     * @param value Byte value
     * @return  byte[] where one byte is equivalent to one bit
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

    /**
     * Converting bits to String
     * @param blocks bits
     * @return bits converted to String
     */
    public String bitsToString(ArrayList<byte[]> blocks) {
        StringBuilder text = new StringBuilder();

        for (byte[] block : blocks) {
            text.append(oneByteOneBitToString(block));
        }

        return text.toString();
    }

    /**
     * Converting bits to their corresponding char values.
     * @param bits bits we want to convert
     * @return text representation of bits
     */
    public static String oneByteOneBitToString(byte[] bits) {

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < bits.length; i += 8) {
            byte byteValue = 0;
            for (int j = 0; j < 8; j++) {
                byteValue |= (byte) (bits[i + j] << (7 - j));
            }
            //if(byteValue != 0)
            text.append((char) (byteValue & 0xFF));

        }

        return text.toString();
    }


    /**
     * A subsidiary function which prints the block of bits(8 bits)
     * @param oneBlock bits we want to print out
     */
    public void printOneBlock(byte[] oneBlock) {
        for (int i = 0; i < oneBlock.length; i++) {
            if (i % 8 == 0) {
                System.out.println();
            }
            System.out.print(oneBlock[i] + " ");
        }
    }
}
