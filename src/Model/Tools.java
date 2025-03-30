package Model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;


public class Tools {

    //STRING --> BITY

    public ArrayList<byte[]> stringToBits(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.US_ASCII); //konwersja string na bajty np. dla "abc" bytes = [97, 98, 99]

        ArrayList<byte[]> blocks = new ArrayList<>();

        int blocksAmt = (int) Math.ceil(bytes.length / 8.0); //ile bloków 64bit(8B) należy utworzyć

        System.out.println("dla " + text + " bytes= " + Arrays.toString(bytes));
        //System.out.println("Blocks amt: " + blocksAmt);

        for (int i = 0; i < blocksAmt; i++) {
            byte[] block = new byte[8];

            int lengthToCopy = Math.min(8, bytes.length - i * 8);
            System.arraycopy(bytes, i * 8, block, 0, lengthToCopy);

            if (lengthToCopy < 8) {
                for (int j = lengthToCopy; j < 8; j++) {
                    block[j] = 0;
                }
            }

            blocks.add(oneByteOneBit(block));
        }

        return blocks;
    }

    public byte[] oneByteOneBit(byte[] oneBlock) {
        byte[] block = new byte[64];

        for (int i = 0; i < 8; i++) {
            int b = oneBlock[i] & 0xFF;
            for (int j = 7; j >= 0; j--) {
                byte currentBit = (byte) ((b >> j) & 1);
                block[i * 8 + (7 - j)] = currentBit;
            }
        }
        return block;
    }



    //BITY --> STRING

    public String bitsToString(ArrayList<byte[]> blocks) {
        StringBuilder result = new StringBuilder();

        for (byte[] block : blocks) {
            result.append(oneByteOneBitToString(block));
        }

        return result.toString();
    }


    public String oneByteOneBitToString(byte[] oneBlock) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            byte byteValue = 0;


            for (int j = 0; j < 8; j++) {

                byteValue |= (byte) (oneBlock[i * 8 + j] << (7 - j));
            }
            int unsignedValue = byteValue < 0 ? byteValue + 128 : byteValue;

            if (unsignedValue != 0) {
                result.append((char) unsignedValue);
            }
        }

        return result.toString();
    }



    public void printOneBlock(byte[] oneBlock) {
        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0) {
                System.out.println();
            }
            System.out.print(oneBlock[i] + " ");
        }
    }

    public void printOneBlock2(byte[] oneBlock) {
        for (int i = 0; i < 64; i++) {
            System.out.print(oneBlock[i]);
        }
    }

}
