package Implements.Dynamic.Nodes;

public class VertexNode {
    public int vertex;
    public EdgeNode edgesHead; // lista de aristas
    public VertexNode next;

    public VertexNode(int vertex) {
        this.vertex = vertex;
        this.edgesHead = null;
        this.next = null;
    }
}
