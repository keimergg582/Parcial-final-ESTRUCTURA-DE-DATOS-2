package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.ArrayList;
import java.util.List;

public class AvlTree<T extends Comparable<T>> {
    private static final class Node<T> {
        T value;
        int height = 1;
        Node<T> left, right;
        Node(T value) { this.value = value; }
    }

    private Node<T> root;

    public boolean insert(T value) {
        requireValue(value);
        boolean[] inserted = {false};
        root = insert(root, value, inserted);
        return inserted[0];
    }

    private Node<T> insert(Node<T> node, T value, boolean[] inserted) {
        if (node == null) { inserted[0] = true; return new Node<>(value); }
        int comparison = value.compareTo(node.value);
        if (comparison < 0) node.left = insert(node.left, value, inserted);
        else if (comparison > 0) node.right = insert(node.right, value, inserted);
        else return node;
        return rebalance(update(node));
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
        boolean[] deleted = {false};
        root = delete(root, value, deleted);
        return deleted[0];
    }

    private Node<T> delete(Node<T> node, T value, boolean[] deleted) {
        if (node == null) return null;
        int comparison = value.compareTo(node.value);
        if (comparison < 0) node.left = delete(node.left, value, deleted);
        else if (comparison > 0) node.right = delete(node.right, value, deleted);
        else {
            deleted[0] = true;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node<T> successor = min(node.right);
            node.value = successor.value;
            boolean[] ignored = {false};
            node.right = delete(node.right, successor.value, ignored);
        }
        return rebalance(update(node));
    }

    private Node<T> min(Node<T> node) { while (node.left != null) node = node.left; return node; }
    private int height(Node<T> n) { return n == null ? 0 : n.height; }
    private int balance(Node<T> n) { return n == null ? 0 : height(n.left) - height(n.right); }
    private Node<T> update(Node<T> n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
        return n;
    }

    private Node<T> rebalance(Node<T> n) {
        int factor = balance(n);
        if (factor > 1) {
            if (balance(n.left) < 0) n.left = rotateLeft(n.left); // LR
            return rotateRight(n); // LL o LR
        }
        if (factor < -1) {
            if (balance(n.right) > 0) n.right = rotateRight(n.right); // RL
            return rotateLeft(n); // RR o RL
        }
        return n;
    }

    private Node<T> rotateRight(Node<T> y) {
        Node<T> x = y.left, transfer = x.right;
        x.right = y; y.left = transfer;
        update(y); update(x);
        return x;
    }

    private Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.right, transfer = y.left;
        y.left = x; x.right = transfer;
        update(x); update(y);
        return y;
    }

    public int height() { return height(root); }
    public int balanceFactor() { return balance(root); }
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

    public String structure() {
        StringBuilder sb = new StringBuilder();
        structure(root, "", true, sb);
        return sb.toString();
    }
    private void structure(Node<T> n, String prefix, boolean last, StringBuilder sb) {
        if (n == null) return;
        sb.append(prefix).append("+-- ")
          .append(n.value).append(" [h=").append(n.height)
          .append(", FB=").append(balance(n)).append("]")
          .append(System.lineSeparator());
        String next = prefix + (last ? "    " : "|   ");
        if (n.left != null || n.right != null) {
            structure(n.left, next, n.right == null, sb);
            structure(n.right, next, true, sb);
        }
    }

    private static void requireValue(Object value) {
        if (value == null) throw new IllegalArgumentException("El valor no puede ser null");
    }
}
