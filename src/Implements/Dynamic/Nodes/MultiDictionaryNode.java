package Implements.Dynamic.Nodes;

public class MultiDictionaryNode {
    public int key;
    public MultiValueNode valuesHead; // lista enlazada de valores para esta clave
    public MultiDictionaryNode next;

    public MultiDictionaryNode(int key, int firstValue) {
        this.key = key;
        this.valuesHead = new MultiValueNode(firstValue);
        this.next = null;
    }
}
