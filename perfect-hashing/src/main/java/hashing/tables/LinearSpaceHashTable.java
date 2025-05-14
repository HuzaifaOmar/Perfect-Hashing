package hashing.tables;

import java.util.ArrayList;
import java.util.List;

import hashing.functions.IHashFunction;
import hashing.functions.MatrixHashFunction;

public class LinearSpaceHashTable implements IPerfectHashTable {

    private IHashFunction primaryHashFunction;
    private QuadraticSpaceHashTable[] subTables;
    private int capacity;
    private int currentSize;
    private int rebuildAttempts;
    private static final int DEFAULT_KEY_BITS = 128;

    @Override
    public int build(List<String> keys) {
        if (keys == null) {
            throw new IllegalArgumentException("Keys cannot be null");
        }

        this.capacity = keys.size();
        this.subTables = new QuadraticSpaceHashTable[capacity];
        this.rebuildAttempts = -1;
        this.currentSize = 0;

        boolean success = false;
        while (!success) {
            success = true;
            rebuildAttempts++;

            // Initialize temporary buckets for key distribution
            List<List<String>> tempBuckets = new ArrayList<>(capacity);
            for (int i = 0; i < capacity; i++) {
                tempBuckets.add(new ArrayList<>());
            }

            primaryHashFunction = new MatrixHashFunction(capacity, DEFAULT_KEY_BITS);

            // Distribute keys to buckets
            for (String key : keys) {
                int bucketIndex = Math.abs(primaryHashFunction.hash(key) % capacity);
                tempBuckets.get(bucketIndex).add(key);
            }

            // Build each sub-table
            for (int i = 0; i < capacity; i++) {
                List<String> bucketKeys = tempBuckets.get(i);
                subTables[i] = new QuadraticSpaceHashTable();

                try{
                    subTables[i].build(bucketKeys);
                } catch (RuntimeException e) {
                    // If a rebuild fails, mark success as false and break
                    success = false;
                    break;
                }
            }
        }

        currentSize = keys.size();
        return rebuildAttempts;
    }

    @Override
    public boolean insert(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (search(key)) {
            return false; // Key already exists
        }

        // Check if we need to resize
        if (currentSize >= capacity) {
            // Double the capacity and rebuild
            List<String> allKeys = getAllKeys();
            allKeys.add(key);
            if (capacity == 0) {
                capacity = 1;
            } else {
                capacity *= 2;
            }
            build(allKeys);
            return true;
        }

        // First check if key would cause collision in existing structure
        int tableIndex = Math.abs(primaryHashFunction.hash(key) % capacity);

        // Initialize the sub-table if it's null
        if (subTables[tableIndex] == null) {
            subTables[tableIndex] = new QuadraticSpaceHashTable();
            subTables[tableIndex].build(new ArrayList<>());
        }

        // Try to insert into the sub-table
        boolean inserted = subTables[tableIndex].insert(key);

        // If insertion fails, rebuild the sub-table only
        if (!inserted) {
            List<String> bucketKeys = subTables[tableIndex].toList();
            bucketKeys.add(key);
            subTables[tableIndex] = new QuadraticSpaceHashTable();
            int attempts = subTables[tableIndex].build(bucketKeys);

            // If too many attempts, rebuild the entire table
            if (attempts > 100) {
                List<String> allKeys = getAllKeys();
                allKeys.add(key);
                build(allKeys);
            }
        }

        currentSize++;
        return true;
    }

    // Helper method to get all keys currently in the table
    private List<String> getAllKeys() {
        List<String> allKeys = new ArrayList<>();
        for (QuadraticSpaceHashTable subTable : subTables) {
            if (subTable != null) {
                allKeys.addAll(subTable.toList());
            }
        }
        return allKeys;
    }

    @Override
    public boolean delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (!search(key)) {
            return false; // Key not found
        }

        int tableIndex = Math.abs(primaryHashFunction.hash(key) % capacity);
        boolean deleted = subTables[tableIndex].delete(key);

        if (deleted) {
            currentSize--;
        }

        return deleted;
    }

    @Override
    public boolean search(String key) {
        if (key == null || capacity == 0) {
            return false;
        }

        int tableIndex = Math.abs(primaryHashFunction.hash(key) % capacity);
        return subTables[tableIndex] != null && subTables[tableIndex].search(key);
    }

    @Override
    public int getSpace() {
        int totalSpace = capacity;
        for (QuadraticSpaceHashTable subTable : subTables) {
            if (subTable != null) {
                totalSpace += subTable.getSpace();
            }
        }
        return totalSpace;
    }

    @Override
    public int size() {
        return currentSize;
    }

    public int getRebuildAttempts() {
        return rebuildAttempts;
    }

}
