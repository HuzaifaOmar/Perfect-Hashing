package dictionary;

import hashing.tables.IPerfectHashTable;
import hashing.tables.LinearSpaceHashTable;
import hashing.tables.QuadraticSpaceHashTable;

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
     !
     ! I DO NOT KNOW IF WORDS ARE KEY SENSITIVE OR NOT
     !
     */

    @Override
    public boolean insert(String word) {
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
        return new int[0];
    }

    @Override
    public int[] batchDelete(String filePath) {
        return new int[0];
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
