package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Implements.Dynamic.Nodes.Node;
import Interfaces.StackADT;

public class DynamicStackADT implements StackADT {
    private Node<Integer> top;

    @Override
    public void add(int value) {
        Node newNode = new Node(value);
        newNode.next = top;
        top = newNode;
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        top = top.next;
    }

    @Override
    public int getElement() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        return top.value;
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }
}
