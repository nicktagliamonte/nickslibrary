package com.nickslibrary.datastructures.trees;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.nickslibrary.utils.tree.BTreeNode;

/**
 * B-Tree implementation supporting insertions, deletions, searches, and
 * traversals.
 *
 * @param <T> The type of elements stored in the B-Tree.
 */
public class BTree<T extends Comparable<T>> {
    private BTreeNode<T> root;
    private int degree;

    /**
     * Constructs an empty B-Tree with the given degree.
     *
     * @param degree Minimum degree (defines range for number of keys).
     */
    public BTree(int degree) {
        this.degree = degree;
        this.root = new BTreeNode<>(degree, true, null);
    }

    /**
     * Inserts a key into the B-Tree.
     * If the root node is full, it is split, and the tree height increases.
     */
    public void insert(T key) {
        if (root == null) {
            root = new BTreeNode<>(degree, true, key);
            root.setKey(0, key);
            root.setNumKeys(1);
        } else {
            if (root.getNumKeys() == (2 * degree - 1)) {
                BTreeNode<T> newRoot = new BTreeNode<>(degree, false, null);
                newRoot.setChild(0, root);
                newRoot.splitChild(0);
                int i = (newRoot.getKey(0).compareTo(key) < 0) ? 1 : 0;
                newRoot.getChild(i).insertNonFull(key);
                root = newRoot;
            } else {
                root.insertNonFull(key);
            }
        }
    }

    public void bulkInsert(Collection<T> keys) {
        for (T key : keys) {
            insert(key);
        }
    }

    /**
     * Deletes a key from the B-Tree.
     * Ensures the tree remains balanced after deletion by merging or borrowing keys
     * if necessary.
     */
    public void delete(T key) {
        if (root == null) {
            return; // Tree is empty, nothing to delete.
        }

        // Start deletion process
        deleteFromNode(root, key);

        // If root has become empty and it's not a leaf, make its first child the new
        // root
        if (root.getNumKeys() == 0) {
            if (root.isLeaf()) {
                root = null; // Tree is now empty.
            } else {
                root = root.getChild(0); // Promote the first child as the new root.
            }
        }
    }

    /**
     * Deletes a key from the given node, ensuring the tree remains balanced.
     * 
     * @param node The node to delete from.
     * @param key  The key to delete.
     */
    private void deleteFromNode(BTreeNode<T> node, T key) {
        int keyIndex = node.findKeyIndex(key);

        // Key is found in this node
        if (keyIndex < node.getNumKeys() && node.getKey(keyIndex).compareTo(key) == 0) {
            if (node.isLeaf()) {
                // Simple case: Remove key from leaf node
                node.removeKey(keyIndex);
            } else {
                // Key is in an internal node
                handleInternalNodeDeletion(node, keyIndex);
            }
        } else {
            // Key is not in this node, search the appropriate child
            BTreeNode<T> childNode = node.getChild(keyIndex);
            if (childNode.getNumKeys() < degree) {
                // Ensure the child node has enough keys, balance if necessary
                balanceChildNode(node, keyIndex);
            }
            deleteFromNode(childNode, key);
        }
    }

    /**
     * Handles the deletion when the key is found in an internal node.
     * 
     * @param node     The internal node where the key was found.
     * @param keyIndex The index of the key to be deleted.
     */
    private void handleInternalNodeDeletion(BTreeNode<T> node, int keyIndex) {
        BTreeNode<T> leftChild = node.getChild(keyIndex);
        BTreeNode<T> rightChild = node.getChild(keyIndex + 1);

        if (leftChild.getNumKeys() >= degree) {
            // Find predecessor from left child
            T predecessor = leftChild.getKey(leftChild.getNumKeys() - 1);
            node.setKey(keyIndex, predecessor);
            deleteFromNode(leftChild, predecessor);
        } else if (rightChild.getNumKeys() >= degree) {
            // Find successor from right child
            T successor = rightChild.getKey(0);
            node.setKey(keyIndex, successor);
            deleteFromNode(rightChild, successor);
        } else {
            // Merge left and right children
            mergeChildren(node, keyIndex);
            deleteFromNode(leftChild, node.getKey(keyIndex));
        }
    }

