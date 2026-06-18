package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Implements.Dynamic.Nodes.SetNode;
import Interfaces.SetADT;

public class DynamicSetADT implements SetADT {
    private SetNode head;
    private int size;

    public DynamicSetADT() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public boolean exist(int value) {
        SetNode current = head;
        while (current != null) {
            if (current.value == value) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public int choose() {
        if (isEmpty()) throw new EmptyStructureException();
        // Recorre hasta un índice pseudo-aleatorio basado en el tamaño
        int index = (int) (System.currentTimeMillis() % size);
        SetNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    @Override
    public void add(int value) {
        if (exist(value)) return; // no se permiten repetidos
        SetNode newNode = new SetNode(value);
        newNode.next = head;
        head = newNode;
        size++;
    }

    @Override
    public void remove(int element) {
        if (isEmpty()) throw new EmptyStructureException();
        if (head.value == element) {
            head = head.next;
            size--;
            return;
        }
        SetNode current = head;
        while (current.next != null) {
            if (current.next.value == element) {
                current.next = current.next.next;
                size--;
                return;
            }
            current = current.next;
        }
        // Si no existe, no hace nada
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }
}
