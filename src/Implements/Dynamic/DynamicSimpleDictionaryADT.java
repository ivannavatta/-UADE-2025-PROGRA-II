package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Exceptions.KeyNotFoundException;
import Implements.Dynamic.Nodes.DictionaryNode;
import Interfaces.SetADT;
import Interfaces.SimpleDictionaryADT;

public class DynamicSimpleDictionaryADT implements SimpleDictionaryADT {
    private DictionaryNode head;

    public DynamicSimpleDictionaryADT() {
        this.head = null;
    }

    @Override
    public void add(int key, int value) {
        // Si la clave ya existe, pisa el valor
        DictionaryNode current = head;
        while (current != null) {
            if (current.key == key) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        // Si no existe, agrega al frente
        DictionaryNode newNode = new DictionaryNode(key, value);
        newNode.next = head;
        head = newNode;
    }

    @Override
    public void remove(int key) {
        if (isEmpty()) throw new EmptyStructureException();
        if (head.key == key) {
            head = head.next;
            return;
        }
        DictionaryNode current = head;
        while (current.next != null) {
            if (current.next.key == key) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
        // Si no existe la clave, no hace nada
    }

    @Override
    public int get(int key) {
        if (isEmpty()) throw new EmptyStructureException();
        DictionaryNode current = head;
        while (current != null) {
            if (current.key == key) return current.value;
            current = current.next;
        }
        throw new KeyNotFoundException();
    }

    @Override
    public SetADT getKeys() {
        DynamicSetADT keys = new DynamicSetADT();
        DictionaryNode current = head;
        while (current != null) {
            keys.add(current.key);
            current = current.next;
        }
        return keys;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }
}