    /**
     * Balances the child node before performing the delete operation.
     * 
     * @param node       The parent node.
     * @param childIndex The index of the child node to balance.
     */
    private void balanceChildNode(BTreeNode<T> node, int childIndex) {
        BTreeNode<T> leftSibling = (childIndex > 0) ? node.getChild(childIndex - 1) : null;
        BTreeNode<T> rightSibling = (childIndex < node.getNumKeys()) ? node.getChild(childIndex + 1) : null;

        if (leftSibling != null && leftSibling.getNumKeys() >= degree) {
            borrowFromSibling(node, childIndex, leftSibling, true);
        } else if (rightSibling != null && rightSibling.getNumKeys() >= degree) {
            borrowFromSibling(node, childIndex, rightSibling, false);
        } else {
            if (leftSibling != null) {
                mergeChildren(node, childIndex - 1);
            } else {
                mergeChildren(node, childIndex);
            }
        }
    }

    /**
     * Borrows a key from a sibling to balance the child node.
     * 
     * @param parentNode    The parent node containing the child.
     * @param childIndex    The index of the child node.
     * @param sibling       The sibling node from which to borrow a key.
     * @param isLeftSibling Whether the sibling is to the left of the child.
     */
    private void borrowFromSibling(BTreeNode<T> parentNode, int childIndex, BTreeNode<T> sibling,
            boolean isLeftSibling) {
        BTreeNode<T> childNode = parentNode.getChild(childIndex);
        if (isLeftSibling) {
            T borrowedKey = sibling.getKey(sibling.getNumKeys() - 1);
            BTreeNode<T> borrowedChild = sibling.getChild(sibling.getNumKeys());

            // Shift keys and children
            for (int i = childNode.getNumKeys() - 1; i >= 0; i--) {
                childNode.setKey(i + 1, childNode.getKey(i));
            }
            for (int i = childNode.getNumKeys(); i >= 0; i--) {
                childNode.setChild(i + 1, childNode.getChild(i));
            }

            // Update keys and children
            childNode.setKey(0, parentNode.getKey(childIndex - 1));
            parentNode.setKey(childIndex - 1, borrowedKey);
            childNode.setChild(0, borrowedChild);

            sibling.removeKey(sibling.getNumKeys() - 1);
            childNode.setNumKeys(childNode.getNumKeys() + 1);
            sibling.setNumKeys(sibling.getNumKeys() - 1);
        } else {
            T borrowedKey = sibling.getKey(0);
            BTreeNode<T> borrowedChild = sibling.getChild(0);

            // Shift keys and children
            for (int i = 0; i < childNode.getNumKeys(); i++) {
                childNode.setKey(i + 1, childNode.getKey(i));
            }
            for (int i = 0; i < childNode.getNumKeys() + 1; i++) {
                childNode.setChild(i + 1, childNode.getChild(i));
            }

            // Update keys and children
            childNode.setKey(childNode.getNumKeys(), parentNode.getKey(childIndex));
            parentNode.setKey(childIndex, borrowedKey);
            childNode.setChild(childNode.getNumKeys() + 1, borrowedChild);

            sibling.removeKey(0);
            childNode.setNumKeys(childNode.getNumKeys() + 1);
            sibling.setNumKeys(sibling.getNumKeys() - 1);
        }
    }

    /**
     * Merges two children of the parent node.
     * 
     * @param parentNode The parent node.
     * @param childIndex The index of the first child node to merge.
     */
    private void mergeChildren(BTreeNode<T> parentNode, int childIndex) {
        BTreeNode<T> leftChild = parentNode.getChild(childIndex);
        BTreeNode<T> rightChild = parentNode.getChild(childIndex + 1);

        // Move parent's key down to left child
        leftChild.setKey(leftChild.getNumKeys(), parentNode.getKey(childIndex));
        leftChild.setNumKeys(leftChild.getNumKeys() + 1);

        // Merge keys and children
        for (int i = 0; i < rightChild.getNumKeys(); i++) {
            leftChild.setKey(leftChild.getNumKeys(), rightChild.getKey(i));
            leftChild.setNumKeys(leftChild.getNumKeys() + 1);
        }
        if (!rightChild.isLeaf()) {
            for (int i = 0; i <= rightChild.getNumKeys(); i++) {
                leftChild.setChild(leftChild.getNumKeys(), rightChild.getChild(i));
            }
        }

        // Remove the key and child from the parent
        parentNode.removeKey(childIndex);
        parentNode.setChild(childIndex + 1, null);
        leftChild.setNumKeys(leftChild.getNumKeys() + rightChild.getNumKeys());
    }

