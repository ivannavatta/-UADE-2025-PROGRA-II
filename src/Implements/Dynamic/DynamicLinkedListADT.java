package Implements.Dynamic;

import Exceptions.IndexOutOfBoundsException;
import Implements.Dynamic.Nodes.Node;
import Interfaces.LinkedListADT;

public class DynamicLinkedListADT implements LinkedListADT {
    private Node first; // Puntero al inicio de la lista
    private int count;  // Contador de elementos

    public DynamicLinkedListADT() {
        this.first = null;
        this.count = 0;
    }

    @Override
    public void add(int value) {
        Node newNode = new Node(value);
        if (first == null) {
            first = newNode;
        } else {
            Node current = first;
            while (current.next != null) { // Recorremos hasta el final
                current = current.next;
            }
            current.next = newNode;
        }
        count++;
    }

    @Override
    public void insert(int index, int value) {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException(); // índice inválido
        }
        Node newNode = new Node(value);
        if (index == 0) {
            newNode.next = first;
            first = newNode;
        } else {
            Node current = first;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        count++;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException(); // índice inválido
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException(); // índice inválido
        }
        if (index == 0) {
            first = first.next;
        } else {
            Node current = first;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
        count--;
    }
}
