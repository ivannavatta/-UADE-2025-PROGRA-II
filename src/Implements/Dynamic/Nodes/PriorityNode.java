package Implements.Dynamic.Nodes;

public class PriorityNode {
    public int value;
    public int priority;
    public  PriorityNode next;

    public PriorityNode(int value, int priority) {
        this.value = value;
        this.priority = priority;
        this.next = null;
    }
}
