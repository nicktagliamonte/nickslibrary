package com.nickslibrary.datastructures.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A generic Binary Search Tree (BST) implementation.
 * This class provides basic BST operations such as insertion, search, 
 * and inorder traversal. It supports generic types that implement Comparable.
 *
 * @param <T> the type of elements stored in the tree, must be Comparable
 */

import com.nickslibrary.utils.BinaryTreeNode;

public class BinarySearchTree<T extends Comparable<T>> {
    private BinaryTreeNode<T> root;

    /**
     * Constructs an empty Binary Search Tree.
     */
    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * Inserts a value into the Binary Search Tree.
     * 
     * @param value the value to insert into the tree
     */
    public void insert(T value) {
        root = insertRecursive(root, value);
    }

    /**
     * Recursively inserts a value into the tree.
     * 
     * @param node  the current node in the recursion
     * @param value the value to insert
     * @return the updated node after insertion
     */
    private BinaryTreeNode<T> insertRecursive(BinaryTreeNode<T> node, T value) {
        if (node == null) {
            return new BinaryTreeNode<>(value);
        }
        if (value.compareTo(node.getValue()) < 0) {
            node.setLeft(insertRecursive(node.getLeft(), value));
        } else if (value.compareTo(node.getValue()) > 0) {
            node.setRight(insertRecursive(node.getRight(), value));
        }
        return node;
    }

    /**
     * Checks if the tree contains a given value.
     * 
     * @param value the value to search for
     * @return true if the value is found, false otherwise
     */
    public boolean contains(T value) {
        return containsRecursive(root, value);
    }

    /**
     * Recursively searches for a value in the tree.
     * 
     * @param node  the current node being checked
     * @param value the value to search for
     * @return true if the value is found, false otherwise
     */
    private boolean containsRecursive(BinaryTreeNode<T> node, T value) {
        if (node == null)
            return false;
        if (value.equals(node.getValue()))
            return true;
        return value.compareTo(node.getValue()) < 0
                ? containsRecursive(node.getLeft(), value)
                : containsRecursive(node.getRight(), value);
    }

    /**
     * Performs an in-order traversal of the tree and prints the values.
     * This traversal visits nodes in ascending order for a BST.
     */
    public void inorderTraversal() {
        inorderRecursive(root);
        System.out.println();
    }

    /**
     * Helper method for in-order traversal.
     * Recursively visits the left subtree, prints the current node's value,
     * and then visits the right subtree.
     *
     * @param node The current node being processed.
     */
    private void inorderRecursive(BinaryTreeNode<T> node) {
        if (node != null) {
            inorderRecursive(node.getLeft());
            System.out.print(node.getValue() + " ");
            inorderRecursive(node.getRight());
        }
    }

    /**
     * Deletes the specified value from the binary search tree, if it exists.
     * If the value is not found, the tree remains unchanged.
     *
     * @param value the value to be deleted from the tree
     */
    public void delete(T value) {
        root = deleteRecursive(root, value);
    }

    /**
     * Recursively deletes the specified value from the binary search tree.
     * Handles three cases:
     * 1. The node to be deleted has no children (simply removed).
     * 2. The node has one child (replaced by its child).
     * 3. The node has two children (replaced by its inorder successor, the smallest
     * value in the right subtree).
     *
     * @param node  the current subtree root
     * @param value the value to be deleted
     * @return the modified subtree with the value removed
     */
    private BinaryTreeNode<T> deleteRecursive(BinaryTreeNode<T> node, T value) {
        if (node == null) {
            return null;
        }

        if (value.compareTo(node.getValue()) < 0) {
            node.setLeft(deleteRecursive(node.getLeft(), value));
        } else if (value.compareTo(node.getValue()) > 0) {
            node.setRight(deleteRecursive(node.getRight(), value));
        } else {
            // Node with only one child or no child
            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            }

            // Node with two children: Get inorder successor (smallest in the right subtree)
            node.setValue(findMin(node.getRight()));

            // Delete the inorder successor
            node.setRight(deleteRecursive(node.getRight(), node.getValue()));
        }

