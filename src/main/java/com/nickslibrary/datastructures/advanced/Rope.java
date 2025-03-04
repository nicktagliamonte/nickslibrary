package com.nickslibrary.datastructures.advanced;

public class Rope {

    private static class Node {
        String value;
        int weight;
        Node left, right;

        Node(String value) {
            this.value = value;
            this.weight = value.length();
            this.left = this.right = null;
        }

        Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.weight = left.weight + (right != null ? right.weight : 0);
        }
    }

    private Node root;

    public Rope(String value) {
        root = new Node(value);
    }

    private Rope(Node root) {
        this.root = root;
    }

    // Concatenate two ropes
    public static Rope concatenate(Rope left, Rope right) {
        return new Rope(new Node(left.root, right.root));
    }

    // Get character at index
    public char charAt(int index) {
        if (index < 0 || index >= root.weight) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return charAt(root, index);
    }

    private char charAt(Node node, int index) {
        if (node.left == null && node.right == null) {
            return node.value.charAt(index);
        }
        if (index < node.left.weight) {
            return charAt(node.left, index);
        } else {
            return charAt(node.right, index - node.left.weight);
        }
    }

    // Split the rope at index
    public Rope[] split(int index) {
        Node[] splitNodes = split(root, index);
        return new Rope[] { new Rope(splitNodes[0]), new Rope(splitNodes[1]) };
    }

    private Node[] split(Node node, int index) {
        if (node.left == null && node.right == null) {
            Node leftNode = new Node(node.value.substring(0, index));
            Node rightNode = new Node(node.value.substring(index));
            return new Node[] { leftNode, rightNode };
        }
        if (index < node.weight) {
            Node[] splitNodes = split(node.left, index);
            return new Node[] { splitNodes[0], new Node(splitNodes[1], node.right) };
        } else {
            Node[] splitNodes = split(node.right, index - node.left.weight);
            return new Node[] { new Node(node.left, splitNodes[0]), splitNodes[1] };
        }
    }

    // Convert the rope to a string
    public String toString() {
        return toString(root);
    }

    private String toString(Node node) {
        if (node.left == null && node.right == null) {
            return node.value;
        }
        return toString(node.left) + toString(node.right);
    }
}