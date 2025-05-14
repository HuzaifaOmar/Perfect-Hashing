package hashing.functions;

import java.util.Random;

public class FastHashFunction implements IHashFunction {
    private final int seed;
    private final int tableSize;
    private static final Random random = new Random();

    public FastHashFunction(int tableSize) {
        this.tableSize = tableSize;
        this.seed = random.nextInt(Integer.MAX_VALUE);
    }
    @Override
    public int hash(String key) {
        int h = seed;
        for (int i = 0; i < key.length(); i++) {
            h ^= key.charAt(i);
            h *= 0x5bd1e995;
            h ^= h >>> 13;
        }
        h ^= h >>> 15;
        return Math.abs(h) % tableSize;
    }

    @Override
    public IHashFunction generateNew() {
        return new FastHashFunction(tableSize);
    }
}
