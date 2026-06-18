package Interfaces;

public interface SetADT {
    boolean exist(int value);

    int choose();

    void add(int value);

    void remove(int element);

    boolean isEmpty();
}