        return node;
    }

    /**
     * Finds and returns the minimum value in the given subtree.
     * The minimum value is located at the leftmost node of the subtree.
     *
     * @param node the root of the subtree to search
     * @return the minimum value in the subtree
     */
    private T findMin(BinaryTreeNode<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node.getValue();
    }

    /**
     * Performs a preorder traversal of the binary search tree and returns a list
     * containing the elements in preorder sequence (Root → Left → Right).
     * 
     * @return a list of elements in preorder traversal order
     */
    public List<T> preorderTraversal() {
        List<T> result = new ArrayList<>();
        preorderRecursive(root, result);
        return result;
    }

    /**
     * Helper method for preorder traversal.
     * Recursively visits the current node, then the left subtree, and finally the
     * right subtree.
     *
     * @param node   the current node being processed
     * @param result the list collecting elements in preorder sequence
     */
    private void preorderRecursive(BinaryTreeNode<T> node, List<T> result) {
        if (node != null) {
            result.add(node.getValue()); // Visit the node first
            preorderRecursive(node.getLeft(), result); // Then visit left subtree
            preorderRecursive(node.getRight(), result); // Finally, visit right subtree
        }
    }

    /**
     * Performs a postorder traversal of the tree and returns a list of elements.
     * In postorder traversal, each node is visited after its left and right
     * subtrees.
     * I'm including a postorder traversal method for completeness, but in practice
     * 
     * @return a List containing elements in postorder traversal order the
     *         serialized return is handled by preorderTraversal and deleteAll is
     *         handled by
     *         setting root to null and letting the GC take care of dereferenced
     *         memory
     */
    public List<T> postorderTraversal() {
        List<T> result = new ArrayList<>();
        postorderRecursive(root, result);
        return result;
    }

    /**
     * Helper method for postorder traversal.
     * Recursively visits the left subtree, then the right subtree,
     * and finally adds the current node's value to the result list.
     *
     * @param node   The current node being processed.
     * @param result The list storing elements in postorder order.
     */
    private void postorderRecursive(BinaryTreeNode<T> node, List<T> result) {
        if (node != null) {
            postorderRecursive(node.getLeft(), result); // Visit left subtree
            postorderRecursive(node.getRight(), result); // Visit right subtree
            result.add(node.getValue()); // Visit current node
        }
    }

    /**
     * Performs a level-order traversal (breadth-first) of the tree
     * and returns a list of elements in that order.
     * 
     * @return a List containing elements in level-order traversal order
     */
    public List<T> levelOrderTraversal() {
        List<T> result = new ArrayList<>();
        if (root == null) {
            return result; // Return empty list if tree is empty
        }

        Queue<BinaryTreeNode<T>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            BinaryTreeNode<T> current = queue.poll();
            result.add(current.getValue()); // Visit the current node

            if (current.getLeft() != null) {
                queue.add(current.getLeft()); // Add left child to queue
            }
            if (current.getRight() != null) {
                queue.add(current.getRight()); // Add right child to queue
            }
        }
        return result;
    }

    /**
     * Retrieves a value from the tree, if it exists.
     * 
     * @param value the value to search for
     * @return the found value, or null if not found
     */
    public T get(T value) {
        return getRecursive(root, value);
    }

    /**
     * Recursively searches for a value in the tree.
     * 
     * @param node  the current node being checked
     * @param value the value to retrieve
     * @return the found value, or null if not found
     */
    private T getRecursive(BinaryTreeNode<T> node, T value) {
        if (node == null) {
            return null; // Value not found
        }
        if (value.equals(node.getValue())) {
            return node.getValue(); // Return the found value
        }
        return value.compareTo(node.getValue()) < 0
                ? getRecursive(node.getLeft(), value) // Search left subtree
                : getRecursive(node.getRight(), value); // Search right subtree
    }

    /**
     * Retrieves the node containing the given value.
     * 
     * @param value the value to search for
     * @return the found node, or null if not found
     */
    public BinaryTreeNode<T> getNode(T value) {
        return getNodeRecursive(root, value);
    }

    private BinaryTreeNode<T> getNodeRecursive(BinaryTreeNode<T> node, T value) {
        if (node == null || value.equals(node.getValue())) {
            return node; // Return the found node (or null)
        }
        return value.compareTo(node.getValue()) < 0
                ? getNodeRecursive(node.getLeft(), value)
                : getNodeRecursive(node.getRight(), value);
    }

    /**
     * Finds and returns the minimum value in the Binary Search Tree.
     * The minimum value is located at the leftmost node of the tree.
     *
     * @return the minimum value in the tree, or null if the tree is empty
     */
    public T findMin() {
        if (root == null) {
            return null;
        }
        return findMinRecursive(root);
    }

    /**
     * Recursively finds the minimum value in the given subtree.
     * The leftmost node contains the smallest value.
     *
     * @param node the root of the subtree to search
     * @return the minimum value in the subtree
     */
    private T findMinRecursive(BinaryTreeNode<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node.getValue();
    }

    /**
     * Finds and returns the maximum value in the Binary Search Tree.
     * The maximum value is located at the rightmost node of the tree.
     *
     * @return the maximum value in the tree, or null if the tree is empty
     */
    public T findMax() {
        if (root == null) {
            return null;
        }
        return findMaxRecursive(root);
    }

    /**
     * Recursively finds the maximum value in the given subtree.
     * The rightmost node contains the largest value.
     *
     * @param node the root of the subtree to search
     * @return the maximum value in the subtree
     */
    private T findMaxRecursive(BinaryTreeNode<T> node) {
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node.getValue();
    }

    /**
     * Computes the height of the Binary Search Tree.
     * The height of a tree is defined as the number of edges
     * on the longest path from the root to a leaf node.
     * An empty tree has a height of -1, and a tree with only
     * a root node has a height of 0.
     *
     * @return the height of the tree, or -1 if the tree is empty
     */
    public int getHeight() {
        return getHeightRecursive(root);
    }

    /**
     * Recursively calculates the height of a subtree.
     * The height is determined by the maximum depth of its left and right subtrees.
     *
     * @param node the current node in the recursion
     * @return the height of the subtree rooted at this node
     */
    private int getHeightRecursive(BinaryTreeNode<T> node) {
        if (node == null) {
            return -1; // Base case: an empty tree has height -1
        }
        int leftHeight = getHeightRecursive(node.getLeft());
        int rightHeight = getHeightRecursive(node.getRight());
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * Computes the total number of nodes in the Binary Search Tree.
     * This represents the total elements stored in the tree.
     *
     * @return the number of nodes in the tree, or 0 if the tree is empty
     */
    public int size() {
        return sizeRecursive(root);
    }

    /**
     * Recursively calculates the number of nodes in a subtree.
     * The size is determined by summing the sizes of its left and right subtrees
     * and adding 1 for the current node.
     *
     * @param node the current node in the recursion
     * @return the number of nodes in the subtree rooted at this node
     */
    private int sizeRecursive(BinaryTreeNode<T> node) {
        if (node == null) {
            return 0; // Base case: an empty tree has size 0
        }
        return 1 + sizeRecursive(node.getLeft()) + sizeRecursive(node.getRight());
    }

    /**
     * Checks if the Binary Search Tree is empty.
     * A tree is considered empty if it has no nodes.
     *
     * @return {@code true} if the tree is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Removes all elements from the Binary Search Tree, making it empty.
     * This sets the root to {@code null}, allowing Java's garbage collector
     * to reclaim memory used by the nodes.
     */
    public void clear() {
        root = null;
    }
}
