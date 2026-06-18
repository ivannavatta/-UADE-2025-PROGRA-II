package Implements.Static;

import Exceptions.FullStructureException;
import Exceptions.IndexOutOfBoundsException;
import Interfaces.LinkedListADT;

public class StaticLinkedListADT implements LinkedListADT {
    private int[] array;
    private int count;

    public StaticLinkedListADT() {
        this.array = new int[100];
        this.count = 0;
    }

    @Override
    public void add(int value) {
        if (count >= array.length) {
            throw new FullStructureException();
        }
        array[count] = value;
        count++;
    }

    @Override
    public void insert(int index, int value) {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException();
        }
        if (count >= array.length) {
            throw new FullStructureException();
        }
        for (int i = count; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = value;
        count++;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = index; i < count - 1; i++) {
            array[i] = array[i + 1];
        }
        count--;
    }
}
