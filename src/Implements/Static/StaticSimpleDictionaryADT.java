package Implements.Static;

import Exceptions.EmptyStructureException;
import Exceptions.FullStructureException;
import Exceptions.KeyNotFoundException;
import Interfaces.SetADT;
import Interfaces.SimpleDictionaryADT;

public class StaticSimpleDictionaryADT implements SimpleDictionaryADT {
    private int[] keys;
    private int[] values;
    private int size;
    private final int capacity;

    public StaticSimpleDictionaryADT(int capacity) {
        this.capacity = capacity;
        this.keys = new int[capacity];
        this.values = new int[capacity];
        this.size = 0;
    }

    private int findIndex(int key) {
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) return i;
        }
        return -1;
    }

    @Override
    public void add(int key, int value) {
        int index = findIndex(key);
        if (index != -1) {
            values[index] = value;
        } else {
            if (size == capacity) throw new FullStructureException();
            keys[size] = key;
            values[size] = value;
            size++;
        }
    }

    @Override
    public void remove(int key) {
        if (isEmpty()) throw new EmptyStructureException();
        int index = findIndex(key);
        if (index == -1) return;
        keys[index] = keys[size - 1];
        values[index] = values[size - 1];
        size--;
    }

    @Override
    public int get(int key) {
        if (isEmpty()) throw new EmptyStructureException();
        int index = findIndex(key);
        if (index == -1) throw new KeyNotFoundException();
        return values[index];
    }

    @Override
    public SetADT getKeys() {
        StaticSetADT set = new StaticSetADT(size == 0 ? 1 : size);
        for (int i = 0; i < size; i++) {
            set.add(keys[i]);
        }
        return set;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
