package com.nickslibrary.datastructures.advanced;

public class SplayTree {

    private static class Node {
        int key;
        Node left, right;

        Node(int key) {
            this.key = key;
            this.left = this.right = null;
        }
    }

    private Node root;

    public SplayTree() {
        root = null;
    }

    // Right rotation
    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    // Left rotation
    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    // Splay operation
    private Node splay(Node root, int key) {
        if (root == null || root.key == key) {
            return root;
        }

        // Key lies in left subtree
        if (key < root.key) {
            if (root.left == null) {
                return root;
            }

            // Zig-Zig (Left Left)
            if (key < root.left.key) {
                root.left.left = splay(root.left.left, key);
                root = rotateRight(root);
            } else if (key > root.left.key) { // Zig-Zag (Left Right)
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null) {
                    root.left = rotateLeft(root.left);
                }
            }

            return (root.left == null) ? root : rotateRight(root);
        } else { // Key lies in right subtree
            if (root.right == null) {
                return root;
            }

            // Zag-Zig (Right Left)
            if (key < root.right.key) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null) {
                    root.right = rotateRight(root.right);
                }
            } else if (key > root.right.key) { // Zag-Zag (Right Right)
                root.right.right = splay(root.right.right, key);
                root = rotateLeft(root);
            }

            return (root.right == null) ? root : rotateLeft(root);
        }
    }

    // Insert a new key
    public void insert(int key) {
        if (root == null) {
            root = new Node(key);
            return;
        }

        root = splay(root, key);

        if (root.key == key) {
            return;
        }

        Node newNode = new Node(key);

        if (key < root.key) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }

        root = newNode;
    }

    // Delete a key
    public void delete(int key) {
        if (root == null) {
            return;
        }

        root = splay(root, key);

        if (root.key != key) {
            return;
        }

        if (root.left == null) {
            root = root.right;
        } else {
            Node temp = root.right;
            root = root.left;
            splay(root, key);
            root.right = temp;
        }
    }

    // Search for a key
    public boolean search(int key) {
        root = splay(root, key);
        return root != null && root.key == key;
    }

    // In-order traversal
    public void inorder() {
        inorder(root);
    }

    private void inorder(Node root) {
        if (root != null) {
            inorder(root.left);
            System.out.print(root.key + " ");
            inorder(root.right);
        }
    }
}