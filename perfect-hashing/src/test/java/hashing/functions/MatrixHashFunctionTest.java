package hashing.functions;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class MatrixHashFunctionTest {

    @Test
    public void testHashInRange() {
        int tableSize = 16;
        IHashFunction hashFunction = new MatrixHashFunction(tableSize, 64);

        String[] testStrings = {"apple", "banana", "cherry", "pizza", "grape", "honey", "lemon", "black", "white"};

        for (String testString : testStrings) {
            int hash = hashFunction.hash(testString);
            assert (hash >= 0 && hash < tableSize);
        }
    }

    @Test
    public void testDifferentStringsHash() {
        int tableSize = 64;
        IHashFunction hashFunction = new MatrixHashFunction(tableSize, 64);
        String[] testStrings = {"apple", "banana", "cherry", "pizza", "grape", "honey", "lemon", "black", "white"};

        Map<Integer, String> hashedValues = new HashMap<>();

        for (String str : testStrings) {
            int hash = hashFunction.hash(str);
            hashedValues.put(hash, str);
        }

        // more than half of the strings should have different hashes
        assertTrue(hashedValues.size() > testStrings.length / 2);
    }

    @Test
    public void testSameStringHashesSame() {
        int tableSize = 64;
        IHashFunction hashFunction = new MatrixHashFunction(tableSize, 64);
        String testString = "testString";
        int hash1 = hashFunction.hash(testString);
        int hash2 = hashFunction.hash(testString);
        assertTrue(hash1 == hash2);
    }

    @Test
    public void testGenerateNewCreatesDistinctFunction() {
        int tableSize = 64;
        IHashFunction hashFunction1 = new MatrixHashFunction(tableSize, 64);
        IHashFunction hashFunction2 = hashFunction1.generateNew();

        int differentHashCount = 0;
        int testCount = 20;
        for (int i = 0; i < testCount; i++) {
            String testString = "testString" + i;
            int hash1 = hashFunction1.hash(testString);
            int hash2 = hashFunction2.hash(testString);
            if (hash1 != hash2) differentHashCount++;
        }

        assertTrue("Different hash functions should produce different outputs for at least one string", differentHashCount > testCount / 2);
    }
}
