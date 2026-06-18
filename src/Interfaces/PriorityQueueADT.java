package Interfaces;

public interface PriorityQueueADT {

    int getElement();

    int getPriority();

    void add(int value, int priority);

    void remove();

    boolean isEmpty();
}
