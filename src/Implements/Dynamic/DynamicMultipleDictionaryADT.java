package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Exceptions.KeyNotFoundException;
import Implements.Dynamic.Nodes.MultiDictionaryNode;
import Implements.Dynamic.Nodes.MultiValueNode;
import Interfaces.MultipleDictionaryADT;
import Interfaces.SetADT;

public class DynamicMultipleDictionaryADT implements MultipleDictionaryADT {
    private MultiDictionaryNode head;

    public DynamicMultipleDictionaryADT() {
        this.head = null;
    }

    // Busca el nodo de la clave, o null si no existe
    private MultiDictionaryNode findKey(int key) {
        MultiDictionaryNode current = head;
        while (current != null) {
            if (current.key == key) return current;
            current = current.next;
        }
        return null;
    }

    @Override
    public void add(int key, int value) {
        MultiDictionaryNode keyNode = findKey(key);
        if (keyNode == null) {
            // Clave nueva: crea nodo y lo agrega al frente
            MultiDictionaryNode newNode = new MultiDictionaryNode(key, value);
            newNode.next = head;
            head = newNode;
        } else {
            // Clave existente: agrega valor al frente de su lista
            MultiValueNode newValue = new MultiValueNode(value);
            newValue.next = keyNode.valuesHead;
            keyNode.valuesHead = newValue;
        }
    }

    @Override
    public void remove(int key) {
        if (isEmpty()) throw new EmptyStructureException();
        if (head.key == key) {
            head = head.next;
            return;
        }
        MultiDictionaryNode current = head;
        while (current.next != null) {
            if (current.next.key == key) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
        // Si no existe, no hace nada
    }

    @Override
    public void remove(int key, int value) {
        if (isEmpty()) throw new EmptyStructureException();
        MultiDictionaryNode keyNode = findKey(key);
        if (keyNode == null) return; // no existe la clave, no hace nada

        // Eliminar el valor de la lista de valores
        if (keyNode.valuesHead == null) return;

        if (keyNode.valuesHead.value == value) {
            keyNode.valuesHead = keyNode.valuesHead.next;
            // Si la lista quedó vacía, eliminar la clave
            if (keyNode.valuesHead == null) remove(key);
            return;
        }

        MultiValueNode current = keyNode.valuesHead;
        while (current.next != null) {
            if (current.next.value == value) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
        // Si el valor no existe, no hace nada
    }

    @Override
    public int[] get(int key) {
        if (isEmpty()) throw new EmptyStructureException();
        MultiDictionaryNode keyNode = findKey(key);
        if (keyNode == null) throw new KeyNotFoundException();

        // Contar valores
        int count = 0;
        MultiValueNode current = keyNode.valuesHead;
        while (current != null) {
            count++;
            current = current.next;
        }

        // Llenar array
        int[] result = new int[count];
        current = keyNode.valuesHead;
        for (int i = 0; i < count; i++) {
            result[i] = current.value;
            current = current.next;
        }
        return result;
    }

    @Override
    public SetADT getKeys() {
        DynamicSetADT keys = new DynamicSetADT();
        MultiDictionaryNode current = head;
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