    public void bulkDelete(Collection<T> keys) {
        for (T key : keys) {
            delete(key);
        }
    }

    /**
     * Searches for a key in the B-Tree.
     * 
     * @param key The key to search for.
     * @return true if the key is found, false otherwise.
     */
    public boolean search(T key) {
        return root != null && root.searchKey(key);
    }

    /**
     * Clears the entire B-Tree.
     */
    public void clear() {
        root = null; // Remove reference to the root, effectively clearing the tree
    }

    /**
     * Checks if the B-Tree is empty.
     */
    public boolean isEmpty() {
        return root == null || root.getNumKeys() == 0;
    }

    /**
     * Returns the height of the B-Tree.
     */
    public int getHeight() {
        if (root == null) {
            return 0; // Empty tree, height is 0
        }
        return getHeightRecursive(root);
    }

    /**
     * Helper method to calculate the height of a node recursively.
     */
    private int getHeightRecursive(BTreeNode<T> node) {
        if (node.isLeaf()) {
            return 1; // Leaf nodes have a height of 1
        }

        // Recursively calculate the height of the first child
        return 1 + getHeightRecursive(node.getChild(0));
    }

    /**
     * Returns the total number of keys in the tree.
     */
    public int size() {
        if (root == null) {
            return 0; // Empty tree, no keys
        }
        return sizeRecursive(root);
    }

    /**
     * Helper method to calculate the size of the tree recursively.
     */
    private int sizeRecursive(BTreeNode<T> node) {
        int size = node.getNumKeys(); // Start with the number of keys in this node

        if (!node.isLeaf()) {
            // Recursively calculate the size of all children
            for (int i = 0; i <= node.getNumKeys(); i++) {
                size += sizeRecursive(node.getChild(i));
            }
        }

        return size;
    }

    /**
     * Returns the smallest key in the tree.
     */
    public T getMin() {
        if (root == null) {
            return null; // Tree is empty
        }
        return getMinRecursive(root);
    }

    /**
     * Helper method to find the smallest key recursively.
     */
    private T getMinRecursive(BTreeNode<T> node) {
        // Traverse to the leftmost leaf node
        while (!node.isLeaf()) {
            node = node.getChild(0); // Go to the leftmost child
        }
        return node.getKey(0); // Return the first key in the leftmost node
    }

    /**
     * Returns the largest key in the tree.
     */
    public T getMax() {
        if (root == null) {
            return null; // Tree is empty
        }
        return getMaxRecursive(root);
    }

    /**
     * Helper method to find the largest key recursively.
     */
    private T getMaxRecursive(BTreeNode<T> node) {
        // Traverse to the rightmost leaf node
        while (!node.isLeaf()) {
            node = node.getChild(node.getNumKeys()); // Go to the rightmost child
        }
        return node.getKey(node.getNumKeys() - 1); // Return the last key in the rightmost node
    }

    /**
     * Creates a deep copy of the B-Tree.
     */
    public BTree<T> clone() {
        BTree<T> newTree = new BTree<>(degree); // Create a new tree with the same degree
        if (root != null) {
            newTree.root = cloneNode(root); // Clone the root node
        }
        return newTree;
    }

    /**
     * Helper method to clone a node recursively.
     */
    private BTreeNode<T> cloneNode(BTreeNode<T> node) {
        BTreeNode<T> newNode = new BTreeNode<>(degree, node.isLeaf(), node.getKey(0));
        newNode.setNumKeys(node.getNumKeys());

        // Clone the keys
        for (int i = 0; i < node.getNumKeys(); i++) {
            newNode.setKey(i, node.getKey(i));
        }

        // Clone the children if it's not a leaf
        if (!node.isLeaf()) {
            for (int i = 0; i <= node.getNumKeys(); i++) {
                newNode.setChild(i, cloneNode(node.getChild(i))); // Recursively clone children
            }
        }

        return newNode;
    }

    /**
     * Checks if the tree maintains B-Tree properties.
     */
    public boolean isValidBTree() {
        if (root == null) {
            return true; // An empty tree is technically valid (since there's nothing to violate).
        }
        return isValidBTreeRecursive(root, 0);
    }

