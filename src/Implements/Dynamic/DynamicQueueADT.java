package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Implements.Dynamic.Nodes.Node;
import Interfaces.QueueADT;

public class DynamicQueueADT implements QueueADT {
    private Node first; // Para desencolar (remove)
    private Node last;  // Para encolar (add)

    public DynamicQueueADT() {
        this.first = null;
        this.last = null;
    }

    @Override
    public void add(int value) {
        Node newNode = new Node(value);
        if (isEmpty()) {
            first = newNode; // Si es el primero, es tanto el inicio como el fin
        } else {
            last.next = newNode; // El que era último ahora apunta al nuevo
        }
        last = newNode; // El nuevo pasa a ser el último oficialmente
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new EmptyStructureException(); // no podés sacar de una cola vacía
        }
        first = first.next;
        if (first == null) {
            last = null;
        }
    }

    @Override
    public int getElement() {
        if (isEmpty()) {
            throw new EmptyStructureException(); // no podés leer de una cola vacía
        }
        return first.value;
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }
}
