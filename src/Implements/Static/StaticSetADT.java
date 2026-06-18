package Implements.Static;

import Exceptions.EmptyStructureException;
import Exceptions.FullStructureException;
import Interfaces.SetADT;

public class StaticSetADT implements SetADT {
    private int[] elements;
    private int size;
    private final int capacity;

    public StaticSetADT(int capacity) {
        this.capacity = capacity;
        this.elements = new int[capacity];
        this.size = 0;
    }

    @Override
    public boolean exist(int value) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == value) return true;
        }
        return false;
    }

    @Override
    public int choose() {
        if (isEmpty()) throw new EmptyStructureException();
        int index = (int) (System.currentTimeMillis() % size);
        return elements[index];
    }

    @Override
    public void add(int value) {
        if (size == capacity) throw new FullStructureException();
        if (exist(value)) return; // no se permiten repetidos
        elements[size] = value;
        size++;
    }

    @Override
    public void remove(int element) {
        if (isEmpty()) throw new EmptyStructureException();
        for (int i = 0; i < size; i++) {
            if (elements[i] == element) {
                // Mueve el último al lugar eliminado
                elements[i] = elements[size - 1];
                size--;
                return;
            }
        }
        // Si no existe, no hace nada
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
