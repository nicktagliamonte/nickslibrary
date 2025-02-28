package com.nickslibrary.datastructures.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.nickslibrary.utils.tree.AVLTreeNode;

/**
 * Implementation of an AVL Tree.
 *
 * @param <T> The type of elements stored in the tree.
 */
public class AVLTree<T extends Comparable<T>> {
    private AVLTreeNode<T> root;

    /**
     * Inserts a value into the AVL tree.
     *
     * @param value The value to insert.
     */
    public void insert(T value) {
        root = insertRecursive(root, value);
    }

    /**
     * Recursively inserts a value into the AVL tree and balances the tree.
     *
     * @param node  The current node in the recursive traversal.
     * @param value The value to insert.
     * @return The balanced node after insertion.
     */
    private AVLTreeNode<T> insertRecursive(AVLTreeNode<T> node, T value) {
        if (node == null) {
            return new AVLTreeNode<>(value);
        }

        if (value.compareTo(node.getValue()) < 0) {
            node.setLeft(insertRecursive(node.getLeft(), value));
        } else if (value.compareTo(node.getValue()) > 0) {
            node.setRight(insertRecursive(node.getRight(), value));
        } else {
            // Duplicate values are not allowed in this AVL tree
            return node;
        }

        return balance(node);
    }

    /**
     * Deletes a value from the AVL tree.
     *
     * @param value The value to delete.
     */
    public void delete(T value) {
        root = deleteRecursive(root, value);
    }

    /**
     * Recursively deletes a value from the AVL tree and balances the tree.
     *
     * @param node  The current node in the recursive traversal.
     * @param value The value to delete.
     * @return The balanced node after deletion.
     */
    private AVLTreeNode<T> deleteRecursive(AVLTreeNode<T> node, T value) {
        if (node == null) {
            return null; // Value not found
        }

        if (value.compareTo(node.getValue()) < 0) {
            node.setLeft(deleteRecursive(node.getLeft(), value));
        } else if (value.compareTo(node.getValue()) > 0) {
            node.setRight(deleteRecursive(node.getRight(), value));
        } else {
            // Node to delete found

            // Case 1: Node has only one child or no children
            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            }

            // Case 2: Node has two children, find in-order successor (smallest in right
            // subtree)
            AVLTreeNode<T> successor = findMin(node.getRight());
            node.setValue(successor.getValue());
            node.setRight(deleteRecursive(node.getRight(), successor.getValue()));
        }

