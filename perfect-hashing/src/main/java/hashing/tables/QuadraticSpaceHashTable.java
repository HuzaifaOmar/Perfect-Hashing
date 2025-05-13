package hashing.tables;

import hashing.functions.IHashFunction;
import hashing.functions.MatrixHashFunction;

import java.util.ArrayList;
import java.util.List;

public class QuadraticSpaceHashTable implements IPerfectHashTable {

    private IHashFunction hashFunction;
    private List<String> table;
    private int size;

    private final String DELETED = "MARK_DELETED";
    private final float LOAD_FACTOR = 0.75f;

    @Override
    public int build(List<String> set) {
        this.size = Math.max(set.size(), 1);
        int NUM_OF_BITS = 300;
        
        int tableSize = size * size;
        
        int rebuildAttempts = -1;
        final int MAX_ATTEMPTS = 1000;
        
        boolean success = false;
        while (!success && rebuildAttempts < MAX_ATTEMPTS) {
            rebuildAttempts++;
            table = new ArrayList<>(tableSize);
            for (int i = 0; i < tableSize; i++)
                table.add("");

            hashFunction = new MatrixHashFunction(tableSize, NUM_OF_BITS);

            success = true;
            for (String key : set) {
                int idx = hashFunction.hash(key);
                if (idx >= tableSize) {
                    success = false;
                    break;
                }

                if (table.get(idx).isEmpty())
                    table.set(idx, key);
                else {
                    success = false;
                    break;
                }
            }
        }
        if (rebuildAttempts >= MAX_ATTEMPTS) {
            throw new RuntimeException("Failed to find a perfect hash function after " + 
                MAX_ATTEMPTS + " attempts. The key set may be too large or problematic.");
        }
        
        return rebuildAttempts;
    }

    @Override
    public boolean insert(String key) {
        int idx = mySearch(key);
        if (idx != -1)
            return false;
        if ((float) (size + 1) / table.size() >= LOAD_FACTOR) {
            List<String> list = this.toList();
            list.add(key);
            build(list);
            return true;
        }
        idx = hashFunction.hash(key);
        while (!table.get(idx).isEmpty() || table.get(idx).equals(DELETED)) {
            idx = (idx + 1) % table.size();
        }
        table.set(idx, key);
        size += 1;
        return true;
    }

    @Override
    public boolean delete(String key) {
        int idx = mySearch(key);
        if (idx == -1)
            return false;
        table.set(idx, DELETED);
        size -= 1;
        return true;
    }

    @Override
    public boolean search(String key) {
        if (hashFunction == null)
            return false;
        return mySearch(key) != -1;
    }

    public int mySearch(String key) {
        int idx = hashFunction.hash(key);
        int stIdx = idx;
        while (!table.get(idx).equals(key) && !table.get(idx).isEmpty()) {
            idx = (idx + 1) % table.size();
            if (idx == stIdx) {
                build(this.toList());
                return -1;
            }
        }
        if (!table.get(idx).isEmpty())
            return idx;
        return -1;
    }

    @Override
    public int getSpace() {
        return table.size();
    }

    @Override
    public int size() {
        return this.size;
    }

    public List<String> toList() {
        List<String> list = new ArrayList<>();
        for (String s : table) {
            if (!s.isEmpty())
                list.add(s);
        }
        return list;
    }

    // public static void main(String[] args) {
    // List<String> input = Arrays.asList("x", "y", "z");
    // QuadraticSpaceHashTable hashTable = new QuadraticSpaceHashTable();
    // hashTable.build(input);
    // hashTable.search("y");
    // boolean b = hashTable.delete("y");
    // boolean a = hashTable.search("y");
    // }
}
