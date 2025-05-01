package hashing.tables;

import java.util.List;

public class QuadraticSpaceHashTable implements IPerfectHashTable{
    @Override
    public int build(List<String> keys) {
        return 0;
    }

    @Override
    public boolean insert(String key) {
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    @Override
    public boolean search(String key) {
        return false;
    }

    @Override
    public int getSpace() {
        return 0;
    }

    @Override
    public int size() {
        return 0;
    }
}
