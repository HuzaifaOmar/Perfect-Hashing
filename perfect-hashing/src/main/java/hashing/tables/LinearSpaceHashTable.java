package hashing.tables;

import java.util.ArrayList;
import java.util.List;

import hashing.functions.IHashFunction;
import hashing.functions.MatrixHashFunction;

public class LinearSpaceHashTable implements IPerfectHashTable {

    private IHashFunction primaryHashFunction;
    private Bucket[] buckets;
    private int capacity;
    private int currentSize;
    private int rebuildAttempts;
    private static final int DEFAULT_KEY_BITS = 128;

    public class Bucket {
        IHashFunction hashFunction;
        String[] table;
        int size;
        List<String> keys;

        Bucket(int expectedSize) {
            this.size = expectedSize * expectedSize;
            this.keys = new ArrayList<>();
            this.table = new String[Math.max(1, size)];
        }

        boolean build() {
            if (keys.isEmpty())
                return true;

            boolean success = false;
            int attempts = 0;
            int maxAttempts = 100; // Prevent infinite loops

            while (!success && attempts < maxAttempts) {
                attempts++;
                hashFunction = new MatrixHashFunction(size, DEFAULT_KEY_BITS);
                table = new String[size];
                success = true;

                for (String key : keys) {
                    int index = Math.abs(hashFunction.hash(key));
                    if (table[index] != null) {
                        success = false;
                        break;
                    }
                    table[index] = key;
                }
            }
            return success;
        }

        boolean contains(String key) {
            if (keys.isEmpty() || key == null)
                return false;
            int index = Math.abs(hashFunction.hash(key));
            return key.equals(table[index]);
        }

        // Check if inserting a key would cause a collision
        boolean wouldCollide(String key) {
            if (keys.isEmpty() || key == null)
                return false;
            if (keys.contains(key))
                return false; // Not a collision if the key already exists

            int index = Math.abs(hashFunction.hash(key));
            return table[index] != null;
        }
    }

    @Override
    public int build(List<String> keys) {
        if (keys == null) {
            throw new IllegalArgumentException("Keys cannot be null");
        }
        // Use 2n capacity instead of n
        this.capacity = Math.max(1, keys.size() * 2);
        this.buckets = new Bucket[capacity];
        this.rebuildAttempts = 0;
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
                int bucketIndex = Math.abs(primaryHashFunction.hash(key));
                tempBuckets.get(bucketIndex).add(key);
            }

            // Build each bucket
            for (int i = 0; i < capacity; i++) {
                List<String> bucketKeys = tempBuckets.get(i);
                buckets[i] = new Bucket(bucketKeys.size());
                buckets[i].keys.addAll(bucketKeys);

                if (!buckets[i].build()) {
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

        // First check if key would cause collision in existing structure
        int bucketIndex = Math.abs(primaryHashFunction.hash(key));

        // Check if the bucket has a valid hash function
        if (buckets[bucketIndex].hashFunction == null) {
            // Initialize the bucket's hash function if needed
            buckets[bucketIndex].build();
        }

        // Check for collision
        if (buckets[bucketIndex].keys.isEmpty() || buckets[bucketIndex].wouldCollide(key)) {
            // Collision detected or empty bucket needs to be rebuilt, need to rebuild with
            // new key
            List<String> allKeys = new ArrayList<>();
            for (Bucket bucket : buckets) {
                allKeys.addAll(bucket.keys);
            }
            allKeys.add(key);
            build(allKeys);
        } else {
            // No collision, can directly insert
            buckets[bucketIndex].keys.add(key);
            int index = Math.abs(buckets[bucketIndex].hashFunction.hash(key));
            buckets[bucketIndex].table[index] = key;
            currentSize++;
        }
        return true;
    }

    @Override
    public boolean delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        List<String> allKeys = new ArrayList<>();
        boolean found = false;

        for (Bucket bucket : buckets) {
            for (String k : bucket.keys) {
                if (k.equals(key)) {
                    found = true;
                } else {
                    allKeys.add(k);
                }
            }
        }

        if (!found) {
            return false; // Key not found
        }

        build(allKeys);
        return true;
    }

    @Override
    public boolean search(String key) {
        if (key == null || capacity == 0) {
            return false;
        }

        int bucketIndex = Math.abs(primaryHashFunction.hash(key));
        return buckets[bucketIndex].contains(key);
    }

    @Override
    public int getSpace() {
        int totalSpace = capacity;
        for (Bucket bucket : buckets) {
            totalSpace += bucket.size;
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
