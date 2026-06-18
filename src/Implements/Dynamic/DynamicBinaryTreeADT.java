package Implements.Dynamic;

import Exceptions.EmptyStructureException;
import Implements.Dynamic.Nodes.TreeNode;
import Interfaces.BinaryTreeADT;

public class DynamicBinaryTreeADT implements BinaryTreeADT {
    private TreeNode root;

    public DynamicBinaryTreeADT() {
        this.root = null;
    }

    @Override
    public int getRoot() {
        if (isEmpty()) throw new EmptyStructureException();
        return root.value;
    }

    @Override
    public BinaryTreeADT getLeft() {
        if (isEmpty()) throw new EmptyStructureException();
        DynamicBinaryTreeADT left = new DynamicBinaryTreeADT();
        left.root = root.left;
        return left;
    }

    @Override
    public BinaryTreeADT getRight() {
        if (isEmpty()) throw new EmptyStructureException();
        DynamicBinaryTreeADT right = new DynamicBinaryTreeADT();
        right.root = root.right;
        return right;
    }

    @Override
    public void add(int value) {
        root = addRecursive(root, value);
    }

    private TreeNode addRecursive(TreeNode node, int value) {
        if (node == null) return new TreeNode(value);
        if (value < node.value) {
            node.left = addRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = addRecursive(node.right, value);
        }
        // Si es igual, no agrega duplicado (BST estándar)
        return node;
    }

    @Override
    public void remove(int value) {
        if (isEmpty()) throw new EmptyStructureException();
        root = removeRecursive(root, value);
    }

    private TreeNode removeRecursive(TreeNode node, int value) {
        if (node == null) return null;

        if (value < node.value) {
            node.left = removeRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = removeRecursive(node.right, value);
        } else {
            // Nodo encontrado
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Tiene dos hijos: reemplazar con el mínimo del subárbol derecho
            node.value = findMin(node.right);
            node.right = removeRecursive(node.right, node.value);
        }
        return node;
    }

    // Encuentra el mínimo valor en un subárbol
    private int findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.value;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }
}