    /**
     * Helper method to recursively check if the tree maintains B-Tree properties.
     */
    private boolean isValidBTreeRecursive(BTreeNode<T> node, int minKeys) {
        // Property 1: Check if the number of keys is within the valid range
        if (node.getNumKeys() < minKeys || node.getNumKeys() > 2 * degree - 1) {
            return false; // Invalid key count
        }

        // Property 2: If not a leaf, check the number of children
        if (!node.isLeaf()) {
            if (node.getNumKeys() + 1 != node.getChildren().length) {
                return false; // Invalid number of children for a non-leaf node
            }

            // Recursively check each child node
            for (int i = 0; i <= node.getNumKeys(); i++) {
                if (!isValidBTreeRecursive(node.getChild(i), degree - 1)) {
                    return false; // Invalid child node
                }
            }
        }

        // Property 3: Check if the keys are sorted within the node
        for (int i = 1; i < node.getNumKeys(); i++) {
            if (node.getKey(i).compareTo(node.getKey(i - 1)) < 0) {
                return false; // Keys are not sorted
            }
        }

        return true; // The node is valid
    }

    /**
     * Merges two child nodes of a given parent.
     */
    public void mergeNodes(BTreeNode<T> parent, int index) {
        BTreeNode<T> leftChild = parent.getChild(index);
        BTreeNode<T> rightChild = parent.getChild(index + 1);

        // Move the parent's separating key down to the left child
        leftChild.setKey(leftChild.getNumKeys(), parent.getKey(index));
        leftChild.setNumKeys(leftChild.getNumKeys() + 1);

        // Move all keys and children from the right child to the left child
        for (int i = 0; i < rightChild.getNumKeys(); i++) {
            leftChild.setKey(leftChild.getNumKeys(), rightChild.getKey(i));
            leftChild.setNumKeys(leftChild.getNumKeys() + 1);
        }

        if (!leftChild.isLeaf()) {
            for (int i = 0; i <= rightChild.getNumKeys(); i++) {
                leftChild.setChild(leftChild.getNumKeys() + i, rightChild.getChild(i));
            }
        }

        // Move the parent’s key down (i.e., remove it from the parent)
        for (int i = index; i < parent.getNumKeys() - 1; i++) {
            parent.setKey(i, parent.getKey(i + 1));
        }
        parent.setNumKeys(parent.getNumKeys() - 1);

        // Now remove the right child from the parent
        parent.setChild(index + 1, null); // Set the right child to null
        rightChild.setNumKeys(0); // Clear the right child’s keys
    }

    /**
     * Balances the tree by borrowing from a previous sibling.
     */
    public void borrowFromPrevious(BTreeNode<T> parent, int index) {
        BTreeNode<T> leftSibling = parent.getChild(index - 1);
        BTreeNode<T> node = parent.getChild(index);

        // Move the parent's separating key down into the current node
        node.setKey(0, parent.getKey(index - 1));
        parent.setKey(index - 1, leftSibling.getKey(leftSibling.getNumKeys() - 1));

        // Move the rightmost key of the left sibling to the current node
        node.setChild(0, leftSibling.getChild(leftSibling.getNumKeys()));
        node.setNumKeys(node.getNumKeys() + 1);
        leftSibling.setNumKeys(leftSibling.getNumKeys() - 1);
    }

    /**
     * Balances the tree by borrowing from the next sibling.
     */
    public void borrowFromNext(BTreeNode<T> parent, int index) {
        BTreeNode<T> rightSibling = parent.getChild(index + 1);
        BTreeNode<T> node = parent.getChild(index);

        // Move the parent's separating key down into the current node
        node.setKey(node.getNumKeys(), parent.getKey(index));
        parent.setKey(index, rightSibling.getKey(0));

        // Move the leftmost key of the right sibling to the current node
        node.setChild(node.getNumKeys() + 1, rightSibling.getChild(0));
        node.setNumKeys(node.getNumKeys() + 1);

        // Shift all keys in the right sibling to the left
        for (int i = 1; i < rightSibling.getNumKeys(); i++) {
            rightSibling.setKey(i - 1, rightSibling.getKey(i));
        }
        rightSibling.setNumKeys(rightSibling.getNumKeys() - 1);
    }

