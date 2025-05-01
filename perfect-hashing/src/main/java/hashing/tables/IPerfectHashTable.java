package hashing.tables;

import java.util.List;

public interface IPerfectHashTable {

    int build(List<String> keys);

    boolean insert(String key);

    /**
     * @return true if deletion was successful, false if the key was not found
     */
    boolean delete(String key);

    /**
     * @return true if key exists
     */
    boolean search(String key);

    /**
     * Gets the actual space used by the hash table (total number of slots)
     *
     * @return Total number of slots in the hash table
     */
    int getSpace();

    /**
     * Gets the number of keys stored in the hash table
     *
     * @return Number of keys
     */
    int size();

}