package Implements.Static;

import Exceptions.EmptyStructureException;
import Exceptions.FullStructureException;
import Interfaces.QueueADT;

public class StaticQueueADT implements QueueADT {
    private int[] array;
    private int first;
    private int last;
    private int count; // Usamos un contador para facilitar isEmpty y size

    public StaticQueueADT() {
        this.array = new int[100];
        this.first = 0;
        this.last = 0;
        this.count = 0;
    }

    @Override
    public void add(int value) {
        if (count >= array.length) {
            throw new FullStructureException(); // la cola estática está llena
        }
        array[last] = value;
        last = (last + 1) % array.length;
        count++;
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new EmptyStructureException(); // no podés sacar de una cola vacía
        }
        first = (first + 1) % array.length;
        count--;
    }

    @Override
    public int getElement() {
        if (isEmpty()) {
            throw new EmptyStructureException(); // no podés leer de una cola vacía
        }
        return array[first];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }
}
