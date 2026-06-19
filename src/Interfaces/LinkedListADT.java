package Interfaces;


public interface LinkedListADT<T> {

    void add(T value);

    void insert(int index, T value);

    void remove(int index);

    T get(int index);

    int size();

    boolean isEmpty();
}
