package Implements.Dynamic.Nodes;

public class EdgeNode {
        public int vertex;
        public int weight;
        public EdgeNode next;

        public EdgeNode(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
            this.next = null;
        }
}
