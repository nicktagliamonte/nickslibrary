package com.nickslibrary.datastructures.trees;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

import com.nickslibrary.utils.tree.RedBlackTreeNode;

/**
 * A Red-Black Tree implementation.
 *
 * @param <T> The type of value stored in the tree.
 */
public class RedBlackTree<T extends Comparable<T>> implements Iterable<T> {
    private RedBlackTreeNode<T> root;

    /**
     * Inserts a value into the Red-Black Tree.
     *
     * @param value The value to insert.
     */
    @SuppressWarnings("null")
    public void insert(T value) {
        RedBlackTreeNode<T> newNode = new RedBlackTreeNode<>(value);
        if (root == null) {
            root = newNode;
            root.setBlack(); // Root must always be black
            return;
        }

        RedBlackTreeNode<T> parent = null;
        RedBlackTreeNode<T> current = root;

        // Standard BST insertion
        while (current != null) {
            parent = current;
            if (value.compareTo(current.getValue()) < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        // Assign parent and insert node
        newNode.setParent(parent);
        if (value.compareTo(parent.getValue()) < 0) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }

        // Set new node as red
        newNode.setRed();

        // Fix Red-Black Tree property violations
        fixInsertion(newNode);
    }

    /**
     * Deletes a value from the Red-Black Tree.
     *
     * @param value The value to delete.
     */
    public void delete(T value) {
        RedBlackTreeNode<T> nodeToDelete = search(value);
        if (nodeToDelete == null) {
            return; // Value not found, nothing to delete.
        }

        RedBlackTreeNode<T> y = nodeToDelete; // Node to be physically removed.
        RedBlackTreeNode<T> x; // Node that will replace y.
        RedBlackTreeNode.Color originalColor = y.getColor();

        if (nodeToDelete.getLeft() == null) {
            x = nodeToDelete.getRight();
            transplant(nodeToDelete, nodeToDelete.getRight());
        } else if (nodeToDelete.getRight() == null) {
            x = nodeToDelete.getLeft();
            transplant(nodeToDelete, nodeToDelete.getLeft());
        } else {
            y = findMin(nodeToDelete.getRight()); // Successor
            originalColor = y.getColor();
            x = y.getRight();

            if (y.getParent() == nodeToDelete) {
                if (x != null) {
                    x.setParent(y);
                }
            } else {
                transplant(y, y.getRight());
                y.setRight(nodeToDelete.getRight());
                y.getRight().setParent(y);
            }

            transplant(nodeToDelete, y);
            y.setLeft(nodeToDelete.getLeft());
            y.getLeft().setParent(y);
            y.setColor(nodeToDelete.getColor());
        }

        if (originalColor == RedBlackTreeNode.Color.BLACK) {
            fixDeletion(x);
        }
    }

    /**
     * Replaces one subtree as a child of its parent with another subtree.
     * This method is used during deletion to move subtrees around efficiently.
     *
     * @param target      The node to be replaced.
     * @param replacement The node that will take the place of the target node.
     *                    If {@code replacement} is {@code null}, the target node is
     *                    simply removed.
     */
    private void transplant(RedBlackTreeNode<T> u, RedBlackTreeNode<T> v) {
        if (u.getParent() == null) {
            root = v;
        } else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v);
        } else {
            u.getParent().setRight(v);
        }
        if (v != null) {
            v.setParent(u.getParent());
        }
    }

    /**
     * Searches for a node with the given value.
     *
     * @param value The value to search for.
     * @return The node containing the value, or null if not found.
     */
    public RedBlackTreeNode<T> search(T value) {
        RedBlackTreeNode<T> current = root;

        while (current != null) {
            int cmp = value.compareTo(current.getValue());
            if (cmp == 0) {
                return current; // Value found
            } else if (cmp < 0) {
                current = current.getLeft(); // Search left subtree
            } else {
                current = current.getRight(); // Search right subtree
            }
        }

        return null; // Value not found
    }

    /**
     * Retrieves the value from a node containing the given value.
     *
     * @param value The value to find.
     * @return The value stored in the node, or null if not found.
     */
    public T get(T value) {
        RedBlackTreeNode<T> node = search(value);
        return (node != null) ? node.getValue() : null;
    }

    /**
     * Inserts multiple values into the tree.
     *
     * @param values A collection of values to insert.
     */
    public void insertAll(Collection<T> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        for (T value : values) {
            insert(value);
        }
    }

    /**
     * Deletes multiple values from the tree.
     *
     * @param values A collection of values to delete.
     */
    public void deleteAll(Collection<T> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        for (T value : values) {
            delete(value);
        }
    }

    /**
     * Performs an in-order traversal of the tree.
     *
     * @return A list of values in sorted order.
     */
    public List<T> inOrderTraversal() {
        List<T> result = new ArrayList<>();
        inOrderTraversalRecursive(root, result);
        return result;
    }

    /**
     * Helper method to perform an in-order traversal recursively.
     *
     * @param node   The current node in the traversal.
     * @param result The list storing the traversal result.
     */
    private void inOrderTraversalRecursive(RedBlackTreeNode<T> node, List<T> result) {
        if (node == null) {
            return;
        }
        inOrderTraversalRecursive(node.getLeft(), result);
        result.add(node.getValue());
        inOrderTraversalRecursive(node.getRight(), result);
    }

    /**
     * Performs a pre-order traversal of the tree.
     *
     * @return A list of values in pre-order.
     */
    public List<T> preOrderTraversal() {
        List<T> result = new ArrayList<>();
        preOrderHelper(root, result);
        return result;
    }

    /**
     * Helper method for pre-order traversal.
     *
     * @param node   The current node.
     * @param result The list to store the traversal order.
     */
    private void preOrderHelper(RedBlackTreeNode<T> node, List<T> result) {
        if (node != null) {
            result.add(node.getValue()); // Visit the node
            preOrderHelper(node.getLeft(), result); // Traverse left subtree
            preOrderHelper(node.getRight(), result); // Traverse right subtree
        }
    }

    /**
     * Performs a post-order traversal of the tree.
     *
     * @return A list of values in post-order.
     */
    public List<T> postOrderTraversal() {
        List<T> result = new ArrayList<>();
        postOrderHelper(root, result);
        return result;
    }

    /**
     * Helper method for post-order traversal.
     *
     * @param node   The current node.
     * @param result The list to store the traversal order.
     */
    private void postOrderHelper(RedBlackTreeNode<T> node, List<T> result) {
        if (node != null) {
            postOrderHelper(node.getLeft(), result); // Traverse left subtree
            postOrderHelper(node.getRight(), result); // Traverse right subtree
            result.add(node.getValue()); // Visit the node
        }
    }

    /**
     * Performs a level-order traversal of the tree.
     *
     * @return A list of values in level order.
     */
    public List<T> levelOrderTraversal() {
        List<T> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<RedBlackTreeNode<T>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            RedBlackTreeNode<T> current = queue.poll();
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
     * Finds the node with the minimum value in the entire tree.
     *
     * @return The node containing the minimum value.
     */
    public RedBlackTreeNode<T> findMin() {
        if (root == null) {
            return null;
        }
        return findMin(root);
    }

    /**
     * Finds the minimum node starting from a given subtree.
     * 
     * @param node the root of the subtree in question.
     * @return The node containing the minimum value.
     */
    private RedBlackTreeNode<T> findMin(RedBlackTreeNode<T> node) {
        if (node == null)
            return null;
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Finds the node with the maximum value in the tree.
     *
     * @return The node containing the maximum value, or null if the tree is empty.
     */
    public RedBlackTreeNode<T> findMax() {
        if (root == null) {
            return null;
        }
        return findMax(root);
    }

    /**
     * Helper method to find the maximum value node starting from a given node.
     *
     * @param node The starting node.
     * @return The node containing the maximum value.
     */
    private RedBlackTreeNode<T> findMax(RedBlackTreeNode<T> node) {
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    /**
     * Finds the in-order successor of a given value in the tree.
     *
     * @param value The value whose successor is to be found.
     * @return The successor node, or null if no successor exists.
     */
    public RedBlackTreeNode<T> successor(T value) {
        RedBlackTreeNode<T> node = search(value);
        if (node == null) {
            return null;
        }
        return successor(node);
    }

    /**
     * Helper method to find the in-order successor of a given node.
     *
     * @param node The node whose successor is to be found.
     * @return The successor node, or null if no successor exists.
     */
    private RedBlackTreeNode<T> successor(RedBlackTreeNode<T> node) {
        if (node.getRight() != null) {
            return findMin(node.getRight());
        }

        RedBlackTreeNode<T> parent = node.getParent();
        while (parent != null && node == parent.getRight()) {
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * Finds the in-order predecessor of a given value in the tree.
     *
     * @param value The value whose predecessor is to be found.
     * @return The predecessor node, or null if no predecessor exists.
     */
    public RedBlackTreeNode<T> predecessor(T value) {
        RedBlackTreeNode<T> node = search(value);
        if (node == null) {
            return null;
        }
        return predecessor(node);
    }

    /**
     * Helper method to find the in-order predecessor of a given node.
     *
     * @param node The node whose predecessor is to be found.
     * @return The predecessor node, or null if no predecessor exists.
     */
    private RedBlackTreeNode<T> predecessor(RedBlackTreeNode<T> node) {
        if (node.getLeft() != null) {
            return findMax(node.getLeft());
        }

        RedBlackTreeNode<T> parent = node.getParent();
        while (parent != null && node == parent.getLeft()) {
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * Clears the tree by setting the root to null.
     */
    public void clear() {
        this.root = null;
    }

    /**
     * Checks if the tree is empty.
     *
     * @return True if the tree is empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the size of the tree.
     *
     * @return The number of nodes in the tree.
     */
    public int size() {
        return sizeRecursive(root);
    }

    /**
     * Helper method to calculate the size of the tree recursively.
     *
     * @param node The current node.
     * @return The number of nodes in the subtree rooted at this node.
     */
    private int sizeRecursive(RedBlackTreeNode<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + sizeRecursive(node.getLeft()) + sizeRecursive(node.getRight());
    }

    /**
     * Validates whether the tree satisfies all Red-Black Tree properties.
     *
     * @return True if the tree is a valid Red-Black Tree, false otherwise.
     */
    public boolean isValidRedBlackTree() {
        if (root == null) {
            return true; // An empty tree is a valid Red-Black Tree
        }

        // Property 1: Root must be black
        if (root.isRed()) {
            return false;
        }

        // Validate Red-Black properties
        return isValidRedBlackSubtree(root) != -1;
    }

    /**
     * Helper method to check if a subtree satisfies Red-Black Tree properties.
     *
     * @param node The current node being checked.
     * @return The black-height of the subtree if valid, or -1 if invalid.
     */
    private int isValidRedBlackSubtree(RedBlackTreeNode<T> node) {
        if (node == null) {
            return 1; // Null (leaf) nodes contribute a black height of 1
        }

        // Property 2: No two consecutive red nodes
        if (node.isRed() && ((node.getLeft() != null && node.getLeft().isRed()) ||
                (node.getRight() != null && node.getRight().isRed()))) {
            return -1;
        }

        // Compute black heights using getBlackHeight()
        int leftBlackHeight = getBlackHeight(node.getLeft());
        int rightBlackHeight = getBlackHeight(node.getRight());

        // Property 3: Both left and right subtrees must be valid and have the same
        // black height
        if (leftBlackHeight == -1 || rightBlackHeight == -1 || leftBlackHeight != rightBlackHeight) {
            return -1;
        }

        // Property 4: Count black nodes along the path
        return leftBlackHeight + (node.isBlack() ? 1 : 0);
    }

    /**
     * Computes the black-height of a node.
     *
     * @param node The node to check.
     * @return The number of black nodes from this node to a leaf.
     */
    private int getBlackHeight(RedBlackTreeNode<T> node) {
        int blackHeight = 0;
        while (node != null) {
            if (node.isBlack()) {
                blackHeight++;
            }
            node = node.getLeft(); // Always follow the leftmost path
        }
        return blackHeight;
    }

    /**
     * Performs a left rotation at the given node.
     *
     * @param node The node to rotate.
     */
    private void rotateLeft(RedBlackTreeNode<T> node) {
        if (node == null || node.getRight() == null) {
            return; // Cannot rotate left if there is no right child
        }

        RedBlackTreeNode<T> rightChild = node.getRight();
        node.setRight(rightChild.getLeft());

        if (rightChild.getLeft() != null) {
            rightChild.getLeft().setParent(node);
        }

        rightChild.setParent(node.getParent());

        if (node.getParent() == null) {
            root = rightChild; // If node was root, update root
        } else if (node == node.getParent().getLeft()) {
            node.getParent().setLeft(rightChild);
        } else {
            node.getParent().setRight(rightChild);
        }

        rightChild.setLeft(node);
        node.setParent(rightChild);
    }

    /**
     * Performs a right rotation at the given node.
     *
     * @param node The node to rotate.
     */
    private void rotateRight(RedBlackTreeNode<T> node) {
        if (node == null || node.getLeft() == null) {
            return; // Cannot rotate right if there is no left child
        }

        RedBlackTreeNode<T> leftChild = node.getLeft();
        node.setLeft(leftChild.getRight());

        if (leftChild.getRight() != null) {
            leftChild.getRight().setParent(node);
        }

        leftChild.setParent(node.getParent());

        if (node.getParent() == null) {
            root = leftChild; // If node was root, update root
        } else if (node == node.getParent().getRight()) {
            node.getParent().setRight(leftChild);
        } else {
            node.getParent().setLeft(leftChild);
        }

        leftChild.setRight(node);
        node.setParent(leftChild);
    }

    /**
     * Fixes any violations of Red-Black Tree properties after an insertion.
     *
     * @param node The node that was inserted.
     */
    private void fixInsertion(RedBlackTreeNode<T> node) {
        while (node != root && node.getParent().isRed()) {
            RedBlackTreeNode<T> parent = node.getParent();
            RedBlackTreeNode<T> grandparent = parent.getParent();

            if (parent == grandparent.getLeft()) {
                RedBlackTreeNode<T> uncle = grandparent.getRight();

                if (uncle != null && uncle.isRed()) {
                    // Case 1: Recoloring
                    parent.setBlack();
                    uncle.setBlack();
                    grandparent.setRed();
                    node = grandparent;
                } else {
                    if (node == parent.getRight()) {
                        // Case 2: Left rotation to convert to Case 3
                        node = parent;
                        rotateLeft(node);
                    }
                    // Case 3: Right rotation and recoloring
                    parent.setBlack();
                    grandparent.setRed();
                    rotateRight(grandparent);
                }
            } else {
                RedBlackTreeNode<T> uncle = grandparent.getLeft();

                if (uncle != null && uncle.isRed()) {
                    // Case 1: Recoloring
                    parent.setBlack();
                    uncle.setBlack();
                    grandparent.setRed();
                    node = grandparent;
                } else {
                    if (node == parent.getLeft()) {
                        // Case 2: Right rotation to convert to Case 3
                        node = parent;
                        rotateRight(node);
                    }
                    // Case 3: Left rotation and recoloring
                    parent.setBlack();
                    grandparent.setRed();
                    rotateLeft(grandparent);
                }
            }
        }
        root.setBlack();
    }

    /**
     * Fixes any violations of Red-Black Tree properties after a deletion.
     *
     * @param node The node that replaces the deleted node.
     */
    private void fixDeletion(RedBlackTreeNode<T> node) {
        if (node == null) {
            return; // No fix needed if the tree is empty after deletion
        }

        while (node != root && node.isBlack()) {
            RedBlackTreeNode<T> parent = node.getParent();

            if (parent == null) {
                break; // If parent is null, we are at the root, and no further fixes are needed
            }

            if (node == parent.getLeft()) {
                RedBlackTreeNode<T> sibling = parent.getRight();

                if (sibling != null && sibling.isRed()) {
                    sibling.setBlack();
                    parent.setRed();
                    rotateLeft(parent);
                    sibling = parent.getRight();
                }

                if (sibling != null && sibling.getLeft() != null && sibling.getRight() != null &&
                        sibling.getLeft().isBlack() && sibling.getRight().isBlack()) {
                    sibling.setRed();
                    node = parent;
                } else {
                    if (sibling != null && sibling.getRight() != null && sibling.getRight().isBlack()) {
                        if (sibling.getLeft() != null)
                            sibling.getLeft().setBlack();
                        sibling.setRed();
                        rotateRight(sibling);
                        sibling = parent.getRight();
                    }

                    if (sibling != null) {
                        sibling.setColor(parent.getColor());
                        parent.setBlack();
                        if (sibling.getRight() != null)
                            sibling.getRight().setBlack();
                        rotateLeft(parent);
                    }
                    node = root;
                }
            } else {
                RedBlackTreeNode<T> sibling = parent.getLeft();

                if (sibling != null && sibling.isRed()) {
                    sibling.setBlack();
                    parent.setRed();
                    rotateRight(parent);
                    sibling = parent.getLeft();
                }

                if (sibling != null && sibling.getRight() != null && sibling.getLeft() != null &&
                        sibling.getRight().isBlack() && sibling.getLeft().isBlack()) {
                    sibling.setRed();
                    node = parent;
                } else {
                    if (sibling != null && sibling.getLeft() != null && sibling.getLeft().isBlack()) {
                        if (sibling.getRight() != null)
                            sibling.getRight().setBlack();
                        sibling.setRed();
                        rotateLeft(sibling);
                        sibling = parent.getLeft();
                    }

                    if (sibling != null) {
                        sibling.setColor(parent.getColor());
                        parent.setBlack();
                        if (sibling.getLeft() != null)
                            sibling.getLeft().setBlack();
                        rotateRight(parent);
                    }
                    node = root;
                }
            }
        }

        if (node != null) {
            node.setBlack(); // Ensure node is black to maintain properties
        }
    }

    /**
     * Creates a deep copy of the Red-Black Tree.
     *
     * @return A new RedBlackTree instance that is a copy of this tree.
     */
    public RedBlackTree<T> clone() {
        RedBlackTree<T> clonedTree = new RedBlackTree<>();
        if (root != null) {
            clonedTree.root = cloneSubtree(root, null);
        }
        return clonedTree;
    }

    /**
     * Recursively clones a subtree.
     *
     * @param node   The current node to clone.
     * @param parent The parent of the cloned node.
     * @return The cloned node.
     */
    private RedBlackTreeNode<T> cloneSubtree(RedBlackTreeNode<T> node, RedBlackTreeNode<T> parent) {
        if (node == null) {
            return null;
        }

        RedBlackTreeNode<T> clonedNode = new RedBlackTreeNode<>(node.getValue());
        clonedNode.setColor(node.getColor());
        clonedNode.setParent(parent);

        clonedNode.setLeft(cloneSubtree(node.getLeft(), clonedNode));
        clonedNode.setRight(cloneSubtree(node.getRight(), clonedNode));

        return clonedNode;
    }

    /**
     * Returns an iterator to traverse the tree in-order.
     *
     * @return An iterator for in-order traversal.
     */
    @Override
    public Iterator<T> iterator() {
        return new InOrderIterator(root);
    }

    /**
     * Private inner class for in-order traversal using an iterator.
     */
    private class InOrderIterator implements Iterator<T> {
        private final Stack<RedBlackTreeNode<T>> stack = new Stack<>();

        public InOrderIterator(RedBlackTreeNode<T> root) {
            pushLeft(root);
        }

        private void pushLeft(RedBlackTreeNode<T> node) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            RedBlackTreeNode<T> node = stack.pop();
            pushLeft(node.getRight());

            return node.getValue();
        }
    }
}
