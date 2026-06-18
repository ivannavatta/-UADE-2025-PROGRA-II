package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Implements.Dynamic.Nodes.EdgeNode;
import Implements.Dynamic.Nodes.VertexNode;
import Interfaces.GraphADT;
import Interfaces.SetADT;

public class DynamicGraphADT implements GraphADT {
    private VertexNode head;

    public DynamicGraphADT() {
        this.head = null;
    }

    private VertexNode findVertex(int vertex) {
        VertexNode current = head;
        while (current != null) {
            if (current.vertex == vertex) return current;
            current = current.next;
        }
        return null;
    }

    @Override
    public SetADT getVertexs() {
        DynamicSetADT set = new DynamicSetADT();
        VertexNode current = head;
        while (current != null) {
            set.add(current.vertex);
            current = current.next;
        }
        return set;
    }

    @Override
    public void addVertex(int vertex) {
        if (findVertex(vertex) != null) return;
        VertexNode newNode = new VertexNode(vertex);
        newNode.next = head;
        head = newNode;
    }

    @Override
    public void removeVertex(int vertex) {
        if (isEmpty()) throw new EmptyStructureException();

        VertexNode current = head;
        while (current != null) {
            removeEdgeFromList(current, vertex);
            current = current.next;
        }

        if (head.vertex == vertex) {
            head = head.next;
            return;
        }
        VertexNode prev = head;
        while (prev.next != null) {
            if (prev.next.vertex == vertex) {
                prev.next = prev.next.next;
                return;
            }
            prev = prev.next;
        }
    }

    private void removeEdgeFromList(VertexNode vertexNode, int target) {
        if (vertexNode.edgesHead == null) return;
        if (vertexNode.edgesHead.vertex == target) {
            vertexNode.edgesHead = vertexNode.edgesHead.next;
            return;
        }
        EdgeNode current = vertexNode.edgesHead;
        while (current.next != null) {
            if (current.next.vertex == target) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public void addEdge(int vertxOne, int vertxTwo, int weight) {
        VertexNode v1 = findVertex(vertxOne);
        VertexNode v2 = findVertex(vertxTwo);
        if (v1 == null || v2 == null) return;

        addEdgeToList(v1, vertxTwo, weight);
        addEdgeToList(v2, vertxOne, weight);
    }

    private void addEdgeToList(VertexNode vertexNode, int target, int weight) {
        EdgeNode current = vertexNode.edgesHead;
        while (current != null) {
            if (current.vertex == target) {
                current.weight = weight;
                return;
            }
            current = current.next;
        }
        EdgeNode newEdge = new EdgeNode(target, weight);
        newEdge.next = vertexNode.edgesHead;
        vertexNode.edgesHead = newEdge;
    }

    @Override
    public void removeEdge(int vertxOne, int vertxTwo) {
        VertexNode v1 = findVertex(vertxOne);
        VertexNode v2 = findVertex(vertxTwo);
        if (v1 == null || v2 == null) return;

        removeEdgeFromList(v1, vertxTwo);
        removeEdgeFromList(v2, vertxOne);
    }

    @Override
    public boolean existEdge(int vertxOne, int vertxTwo) {
        if (isEmpty()) throw new EmptyStructureException();
        VertexNode v1 = findVertex(vertxOne);
        if (v1 == null) return false;
        EdgeNode current = v1.edgesHead;
        while (current != null) {
            if (current.vertex == vertxTwo) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public int edgeWeight(int vertxOne, int vertxTwo) {
        if (isEmpty()) throw new EmptyStructureException();
        VertexNode v1 = findVertex(vertxOne);
        if (v1 == null) throw new IllegalArgumentException("Vértice no existe.");
        EdgeNode current = v1.edgesHead;
        while (current != null) {
            if (current.vertex == vertxTwo) return current.weight;
            current = current.next;
        }
        throw new IllegalArgumentException("La arista no existe.");
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }
}
