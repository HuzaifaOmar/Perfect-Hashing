package dictionary;

import hashing.tables.IPerfectHashTable;
import hashing.tables.LinearSpaceHashTable;
import hashing.tables.QuadraticSpaceHashTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnglishDictionary implements IDictionary {
    private IPerfectHashTable hashTable;

    public EnglishDictionary(String type) {
        if (type.equalsIgnoreCase("quadratic"))
            hashTable = new QuadraticSpaceHashTable();
        else if (type.equalsIgnoreCase("linear"))
            hashTable = new LinearSpaceHashTable();
        else
            throw new IllegalArgumentException("Unknown hash table type: " + type);
    }

    /*
     * !
     * ! I DO NOT KNOW IF WORDS ARE KEY SENSITIVE OR NOT
     * !
     */

    @Override
    public void build() {
        List<String> keys = new ArrayList<>();
        hashTable.build(keys);
    }

    @Override
    public boolean insert(String word) {
        System.out.println("inserting: " + word);
        if (word == null || word.isEmpty()) {
            return false;
        }
        return hashTable.insert(word);
    }

    @Override
    public boolean delete(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return hashTable.delete(word);
    }

    @Override
    public boolean search(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return hashTable.search(word);
    }

    @Override
    public int[] batchInsert(String filePath) {
        int[] result = new int[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                System.out.println(line);
                if (!line.isEmpty())
                    if (insert(line))
                        result[0]++;
                    else
                        result[1]++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int[] batchDelete(String filePath) {
        int[] result = new int[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty())
                    if (delete(line))
                        result[0]++;
                    else
                        result[1]++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int size() {
        return hashTable.size();
    }

    @Override
    public int getSpace() {
        return hashTable.getSpace();
    }
}