    /**
     * Ensures a node has enough keys by merging or borrowing.
     */
    public void fillNode(BTreeNode<T> parent, int index) {
        if (index > 0 && parent.getChild(index - 1).getNumKeys() > degree - 1) {
            borrowFromPrevious(parent, index);
        } else if (index < parent.getNumKeys() && parent.getChild(index + 1).getNumKeys() > degree - 1) {
            borrowFromNext(parent, index);
        } else {
            // If both siblings have too few keys, merge the node with a sibling
            if (index < parent.getNumKeys()) {
                mergeNodes(parent, index);
            } else {
                mergeNodes(parent, index - 1);
            }
        }
    }

    /**
     * Splits the root if it becomes full, creating a new level.
     */
    public void splitRoot() {
        BTreeNode<T> oldRoot = root;

        // Create a new root node with a degree of the B-tree
        root = new BTreeNode<>(degree, false, oldRoot.getKey(degree - 1));

        // Create a new right child node for the new root
        BTreeNode<T> newChild = new BTreeNode<>(degree, oldRoot.isLeaf(), oldRoot.getKey(degree));

        // Move keys and children from the old root to the new right child
        for (int i = degree; i < oldRoot.getNumKeys(); i++) {
            newChild.setKey(i - degree, oldRoot.getKey(i));
            newChild.setChild(i - degree, oldRoot.getChild(i));
        }

        // Set the number of keys for the old root and new child
        oldRoot.setNumKeys(degree - 1);
        newChild.setNumKeys(degree - 1);

        // Set the left child of the new root to be the old root
        root.setChild(0, oldRoot);
        root.setChild(1, newChild);

        // Set the new number of keys for the new root
        root.setNumKeys(1); // As the new root has only one separating key
    }

    /**
     * Returns an iterator for in-order traversal.
     */
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Stack<BTreeNode<T>> stack = new Stack<>();
            private BTreeNode<T> currentNode = root;

            // Constructor to initialize the iterator with the root node
            {
                pushLeftChildren(currentNode);
            }

