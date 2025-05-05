package hashing.tables;

import hashing.functions.IHashFunction;
import hashing.functions.MatrixHashFunction;

import java.util.ArrayList;
import java.util.List;

public class QuadraticSpaceHashTable implements IPerfectHashTable{

    private IHashFunction hashFunction;
    private List<String> table;
    private int size;

    private final String DELETED = "MARK_DELETED";
    private final float LOAD_FACTOR = 0.75f;

    @Override
    public int build(List<String> set) {
        this.size = set.size();
        int NUM_OF_BITS = 300;
        table = new ArrayList<>(size * size);
        for(int i = 0; i < size * size; ++i){
            table.add("");
        }
        hashFunction = new MatrixHashFunction(size, NUM_OF_BITS);

        for (int i = 0; i < size; ++i){
            int idx = hashFunction.hash(set.get(i));
            if(table.get(idx).isEmpty())
                table.set(idx, set.get(i));
            else
                build(set);
        }
        return 0;
    }

    @Override
    public boolean insert(String key) {
        if( (float)(size +  1) / table.size() >= LOAD_FACTOR){
            List<String> list = this.toList();
            list.add(key);
            build(list);
            return true;
        }
        int idx = hashFunction.hash(key);
        while (!table.get(idx).isEmpty() || table.get(idx).equals(DELETED)){
            idx = (idx + 1) % table.size();
        }
        table.set(idx, key);
        size += 1;
        return true;
    }

    @Override
    public boolean delete(String key) {
        int idx = mySearch(key);
        if(idx == -1)
            return false;
        table.set(idx, DELETED);
        size -= 1;
        return true;
    }

    @Override
    public boolean search(String key) {
        return mySearch(key) != -1;
    }

    public int mySearch(String key){
        int idx = hashFunction.hash(key);
        int stIdx = idx;
        while(!table.get(idx).equals(key) && !table.get(idx).isEmpty()){
            idx = (idx + 1) % table.size();
            if(idx == stIdx) {
                build(this.toList());
                return -1;
            }
        }
        return idx;
    }

    @Override
    public int getSpace() {
        return table.size();
    }

    @Override
    public int size() {
        return this.size;
    }

    public List<String> toList(){
        List<String> list = new ArrayList<>();
        for(String s : table){
            if(!s.isEmpty())
                    list.add(s);
        }
        return list;
    }
}
