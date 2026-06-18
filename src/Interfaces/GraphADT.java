package Interfaces;

public interface GraphADT {
    SetADT getVertexs();
    void addVertex(int vertex);
    void removeVertex(int vertex);
    void addEdge(int vertxOne, int vertxTwo, int weight);
    void removeEdge(int vertxOne, int vertxTwo);
    boolean existEdge(int vertxOne, int vertxTwo);
    int edgeWeight(int vertxOne, int vertxTwo);
    boolean isEmpty();
}
