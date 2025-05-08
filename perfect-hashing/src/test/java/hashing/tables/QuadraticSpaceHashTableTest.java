package hashing.tables;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuadraticSpaceHashTableTest {

    private QuadraticSpaceHashTable hashTable;

    @BeforeEach
    public void setUp() {
        hashTable = new QuadraticSpaceHashTable();
    }

    @org.junit.jupiter.api.Test
    public void testBuildCorrectness() {
        List<String> input = Arrays.asList("apple", "banana", "cherry", "date");
        hashTable.build(input);

        for (String s : input) {
            Assertions.assertTrue(hashTable.search(s), "Failed to find: " + s);
        }

        Assertions.assertEquals(input.size(), hashTable.size());
    }

    @org.junit.jupiter.api.Test
    public void testInsertAndSearch() {
        List<String> input = Arrays.asList("a", "b", "c");
        hashTable.build(input);
        Assertions.assertTrue(hashTable.insert("d"));
        Assertions.assertTrue(hashTable.search("d"));
    }

    @org.junit.jupiter.api.Test
    public void testDelete() {

        List<String> input = Arrays.asList("x", "y", "z");
        hashTable.build(input);
        Assertions.assertTrue(hashTable.search("y"));
        Assertions.assertTrue(hashTable.delete("y"));
        Assertions.assertFalse(hashTable.search("y"));
    }
    @org.junit.jupiter.api.Test
    public void testRebuildAfterInsertOverLoadFactor() {
        List<String> input = Arrays.asList("key1", "key2", "key3");
        hashTable.build(input);
        for (int i = 4; i < 50; i++) {
            Assertions.assertTrue(hashTable.insert("key" + i));
        }

        for (int i = 1; i < 50; i++) {
            Assertions.assertTrue(hashTable.search("key" + i));
        }
    }

    @org.junit.jupiter.api.Test
    public void testToList() {
        List<String> input = Arrays.asList("lion", "tiger", "bear");
        hashTable.build(input);
        List<String> out = hashTable.toList();
        Assertions.assertTrue(out.containsAll(input));
        Assertions.assertEquals(input.size(), out.size());
    }

    @org.junit.jupiter.api.Test
    public void testPerformanceLargeDataset() {
        List<String> input = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            input.add("word" + i);
        }

        long startTime = System.nanoTime();
        hashTable.build(input);
        long endTime = System.nanoTime();

        System.out.printf("Build time for 5000 entries: %.2f ms%n", (endTime - startTime) / 1e6);

        for (int i = 0; i < 5000; i++) {
            Assertions.assertTrue(hashTable.search("word" + i));
        }

        Assertions.assertEquals(5000, hashTable.size());
        Assertions.assertTrue(hashTable.getSpace() <= 5000 * 5000); // quadratic space bound
    }
}
