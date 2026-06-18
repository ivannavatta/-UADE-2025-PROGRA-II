package Implements.Static;

import Exceptions.EmptyStructureException;
import Exceptions.FullStructureException;
import Interfaces.PriorityQueueADT;

public class StaticPriorityQueueADT implements PriorityQueueADT {
    private int[] values;
    private int[] priorities;
    private int count;

    public StaticPriorityQueueADT() {
        this.values = new int[100];
        this.priorities = new int[100];
        this.count = 0;
    }

    @Override
    public void add(int value, int priority) {
        if (count >= values.length) {
            throw new FullStructureException();
        }
        int index = 0;
        while (index < count && priorities[index] >= priority) {
            index++;
        }
        for (int i = count; i > index; i--) {
            values[i] = values[i - 1];
            priorities[i] = priorities[i - 1];
        }
        values[index] = value;
        priorities[index] = priority;
        count++;
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        for (int i = 0; i < count - 1; i++) {
            values[i] = values[i + 1];
            priorities[i] = priorities[i + 1];
        }
        count--;
    }

    @Override
    public int getElement() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        return values[0];
    }

    @Override
    public int getPriority() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        return priorities[0];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }
}
