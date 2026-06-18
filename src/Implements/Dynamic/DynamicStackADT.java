package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Implements.Dynamic.Nodes.Node;
import Interfaces.StackADT;

public class DynamicStackADT implements StackADT {
    private Node top; // Referencia al nodo de arriba de todo

    @Override
    public void add(int value) {
        Node newNode = new Node(value);
        newNode.next = top; // El nuevo apunta al que antes era el primero
        top = newNode; // Ahora el nuevo es el tope
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new EmptyStructureException(); // no podés sacar de una pila vacía
        }
        top = top.next;
    }

    @Override
    public int getElement() {
        if (isEmpty()) {
            throw new EmptyStructureException(); // no podés leer de una pila vacía
        }
        return top.value;
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }
}
