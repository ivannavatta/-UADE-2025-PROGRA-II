package Implements.Dynamic.Nodes;

public class DictionaryNode {
    public int key;
    public int value;
    public DictionaryNode next;

    public DictionaryNode(int key, int value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

}
