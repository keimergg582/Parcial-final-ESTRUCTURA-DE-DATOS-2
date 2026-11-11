package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> {
    private static final class Node<T> {
        T value;
        Node<T> left, right;
        Node(T value) { this.value = value; }
    }

    private Node<T> root;

    public boolean insert(T value) {
        requireValue(value);
        if (root == null) { root = new Node<>(value); return true; }
        return insert(root, value);
    }

    private boolean insert(Node<T> node, T value) {
        int comparison = value.compareTo(node.value);
        if (comparison == 0) return false;
        if (comparison < 0) {
            if (node.left == null) { node.left = new Node<>(value); return true; }
            return insert(node.left, value);
        }
        if (node.right == null) { node.right = new Node<>(value); return true; }
        return insert(node.right, value);
    }

    public boolean search(T value) {
        requireValue(value);
        Node<T> current = root;
        while (current != null) {
            int comparison = value.compareTo(current.value);
            if (comparison == 0) return true;
            current = comparison < 0 ? current.left : current.right;
        }
        return false;
    }

    /** Positivo = encontrado; negativo = no encontrado. La magnitud codifica los pasos + 1. */
    public int searchSteps(T value) {
        requireValue(value);
        int steps = 0;
        Node<T> current = root;
        while (current != null) {
            steps++;
            int comparison = value.compareTo(current.value);
            if (comparison == 0) return steps;
            current = comparison < 0 ? current.left : current.right;
        }
        return -(steps + 1);
    }

    public boolean delete(T value) {
        requireValue(value);
        if (!search(value)) return false;
        root = delete(root, value);
        return true;
    }

    private Node<T> delete(Node<T> node, T value) {
        if (node == null) return null;
        int comparison = value.compareTo(node.value);
        if (comparison < 0) node.left = delete(node.left, value);
        else if (comparison > 0) node.right = delete(node.right, value);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node<T> successor = min(node.right);
            node.value = successor.value;
            node.right = delete(node.right, successor.value);
        }
        return node;
    }

    private Node<T> min(Node<T> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public int height() { return height(root); }
    private int height(Node<T> node) {
        return node == null ? 0 : 1 + Math.max(height(node.left), height(node.right));
    }

    public List<T> inOrder() { List<T> out = new ArrayList<>(); inOrder(root, out); return out; }
    private void inOrder(Node<T> n, List<T> out) {
        if (n == null) return;
        inOrder(n.left, out); out.add(n.value); inOrder(n.right, out);
    }

    public List<T> preOrder() { List<T> out = new ArrayList<>(); preOrder(root, out); return out; }
    private void preOrder(Node<T> n, List<T> out) {
        if (n == null) return;
        out.add(n.value); preOrder(n.left, out); preOrder(n.right, out);
    }

    public List<T> postOrder() { List<T> out = new ArrayList<>(); postOrder(root, out); return out; }
    private void postOrder(Node<T> n, List<T> out) {
        if (n == null) return;
        postOrder(n.left, out); postOrder(n.right, out); out.add(n.value);
    }

    private static void requireValue(Object value) {
        if (value == null) throw new IllegalArgumentException("El valor no puede ser null");
    }
}
