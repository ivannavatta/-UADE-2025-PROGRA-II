package Implements.Static;

import Exceptions.EmptyStructureException;
import Exceptions.FullStructureException;
import Interfaces.QueueADT;

public class StaticQueueADT implements QueueADT {
    private int[] array;
    private int first;
    private int last;
    private int count;

    public StaticQueueADT() {
        this.array = new int[100];
        this.first = 0;
        this.last = 0;
        this.count = 0;
    }

    @Override
    public void add(int value) {
        if (count >= array.length) {
            throw new FullStructureException();
        }
        array[last] = value;
        last = (last + 1) % array.length;
        count++;
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        first = (first + 1) % array.length;
        count--;
    }

    @Override
    public int getElement() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        return array[first];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }
}
