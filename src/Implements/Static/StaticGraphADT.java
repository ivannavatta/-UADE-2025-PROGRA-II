package Implements.Static;

import Exceptions.EmptyStructureException;
import Interfaces.GraphADT;
import Interfaces.SetADT;

public abstract class StaticGraphADT implements GraphADT {
    /*
     * Implementación con matriz de adyacencia.
     * - Los vértices son mapeados a índices internos (0 a capacity-1).
     * - adjacency[i][j] = peso de la arista entre el vértice i y j, o NO_EDGE si no existe.
     * - vertexMap[i] almacena el valor del vértice en la posición i, o EMPTY si no hay.
     */
    private static final int NO_EDGE = Integer.MIN_VALUE;
    private static final int EMPTY   = Integer.MIN_VALUE;

    private int[][] adjacency;
    private int[] vertexMap;
    private boolean[] vertexActive;
    private int vertexCount;
    private final int capacity;

    public StaticGraphADT(int capacity) {
        this.capacity = capacity;
        this.adjacency = new int[capacity][capacity];
        this.vertexMap = new int[capacity];
        this.vertexActive = new boolean[capacity];
        this.vertexCount = 0;

        for (int i = 0; i < capacity; i++) {
            vertexActive[i] = false;
            for (int j = 0; j < capacity; j++) {
                adjacency[i][j] = NO_EDGE;
            }
        }
    }

    // Retorna el índice interno del vértice, o -1 si no existe
    private int findIndex(int vertex) {
        for (int i = 0; i < capacity; i++) {
            if (vertexActive[i] && vertexMap[i] == vertex) return i;
        }
        return -1;
    }

    // Retorna un índice libre en vertexMap, o -1 si está lleno
    private int findFreeIndex() {
        for (int i = 0; i < capacity; i++) {
            if (!vertexActive[i]) return i;
        }
        return -1;
    }

    @Override
    public SetADT getVertexs() {
        StaticSetADT set = new StaticSetADT(capacity == 0 ? 1 : capacity);
        for (int i = 0; i < capacity; i++) {
            if (vertexActive[i]) set.add(vertexMap[i]);
        }
        return set;
    }

    @Override
    public void addVertex(int vertex) {
        if (findIndex(vertex) != -1) return; // ya existe
        int free = findFreeIndex();
        if (free == -1) throw new IllegalStateException("Grafo lleno, no se pueden agregar más vértices.");
        vertexMap[free] = vertex;
        vertexActive[free] = true;
        vertexCount++;
    }

    @Override
    public void removeVertex(int vertex) {
        if (isEmpty()) throw new EmptyStructureException();
        int index = findIndex(vertex);
        if (index == -1) return; // no existe, no hace nada

        // Eliminar todas las aristas del vértice
        for (int j = 0; j < capacity; j++) {
            adjacency[index][j] = NO_EDGE;
            adjacency[j][index] = NO_EDGE;
        }
        vertexActive[index] = false;
        vertexCount--;
    }

    @Override
    public void addEdge(int vertxOne, int vertxTwo, int weight) {
        int i = findIndex(vertxOne);
        int j = findIndex(vertxTwo);
        if (i == -1 || j == -1) return; // los vértices deben existir

        // Grafo no dirigido
        adjacency[i][j] = weight;
        adjacency[j][i] = weight;
    }

    @Override
    public void removeEdge(int vertxOne, int vertxTwo) {
        int i = findIndex(vertxOne);
        int j = findIndex(vertxTwo);
        if (i == -1 || j == -1) return; // no hace nada si no existen

        adjacency[i][j] = NO_EDGE;
        adjacency[j][i] = NO_EDGE;
    }

    @Override
    public boolean existEdge(int vertxOne, int vertxTwo) {
        if (isEmpty()) throw new EmptyStructureException();
        int i = findIndex(vertxOne);
        int j = findIndex(vertxTwo);
        if (i == -1 || j == -1) return false;
        return adjacency[i][j] != NO_EDGE;
    }

    @Override
    public int edgeWeight(int vertxOne, int vertxTwo) {
        if (isEmpty()) throw new EmptyStructureException();
        int i = findIndex(vertxOne);
        int j = findIndex(vertxTwo);
        if (i == -1 || j == -1) throw new IllegalArgumentException("Uno o ambos vértices no existen.");
        if (adjacency[i][j] == NO_EDGE) throw new IllegalArgumentException("La arista no existe.");
        return adjacency[i][j];
    }

    @Override
    public boolean isEmpty() {
        return vertexCount == 0;
    }
}
