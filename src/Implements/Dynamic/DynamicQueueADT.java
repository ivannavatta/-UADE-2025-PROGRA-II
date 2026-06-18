package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Implements.Dynamic.Nodes.Node;
import Interfaces.QueueADT;

public class DynamicQueueADT implements QueueADT {
    private Node first;
    private Node last;

    public DynamicQueueADT() {
        this.first = null;
        this.last = null;
    }

    @Override
    public void add(int value) {
        Node newNode = new Node(value);
        if (isEmpty()) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        first = first.next;
        if (first == null) {
            last = null;
        }
    }

    @Override
    public int getElement() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        return first.value;
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }
}
