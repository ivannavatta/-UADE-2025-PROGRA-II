package Implements.Static;

import Exceptions.FullStructureException;
import Exceptions.IndexOutOfBoundsException;
import Interfaces.LinkedListADT;

public class StaticLinkedListADT implements LinkedListADT {
    private int[] array;
    private int count;

    public StaticLinkedListADT() {
        this.array = new int[100]; // Tamaño por defecto
        this.count = 0;
    }

    @Override
    public void add(int value) {
        if (count >= array.length) {
            throw new FullStructureException(); // la lista estática está llena
        }
        array[count] = value;
        count++;
    }

    @Override
    public void insert(int index, int value) {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException(); // índice inválido
        }
        if (count >= array.length) {
            throw new FullStructureException(); // la lista estática está llena
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
            throw new IndexOutOfBoundsException(); // índice inválido
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
            throw new IndexOutOfBoundsException(); // índice inválido
        }
        for (int i = index; i < count - 1; i++) {
            array[i] = array[i + 1];
        }
        count--;
    }
}
