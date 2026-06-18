package Implements.Static;

import Exceptions.EmptyStructureException;
import Exceptions.FullStructureException;
import Interfaces.StackADT;

public class StaticStackADT implements StackADT {
    private int[] array;
    private int top;

    public StaticStackADT() {
        this.array = new int[100];
        this.top = -1;
    }

    @Override
    public void add(int value) {
        if (top >= array.length - 1) {
            throw new FullStructureException();
        }
        array[++top] = value;
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        top--;
    }

    @Override
    public int getElement() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        return array[top];
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }
}
