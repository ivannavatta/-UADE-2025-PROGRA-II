package Implements.Static;

import Exceptions.EmptyStructureException;
import Exceptions.FullStructureException;
import Interfaces.BinaryTreeADT;

public class StaticBinaryTreeADT implements BinaryTreeADT {
    private int[] elements;
    private boolean[] occupied;
    private final int capacity;

    public StaticBinaryTreeADT(int capacity) {
        this.capacity = capacity;
        this.elements = new int[capacity];
        this.occupied = new boolean[capacity];
    }

    @Override
    public int getRoot() {
        if (isEmpty()) throw new EmptyStructureException();
        return elements[0];
    }

    @Override
    public BinaryTreeADT getLeft() {
        return getSubtree(1);
    }

    @Override
    public BinaryTreeADT getRight() {
        return getSubtree(2);
    }

    private StaticBinaryTreeADT getSubtree(int rootIndex) {
        StaticBinaryTreeADT subtree = new StaticBinaryTreeADT(capacity);
        if (rootIndex < capacity && occupied[rootIndex]) {
            copySubtree(subtree, 0, rootIndex);
        }
        return subtree;
    }

    private void copySubtree(StaticBinaryTreeADT dest, int destIndex, int srcIndex) {
        if (srcIndex >= capacity || !occupied[srcIndex]) return;
        if (destIndex >= dest.capacity) throw new FullStructureException();
        dest.elements[destIndex] = elements[srcIndex];
        dest.occupied[destIndex] = true;
        copySubtree(dest, 2 * destIndex + 1, 2 * srcIndex + 1);
        copySubtree(dest, 2 * destIndex + 2, 2 * srcIndex + 2);
    }

    @Override
    public void add(int value) {
        addAt(0, value);
    }

    private void addAt(int index, int value) {
        if (index >= capacity) throw new FullStructureException();
        if (!occupied[index]) {
            elements[index] = value;
            occupied[index] = true;
        } else if (value < elements[index]) {
            addAt(2 * index + 1, value);
        } else if (value > elements[index]) {
            addAt(2 * index + 2, value);
        }
    }

    @Override
    public void remove(int value) {
        if (isEmpty()) throw new EmptyStructureException();
        removeAt(0, value);
    }

    private void removeAt(int index, int value) {
        if (index >= capacity || !occupied[index]) return;

        if (value < elements[index]) {
            removeAt(2 * index + 1, value);
        } else if (value > elements[index]) {
            removeAt(2 * index + 2, value);
        } else {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;

            if (!hasOccupied(leftChild) && !hasOccupied(rightChild)) {
                occupied[index] = false;
            } else if (!hasOccupied(leftChild)) {
                int minIndex = findMinIndex(rightChild);
                elements[index] = elements[minIndex];
                removeAt(rightChild, elements[index]);
            } else if (!hasOccupied(rightChild)) {
                int maxIndex = findMaxIndex(leftChild);
                elements[index] = elements[maxIndex];
                removeAt(leftChild, elements[index]);
            } else {
                int minIndex = findMinIndex(rightChild);
                elements[index] = elements[minIndex];
                removeAt(rightChild, elements[index]);
            }
        }
    }

    private boolean hasOccupied(int index) {
        return index < capacity && occupied[index];
    }

    private int findMinIndex(int index) {
        int left = 2 * index + 1;
        if (hasOccupied(left)) return findMinIndex(left);
        return index;
    }

    private int findMaxIndex(int index) {
        int right = 2 * index + 2;
        if (hasOccupied(right)) return findMaxIndex(right);
        return index;
    }

    @Override
    public boolean isEmpty() {
        return !occupied[0];
    }
}