            // Helper function to push left children onto the stack
            private void pushLeftChildren(BTreeNode<T> node) {
                while (node != null) {
                    stack.push(node);
                    node = node.getChild(0); // Move to the leftmost child
                }
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                BTreeNode<T> node = stack.pop();
                T key = node.getKey(0); // Get the first key of the current node

                // If the node has a right child, push its left children onto the stack
                if (!node.isLeaf()) {
                    pushLeftChildren(node.getChild(1));
                }

                // After returning a key, move to the next key in the node
                int index = 1;
                while (index < node.getNumKeys()) {
                    stack.push(node);
                    node = node.getChild(index); // Move to the next child
                    pushLeftChildren(node);
                }

                return key;
            }
        };
    }

    /**
     * Returns a list of elements in in-order traversal.
     */
    public List<T> inOrderTraversal() {
        List<T> result = new ArrayList<>();
        inOrderTraversalHelper(root, result);
        return result;
    }

    /**
     * Helper method for recursive in-order traversal.
     */
    private void inOrderTraversalHelper(BTreeNode<T> node, List<T> result) {
        if (node == null) {
            return;
        }

        // Traverse all child nodes before the keys (left children)
        for (int i = 0; i < node.getNumKeys(); i++) {
            // Traverse the left child if it exists
            if (!node.isLeaf()) {
                inOrderTraversalHelper(node.getChild(i), result);
            }

            // Add the key to the result list
            result.add(node.getKey(i));
        }

        // Traverse the rightmost child
        if (!node.isLeaf()) {
            inOrderTraversalHelper(node.getChild(node.getNumKeys()), result);
        }
    }

    /**
     * Returns a list of elements in level-order traversal.
     */
    public List<T> levelOrderTraversal() {
        List<T> result = new ArrayList<>();
        if (root == null) {
            return result; // If the tree is empty, return an empty list.
        }

        Queue<BTreeNode<T>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            BTreeNode<T> node = queue.poll();

            // Add all the keys of the current node to the result list
            for (int i = 0; i < node.getNumKeys(); i++) {
                result.add(node.getKey(i));
            }

            // Add the children of the current node to the queue (if any)
            if (!node.isLeaf()) {
                for (int i = 0; i <= node.getNumKeys(); i++) {
                    queue.add(node.getChild(i));
                }
            }
        }

        return result;
    }

    /**
     * Returns a list of elements in pre-order traversal.
     */
    public List<T> preOrderTraversal() {
        List<T> result = new ArrayList<>();
        preOrderTraversalHelper(root, result);
        return result;
    }

    /**
     * Helper method for recursive pre-order traversal.
     */
    private void preOrderTraversalHelper(BTreeNode<T> node, List<T> result) {
        if (node == null) {
            return; // Base case: If the node is null, return.
        }

        // Visit the current node: Add all keys of the node to the result list
        for (int i = 0; i < node.getNumKeys(); i++) {
            result.add(node.getKey(i));
        }

        // Recursively visit each child node (left to right)
        if (!node.isLeaf()) {
            for (int i = 0; i <= node.getNumKeys(); i++) {
                preOrderTraversalHelper(node.getChild(i), result);
            }
        }
    }

    /**
     * Returns a list of elements in post-order traversal.
     */
    public List<T> postOrderTraversal() {
        List<T> result = new ArrayList<>();
        postOrderTraversalHelper(root, result);
        return result;
    }

    /**
     * Helper method for recursive post-order traversal.
     */
    private void postOrderTraversalHelper(BTreeNode<T> node, List<T> result) {
        if (node == null) {
            return; // Base case: If the node is null, return.
        }

        // Recursively visit each child node (left to right)
        if (!node.isLeaf()) {
            for (int i = 0; i <= node.getNumKeys(); i++) {
                postOrderTraversalHelper(node.getChild(i), result);
            }
        }

        // Visit the current node: Add all keys of the node to the result list
        for (int i = 0; i < node.getNumKeys(); i++) {
            result.add(node.getKey(i));
        }
    }

    /**
     * Retrieves the node containing the given key.
     */
    public BTreeNode<T> getNode(T key) {
        if (root == null) {
            return null; // Tree is empty.
        }
        return getNodeHelper(root, key);
    }

    /**
     * Helper method to recursively search for the node containing the given key.
     */
    private BTreeNode<T> getNodeHelper(BTreeNode<T> node, T key) {
        int i = 0;

        // Find the index of the first key greater than or equal to the given key
        while (i < node.getNumKeys() && key.compareTo(node.getKey(i)) > 0) {
            i++;
        }

        // If the key is found in the current node, return the node
        if (i < node.getNumKeys() && key.compareTo(node.getKey(i)) == 0) {
            return node;
        }

        // If the node is a leaf, the key is not in the tree
        if (node.isLeaf()) {
            return null;
        }

        // Otherwise, recursively search the child node
        return getNodeHelper(node.getChild(i), key);
    }

    /**
     * Merges another B-Tree into this one.
     * This method assumes that the trees have the same degree (order).
     */
    public void merge(BTree<T> other) {
        if (other == null || other.root == null) {
            return; // Nothing to merge if the other tree is null or empty.
        }

        // If this tree is empty, simply make the other tree the root.
        if (root == null) {
            root = other.root;
            return;
        }

        // If the root is not full, we can merge the trees by directly inserting the
        // root of the other tree.
        if (root.getNumKeys() < (2 * degree - 1)) {
            mergeRoots(root, other.root);
        } else {
            // If the root is full, we must split it before merging.
            BTreeNode<T> newRoot = new BTreeNode<>(degree, false, null);
            newRoot.setNumKeys(1);
            newRoot.setKey(0, root.getKey(degree - 1));

            BTreeNode<T> leftChild = root;
            BTreeNode<T> rightChild = other.root;

            newRoot.setChild(0, leftChild);
            newRoot.setChild(1, rightChild);

            root = newRoot;
        }
    }

    /**
     * Merges two non-full roots of two trees into a single root.
     */
    private void mergeRoots(BTreeNode<T> root1, BTreeNode<T> root2) {
        // First, move all the keys from root2 to root1
        int i = root1.getNumKeys();
        for (int j = 0; j < root2.getNumKeys(); j++) {
            root1.setKey(i + j, root2.getKey(j));
        }
        root1.setNumKeys(i + root2.getNumKeys());

        // Move the children of root2 to root1
        int childrenIndex = root1.getNumKeys();
        for (int j = 0; j <= root2.getNumKeys(); j++) {
            root1.setChild(childrenIndex + j, root2.getChild(j));
        }
    }

    public BTreeNode<T> getRoot() {
        return root;
    }
}
