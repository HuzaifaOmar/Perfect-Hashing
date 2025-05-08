package hashing.tables;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LinearSpaceHashTableTest {

    @Test
    public void testEmptyTable() {
        IPerfectHashTable table = new LinearSpaceHashTable();
        assertEquals(0, table.size());
    }

    @Test
    public void testBuildWithEmptyList() {
        IPerfectHashTable table = new LinearSpaceHashTable();
        table.build(new ArrayList<>());
        assertEquals(0, table.size());
    }

    @Test
    public void testBuildWithSingleElement() {
        IPerfectHashTable table = new LinearSpaceHashTable();
        List<String> words = Arrays.asList("apple");
        table.build(words);
        assertEquals(1, table.size());
        assertTrue(table.search("apple"));
        assertTrue(table.getSpace() >= 1);
    }

    @Test
    public void testBuildWithMultipleElements() {
        IPerfectHashTable table = new LinearSpaceHashTable();
        List<String> words = Arrays.asList("apple", "banana", "cherry", "pizza", "grape", "honey", "lemon", "black", "white");
        table.build(words);
        assertEquals(9, table.size());
        assertTrue(table.getSpace() >= 9);
        for (String word : words)
            assertTrue(table.search(word));
    }

    @Test
    public void testInsertDelete() {
        IPerfectHashTable table = new LinearSpaceHashTable();
        table.build(new ArrayList<>());
        assertTrue(table.insert("apple"));
        assertTrue(table.insert("banana"));
        assertEquals(2, table.size());

        assertFalse(table.insert("apple"));

        assertTrue(table.delete("apple"));
        assertEquals(1, table.size());
        assertFalse(table.delete("apple"));
    }
}
