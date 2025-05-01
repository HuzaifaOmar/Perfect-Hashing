package dictionary;

public interface IDictionary {

    boolean insert(String word);

    boolean delete(String word);

    boolean search(String word);

    /**
     * @return int[2] array with [0]=number of words inserted, [1]=number of duplicates
     */
    int[] batchInsert(String filePath);

    /**
     * @return int[2] array with [0]=number of words deleted, [1]=number not found
     */
    int[] batchDelete(String filePath);

    int size();

    int getSpace();
}
