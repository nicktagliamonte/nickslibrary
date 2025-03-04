package com.nickslibrary.datastructures.trees;

import java.util.Random;
import java.util.function.Consumer;

public class Treap {
    public static class Node {
        public int key;
        int priority;
        Node left, right;

        Node(int key) {
            this.key = key;
            this.priority = new Random().nextInt();
            this.left = this.right = null;
        }
    }

    private Node root;

    public Treap() {
        root = null;
    }

    // Right rotation
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Return new root
        return x;
    }

    // Left rotation
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Return new root
        return y;
    }

    // Insert a new key
    public void insert(int key) {
        root = insert(root, key);
    }

    private Node insert(Node root, int key) {
        // If root is null, create a new node
        if (root == null) {
            return new Node(key);
        }

        // If key is smaller than root's key, insert in left subtree
        if (key < root.key) {
            root.left = insert(root.left, key);

            // Fix the heap property if it is violated
            if (root.left.priority > root.priority) {
                root = rotateRight(root);
            }
        } else { // If key is greater than root's key, insert in right subtree
            root.right = insert(root.right, key);

            // Fix the heap property if it is violated
            if (root.right.priority > root.priority) {
                root = rotateLeft(root);
            }
        }

        return root;
    }

    // Delete a key
    public void delete(int key) {
        root = delete(root, key);
    }

    private Node delete(Node root, int key) {
        // If root is null, return null
        if (root == null) {
            return null;
        }

        // If key is smaller than root's key, delete from left subtree
        if (key < root.key) {
            root.left = delete(root.left, key);
        } else if (key > root.key) { // If key is greater than root's key, delete from right subtree
            root.right = delete(root.right, key);
        } else { // If key is equal to root's key, delete this node
            // If left child is null, return right child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) { // If right child is null, return left child
                return root.left;
            }

            // If both children are present
            if (root.left.priority < root.right.priority) {
                root = rotateLeft(root);
                root.left = delete(root.left, key);
            } else {
                root = rotateRight(root);
                root.right = delete(root.right, key);
            }
        }

        return root;
    }

    // Search for a key
    public boolean search(int key) {
        return search(root, key);
    }

    private boolean search(Node root, int key) {
        if (root == null) {
            return false;
        }

        if (root.key == key) {
            return true;
        }

        if (key < root.key) {
            return search(root.left, key);
        } else {
            return search(root.right, key);
        }
    }

    // In-order traversal
    public void inorder(Consumer<Node> consumer) {
        inorder(root, consumer);
    }

    private void inorder(Node node, Consumer<Node> consumer) {
        if (node != null) {
            inorder(node.left, consumer);
            consumer.accept(node);
            inorder(node.right, consumer);
        }
    }
}