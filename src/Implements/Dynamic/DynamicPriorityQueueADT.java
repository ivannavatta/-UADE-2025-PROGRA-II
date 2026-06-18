package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Implements.Dynamic.Nodes.PriorityNode;
import Interfaces.PriorityQueueADT;

public class DynamicPriorityQueueADT implements PriorityQueueADT {
    private PriorityNode first;

    @Override
    public void add(int value, int priority) {
        PriorityNode newNode = new PriorityNode(value, priority);

        if (first == null || priority < first.priority) {
            newNode.next = first;
            first = newNode;
        } else {
            PriorityNode current = first;
            while (current.next != null && current.next.priority >= priority) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        first = first.next;
    }

    @Override
    public int getElement() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        return first.value;
    }

    @Override
    public int getPriority() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        return first.priority;
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }
}
