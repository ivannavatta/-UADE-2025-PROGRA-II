package Implements.Static;

import Exceptions.EmptyStructureException;
import Exceptions.FullStructureException;
import Exceptions.KeyNotFoundException;
import Interfaces.MultipleDictionaryADT;
import Interfaces.SetADT;

public class StaticMultipleDictionaryADT implements MultipleDictionaryADT {
    private int[] keys;
    private int[][] values;
    private int[] valueCount;
    private int size;            // cantidad de claves distintas
    private final int capacity;  // máximo de claves distintas
    private final int maxValues; // máximo de valores por clave

    public StaticMultipleDictionaryADT(int capacity, int maxValues) {
        this.capacity = capacity;
        this.maxValues = maxValues;
        this.keys = new int[capacity];
        this.values = new int[capacity][maxValues];
        this.valueCount = new int[capacity];
        this.size = 0;
    }

    // Retorna el índice de la clave, o -1 si no existe
    private int findKeyIndex(int key) {
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) return i;
        }
        return -1;
    }

    @Override
    public void add(int key, int value) {
        int index = findKeyIndex(key);
        if (index != -1) {
            // Clave existente: agrega el valor
            if (valueCount[index] == maxValues) throw new FullStructureException();
            values[index][valueCount[index]] = value;
            valueCount[index]++;
        } else {
            // Clave nueva
            if (size == capacity) throw new FullStructureException();
            keys[size] = key;
            values[size][0] = value;
            valueCount[size] = 1;
            size++;
        }
    }

    @Override
    public void remove(int key) {
        if (isEmpty()) throw new EmptyStructureException();
        int index = findKeyIndex(key);
        if (index == -1) return; // no existe, no hace nada
        // Mueve el último al lugar eliminado
        keys[index] = keys[size - 1];
        valueCount[index] = valueCount[size - 1];
        for (int i = 0; i < valueCount[index]; i++) {
            values[index][i] = values[size - 1][i];
        }
        size--;
    }

    @Override
    public void remove(int key, int value) {
        if (isEmpty()) throw new EmptyStructureException();
        int keyIndex = findKeyIndex(key);
        if (keyIndex == -1) return; // no existe la clave, no hace nada

        for (int i = 0; i < valueCount[keyIndex]; i++) {
            if (values[keyIndex][i] == value) {
                // Mueve el último valor al lugar eliminado
                values[keyIndex][i] = values[keyIndex][valueCount[keyIndex] - 1];
                valueCount[keyIndex]--;
                // Si no quedan valores, elimina la clave
                if (valueCount[keyIndex] == 0) remove(key);
                return;
            }
        }
        // Si el valor no existe, no hace nada
    }

    @Override
    public int[] get(int key) {
        if (isEmpty()) throw new EmptyStructureException();
        int index = findKeyIndex(key);
        if (index == -1) throw new KeyNotFoundException();

        int count = valueCount[index];
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = values[index][i];
        }
        return result;
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
