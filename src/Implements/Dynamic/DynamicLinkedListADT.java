package Implements.Dynamic;

import Exceptions.IndexOutOfBoundsException;
import Implements.Dynamic.Nodes.Node;
import Interfaces.LinkedListADT;

public class DynamicLinkedListADT<T> implements LinkedListADT<T> {
    private Node<T> first;
    private int count;

    public DynamicLinkedListADT() {
        this.first = null;
        this.count = 0;
    }

    @Override
    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (first == null) {
            first = newNode;
        } else {
            Node<T> current = first;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        count++;
    }

    @Override
    public void insert(int index, T value) {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> newNode = new Node<>(value);
        if (index == 0) {
            newNode.next = first;
            first = newNode;
        } else {
            Node<T> current = first;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        count++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = first;
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
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            first = first.next;
        } else {
            Node<T> current = first;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
        count--;
    }
}