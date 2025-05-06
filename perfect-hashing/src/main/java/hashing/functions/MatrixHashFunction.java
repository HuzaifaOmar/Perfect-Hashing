package hashing.functions;

import java.util.Random;

public class MatrixHashFunction implements IHashFunction {
    /*
        1. we will need a function to convert a string into numerical vector
            ascii character is 127 -> 7 bits
        2. generate a matrix of size b x u
            where b = log2(tableSize)
            and u = maximum expected bits = length of max string * 7
     */
    private final int[][] matrix; // b x u
    private final int tableSize;
    private final int numBits; // b = log2(tableSize)
    private final int keyBits; // u (number of bits in key)

    public MatrixHashFunction(int tableSize, int keyBits) {
        this.tableSize = tableSize;
        this.keyBits = keyBits;
        this.numBits = Integer.SIZE - Integer.numberOfLeadingZeros(this.tableSize - 1);
        this.matrix = new int[numBits][keyBits];
        Random random = new Random();

        for (int i = 0; i < numBits; i++)
            for (int j = 0; j < keyBits; j++)
                matrix[i][j] = random.nextInt(2);
    }

    @Override
    public int hash(String key) {
        int[] bits = stringToBits(key, keyBits);
        int[] result = new int[numBits];

        for (int i = 0; i < numBits; i++)
            for (int j = 0; j < keyBits; j++)
                // XOR = addition modulo 2
                result[i] ^= matrix[i][j] & bits[j];

        int hash = 0;
        for (int i = 0; i < numBits; i++)
            hash |= (result[i] << i);
        return hash % tableSize;
    }

    @Override
    public IHashFunction generateNew() {
        return new MatrixHashFunction(tableSize, keyBits);
    }

    private int[] stringToBits(String key, int bitsSize) {
        int[] result = new int[bitsSize];

        for (int i = 0; i < key.length() && i * 7 < bitsSize; i++) {
            char c = key.charAt(i);
            for (int j = 0; j < 7 && i * 7 + j < bitsSize; j++)
                result[i * 7 + j] = (c >> j) & 1;
        }

        return result;
    }
}