        // Assume balance() is fully implemented and handles rotations
        return balance(node);
    }

    /**
     * Finds the node with the minimum value in a given subtree.
     *
     * @param node The root of the subtree.
     * @return The node with the smallest value.
     */
    public AVLTreeNode<T> findMin(AVLTreeNode<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Finds and returns the node with the maximum value in the AVL tree.
     *
     * @return The AVLTreeNode containing the maximum value, or null if the tree is
     *         empty.
     */
    public AVLTreeNode<T> findMax() {
        if (root == null) {
            return null;
        }

        AVLTreeNode<T> current = root;
        while (current.getRight() != null) {
            current = current.getRight();
        }
        return current;
    }

    /**
     * Searches for a value in the AVL tree.
     *
     * @param value The value to search for.
     * @return True if the value exists in the tree, false otherwise.
     */
    public boolean search(T value) {
        return searchRecursive(root, value);
    }

    /**
     * Recursively searches for a value in the AVL tree.
     *
     * @param node  The current node in the recursive traversal.
     * @param value The value to search for.
     * @return True if the value is found, false otherwise.
     */
    private boolean searchRecursive(AVLTreeNode<T> node, T value) {
        if (node == null) {
            return false; // Value not found
        }

        if (value.compareTo(node.getValue()) < 0) {
            return searchRecursive(node.getLeft(), value);
        } else if (value.compareTo(node.getValue()) > 0) {
            return searchRecursive(node.getRight(), value);
        } else {
            return true; // Value found
        }
    }

    /**
     * Performs a left rotation at a given node.
     *
     * @param node The node to rotate.
     * @return The new root of the rotated subtree.
     */
    private AVLTreeNode<T> rotateLeft(AVLTreeNode<T> node) {
        if (node == null || node.getRight() == null) {
            return node; // Rotation not possible
        }

        AVLTreeNode<T> newRoot = node.getRight();
        node.setRight(newRoot.getLeft());
        newRoot.setLeft(node);

        // Assume updateHeight() correctly updates node heights
        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    /**
     * Performs a right rotation at a given node.
     *
     * @param node The node to rotate.
     * @return The new root of the rotated subtree.
     */
    private AVLTreeNode<T> rotateRight(AVLTreeNode<T> node) {
        if (node == null || node.getLeft() == null) {
            return node; // Rotation not possible
        }

        AVLTreeNode<T> newRoot = node.getLeft();
        node.setLeft(newRoot.getRight());
        newRoot.setRight(node);

        // Assume updateHeight() correctly updates node heights
        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    /**
     * Updates the height of a given node based on its children's heights.
     *
     * @param node The node to update.
     */
    private void updateHeight(AVLTreeNode<T> node) {
        if (node == null) {
            return;
        }

        int leftHeight = (node.getLeft() != null) ? node.getLeft().getHeight() : 0;
        int rightHeight = (node.getRight() != null) ? node.getRight().getHeight() : 0;

        node.setHeight(1 + Math.max(leftHeight, rightHeight));
    }

    /**
     * Balances the AVL tree at a given node.
     *
     * @param node The node to balance.
     * @return The balanced node.
     */
    private AVLTreeNode<T> balance(AVLTreeNode<T> node) {
        if (node == null) {
            return null;
        }

        int balanceFactor = node.getBalanceFactor(); // Use the method from AVLTreeNode

        // Left Heavy (Right Rotation)
        if (balanceFactor > 1) {
            if (node.getLeft().getBalanceFactor() < 0) {
                node.setLeft(rotateLeft(node.getLeft())); // Left-Right Case
            }
            return rotateRight(node);
        }

        // Right Heavy (Left Rotation)
        if (balanceFactor < -1) {
            if (node.getRight().getBalanceFactor() > 0) {
                node.setRight(rotateRight(node.getRight())); // Right-Left Case
            }
            return rotateLeft(node);
        }

        return node; // Already balanced
    }

    /**
     * Returns the height of the tree.
     *
     * @return The height of the AVL tree.
     */
    public int getHeight() {
        return (root == null) ? 0 : root.getHeight();
    }

    /**
     * Performs an in-order traversal of the tree and returns a list of node values.
     *
     * @return A List containing the values in in-order sequence.
     */
    public List<T> inOrderTraversal() {
        List<T> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }

    /**
     * Helper method to recursively perform in-order traversal.
     *
     * @param node The current node being processed.
     * @param list The list to store in-order values.
     */
    private void inOrderRecursive(AVLTreeNode<T> node, List<T> list) {
        if (node != null) {
            inOrderRecursive(node.getLeft(), list);
            list.add(node.getValue());
            inOrderRecursive(node.getRight(), list);
        }
    }

    /**
     * Performs a pre-order traversal of the tree and returns a list of node values.
     *
     * @return A List containing the values in pre-order sequence.
     */
    public List<T> preOrderTraversal() {
        List<T> result = new ArrayList<>();
        preOrderRecursive(root, result);
        return result;
    }

    /**
     * Helper method to recursively perform pre-order traversal.
     *
     * @param node The current node being processed.
     * @param list The list to store pre-order values.
     */
    private void preOrderRecursive(AVLTreeNode<T> node, List<T> list) {
        if (node != null) {
            list.add(node.getValue()); // Visit the node first
            preOrderRecursive(node.getLeft(), list);
            preOrderRecursive(node.getRight(), list);
        }
    }

    /**
     * Performs a post-order traversal of the tree and returns a list of node
     * values.
     *
     * @return A List containing the values in post-order sequence.
     */
    public List<T> postOrderTraversal() {
        List<T> result = new ArrayList<>();
        postOrderRecursive(root, result);
        return result;
    }

    /**
     * Helper method to recursively perform post-order traversal.
     *
     * @param node The current node being processed.
     * @param list The list to store post-order values.
     */
    private void postOrderRecursive(AVLTreeNode<T> node, List<T> list) {
        if (node != null) {
            postOrderRecursive(node.getLeft(), list);
            postOrderRecursive(node.getRight(), list);
            list.add(node.getValue()); // Visit the node last
        }
    }

    /**
     * Performs a level-order traversal of the tree and returns a list of node
     * values.
     *
     * @return A List containing the values in level-order sequence.
     */
    public List<T> levelOrderTraversal() {
        List<T> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<AVLTreeNode<T>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            AVLTreeNode<T> current = queue.poll();
            result.add(current.getValue());

            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.add(current.getRight());
            }
        }

        return result;
    }

    /**
     * Clears the AVL tree by setting the root to null.
     */
    public void clear() {
        root = null;
    }

    /**
     * Checks if the AVL tree is empty.
     *
     * @return True if the tree is empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the number of nodes in the AVL tree.
     *
     * @return The total number of nodes in the tree.
     */
    public int size() {
        return sizeRecursive(root);
    }

    /**
     * Recursively calculates the number of nodes in the subtree.
     *
     * @param node The root of the subtree.
     * @return The number of nodes in the subtree.
     */
    private int sizeRecursive(AVLTreeNode<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + sizeRecursive(node.getLeft()) + sizeRecursive(node.getRight());
    }

    /**
     * Retrieves the node containing the specified value.
     *
     * @param value The value to search for.
     * @return The node containing the value, or null if not found.
     */
    public AVLTreeNode<T> getNode(T value) {
        return getNodeRecursive(root, value);
    }

    /**
     * Recursively searches for a node with the specified value.
     *
     * @param node  The current node being searched.
     * @param value The value to find.
     * @return The node containing the value, or null if not found.
     */
    private AVLTreeNode<T> getNodeRecursive(AVLTreeNode<T> node, T value) {
        if (node == null || node.getValue().equals(value)) {
            return node;
        }

        if (value.compareTo(node.getValue()) < 0) {
            return getNodeRecursive(node.getLeft(), value);
        } else {
            return getNodeRecursive(node.getRight(), value);
        }
    }

    /**
     * Retrieves the value stored in the tree matching the specified value.
     *
     * @param value The value to search for.
     * @return The stored value if found, or null if not present.
     */
    public T get(T value) {
        AVLTreeNode<T> node = getNode(value);
        return (node != null) ? node.getValue() : null;
    }

    public AVLTreeNode<T> getRoot() {
        return root;
    }
}
