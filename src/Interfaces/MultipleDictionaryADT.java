package Interfaces;

public interface MultipleDictionaryADT {
    SetADT getKeys();
    int[] get(int key);
    void add(int key, int value);
    void remove(int key);
    void remove(int key, int value);
    boolean isEmpty();
}
