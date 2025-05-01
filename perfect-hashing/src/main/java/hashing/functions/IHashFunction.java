package hashing.functions;

public interface IHashFunction {
    int hash(String key);

    IHashFunction generateNew();
}
