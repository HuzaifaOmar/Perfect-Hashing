package hashing.tables;

import java.util.ArrayList;
import java.util.List;

public class LinearSpaceHashTable implements IPerfectHashTable {
    private List<String> table;
    private int capacity;
    private int currentSize;

    @Override
    public int build(List<String> keys) {
        capacity = keys.size() * 2; // Example: double the size of keys for simplicity
        table = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            table.add(null); // Initialize with null values
        }
        currentSize = 0;

        for (String key : keys) {
            insert(key);
        }
        return capacity;
    }

    @Override
    public boolean insert(String key) {
        if (currentSize >= capacity) {
            List<String> currentKeys = new ArrayList<>();
            for (String k : table) {
                if (k != null) {
                    currentKeys.add(k);
                }
            }
            currentKeys.add(key); // Add the new key to the list
            build(currentKeys); // Rebuild the table with the updated list of keys
            return true;
        }

        int hash = key.hashCode() % capacity;
        if (hash < 0) hash += capacity;

        while (table.get(hash) != null) {
            if (table.get(hash).equals(key)) {
                return false; // Key already exists
            }
            hash = (hash + 1) % capacity; // Linear probing
        }

        table.set(hash, key);
        currentSize++;
        return true;
    }

    @Override
    public boolean search(String key) {
        return getIdx(key) != -1;
    }

    @Override
    public boolean delete(String key) {
        int idx = getIdx(key);
        if (idx != -1) {
            table.set(idx, null);
            currentSize--;
            return true;
        }
        return false;
    }

    @Override
    public int getSpace() {
        return capacity;
    }

    @Override
    public int size() {
        return currentSize;
    }

    private int getIdx(String key) {
        if (capacity == 0) return -1;

        int hash = key.hashCode() % capacity;
        if (hash < 0) hash += capacity;

        int startHash = hash; // Remember where we started

        do {
            if (table.get(hash) != null && table.get(hash).equals(key)) {
                return hash;
            }
            hash = (hash + 1) % capacity; // Linear probing
        } while (hash != startHash); // Stop when we've checked the entire table

        return -1; // Key not found
    }
}
