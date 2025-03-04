package com.nickslibrary.datastructures.heaps;

import java.util.ArrayList;
import java.util.List;

/**
 * A Fibonacci Heap is a data structure that consists of a collection of
 * heap-ordered trees.
 * It supports efficient operations for merging heaps and extracting the minimum
 * element.
 *
 * @param <T> The type of elements in the Fibonacci Heap, which must be
 *            comparable.
 */
public class FibonacciHeap<T extends Comparable<T>> {

    private Node<T> minNode;
    private int size;

    /**
     * Constructs an empty Fibonacci Heap.
     */
    public FibonacciHeap() {
        this.minNode = null;
        this.size = 0;
    }

    /**
     * Inserts a new element into the Fibonacci Heap.
     *
     * @param value The value to insert into the heap.
     * @return The inserted node.
     */
    public Node<T> insert(T value) {
        Node<T> newNode = new Node<>(value);

        if (minNode == null) {
            minNode = newNode;
        } else {
            minNode.getPrev().setNext(newNode);
            newNode.setPrev(minNode.getPrev());
            minNode.setPrev(newNode);
            newNode.setNext(minNode);

            if (newNode.getValue().compareTo(minNode.getValue()) < 0) {
                minNode = newNode;
            }
        }

        size++;
        return newNode;
    }

    /**
     * Merges another Fibonacci Heap into this one.
     *
     * @param other The Fibonacci Heap to merge.
     */
    public void merge(FibonacciHeap<T> other) {
        if (other == null || other.minNode == null) {
            return;
        }

        if (this.minNode == null) {
            this.minNode = other.minNode;
        } else {
            Node<T> thisLast = this.minNode.getPrev();
            Node<T> otherLast = other.minNode.getPrev();

            this.minNode.setPrev(otherLast);
            otherLast.setNext(this.minNode);
            other.minNode.setPrev(thisLast);
            thisLast.setNext(other.minNode);

            if (other.minNode.getValue().compareTo(this.minNode.getValue()) < 0) {
                this.minNode = other.minNode;
            }
        }

        this.size += other.size;

        other.minNode = null;
        other.size = 0;
    }

    /**
     * Extracts and returns the minimum element from the Fibonacci Heap.
     *
     * @return The minimum element in the heap.
     * @throws IllegalStateException if the heap is empty.
     */
    public T extractMin() {
        if (minNode == null) {
            throw new IllegalStateException("Heap is empty");
        }

        Node<T> min = minNode;
        T minValue = min.getValue();

        if (min.getChild() != null) {
            Node<T> child = min.getChild();
            do {
                Node<T> nextChild = child.getNext();
                removeFromRootList(child); // Call to removeFromRootList
                insertIntoRootList(child);
                child.setParent(null);
                child = nextChild;
            } while (child != min.getChild());
        }

        removeFromRootList(min); // Call to removeFromRootList

        if (min.getNext() == min) {
            minNode = null;
        } else {
            minNode = min.getNext();
            consolidate();
        }

        size--;

        return minValue;
    }

    /**
     * Returns the minimum element without removing it.
     *
     * @return The minimum element in the heap.
     * @throws IllegalStateException if the heap is empty.
     */
    public T peekMin() {
        if (this.minNode == null) {
            throw new IllegalStateException("Heap is empty");
        }
        return this.minNode.getValue();
    }

    /**
     * Decreases the key of a node in the Fibonacci Heap.
     *
     * @param node   The node whose key should be decreased.
     * @param newKey The new value for the key.
     * @throws IllegalArgumentException if newKey is greater than the current key.
     */
    public void decreaseKey(Node<T> node, T newKey) {
        if (newKey.compareTo(node.getValue()) > 0) {
            throw new IllegalArgumentException("newKey cannot be greater than the current key.");
        }

        node.setValue(newKey);

        if (node.isMarked()) {
            return; // Do nothing if the node is marked for deletion
        }

        Node<T> parent = node.getParent();
        if (parent != null && newKey.compareTo(parent.getValue()) < 0) {
            cut(node, parent);
            cascadingCut(parent);
        }

        if (newKey.compareTo(this.minNode.getValue()) < 0) {
            this.minNode = node;
        }
    }

    /**
     * Cuts a node from its parent and adds it to the root list.
     *
     * @param node   The node to cut.
     * @param parent The parent of the node.
     */
    private void cut(Node<T> node, Node<T> parent) {
        parent.removeChild(node);
        addToRootList(node);
        node.setMarked(false);
    }

    /**
     * Performs a cascading cut starting from the parent node.
     *
     * @param node The parent node.
     */
    private void cascadingCut(Node<T> node) {
        Node<T> parent = node.getParent();
        if (parent != null) {
            if (!node.isMarked()) {
                node.setMarked(true);
            } else {
                if (!node.isMarked()) {
                    cut(node, parent);
                    cascadingCut(parent);
                }
            }
        }
    }

    /**
     * Deletes a node from the Fibonacci Heap.
     *
     * @param node The node to delete.
     */
    public void delete(Node<T> node) {
        node.setMarked(true);
    }

    /**
     * Consolidates the heap by combining trees of the same degree.
     */
    private void consolidate() {
        int maxDegree = (int) Math.floor(Math.log(size) / Math.log(2)) + 1;
        List<Node<T>> degreeTable = new ArrayList<>(maxDegree);

        for (int i = 0; i < maxDegree; i++) {
            degreeTable.add(null);
        }

        Node<T> current = minNode;
        List<Node<T>> roots = new ArrayList<>();
        do {
            roots.add(current);
            current = current.getNext();
        } while (current != minNode);

        for (Node<T> root : roots) {
            int degree = root.getDegree();
            while (degreeTable.get(degree) != null) {
                Node<T> other = degreeTable.get(degree);

                if (root.getValue().compareTo(other.getValue()) > 0) {
                    Node<T> temp = root;
                    root = other;
                    other = temp;
                }

                link(root, other); // Call to link method
                degreeTable.set(degree, null);

                degree++;
            }
            degreeTable.set(degree, root);
        }

        minNode = null;
        for (Node<T> node : degreeTable) {
            if (node != null) {
                if (minNode == null) {
                    minNode = node;
                } else {
                    insertIntoRootList(node);
                    if (node.getValue().compareTo(minNode.getValue()) < 0) {
                        minNode = node;
                    }
                }
            }
        }
    }

    /**
     * Removes a node from the root list.
     *
     * @param node The node to be removed from the root list.
     */
    private void removeNodeFromRootList(Node<T> node) {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
    }

    /**
     * Inserts a node into the root list.
     *
     * @param node The node to be inserted into the root list.
     */
    private void insertIntoRootList(Node<T> node) {
        node.setPrev(minNode.getPrev());
        node.setNext(minNode);
        minNode.getPrev().setNext(node);
        minNode.setPrev(node);
    }

    /**
     * Links two trees of the same degree.
     *
     * @param node1 The first tree's root.
     * @param node2 The second tree's root.
     */
    private void link(Node<T> node1, Node<T> node2) {
        // Make node2 a child of node1
        removeNodeFromRootList(node2);
        node2.setParent(node1);
        node2.setNext(node2);
        node2.setPrev(node2);
        if (node1.getChild() == null) {
            node1.setChild(node2);
        } else {
            node2.setNext(node1.getChild());
            node2.setPrev(node1.getChild().getPrev());
            node1.getChild().getPrev().setNext(node2);
            node1.getChild().setPrev(node2);
        }
        node1.setDegree(node1.getDegree() + 1);
        node2.setMarked(false);
    }

    /**
     * Removes a node from the root list.
     *
     * @param node The node to remove.
     */
    private void removeFromRootList(Node<T> node) {
        if (node.getNext() == node) {
            // Node is the only node in the root list
            minNode = null;
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
            if (minNode == node) {
                minNode = node.getNext();
            }
        }
        node.setNext(node);
        node.setPrev(node);
    }

    /**
     * Adds a node to the root list.
     *
     * @param node The node to add.
     */
    private void addToRootList(Node<T> node) {
        if (minNode == null) {
            // If the root list is empty, initialize it with the new node
            minNode = node;
            node.setNext(node);
            node.setPrev(node);
        } else {
            // Insert the node into the root list
            node.setPrev(minNode.getPrev());
            node.setNext(minNode);
            minNode.getPrev().setNext(node);
            minNode.setPrev(node);
        }
        node.setParent(null);
    }

    public static class Node<T extends Comparable<T>> {

        private T value;
        private int degree;
        private Node<T> parent;
        private Node<T> child;
        private Node<T> next;
        private Node<T> prev;
        private boolean marked;

        /**
         * Constructs a new node with the specified value.
         *
         * @param value The value to store in the node.
         */
        public Node(T value) {
            this.value = value;
            this.degree = 0;
            this.parent = null;
            this.child = null;
            this.next = this;
            this.prev = this;
            this.marked = false;
        }

        /**
         * Sets the next node in the list.
         *
         * @param next The next node.
         */
        public void setNext(Node<T> next) {
            this.next = next;
        }

        /**
         * Gets the next node in the list
         * 
         * @return next The next node.
         */
        public Node<T> getNext() {
            return this.next;
        }

        /**
         * Gets the previous node in the list.
         *
         * @return The previous node.
         */
        public Node<T> getPrev() {
            return this.prev;
        }

        /**
         * Sets the previous node in the list.
         *
         * @param prev The previous node.
         */
        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }

        /**
         * Sets the parent of this node.
         *
         * @param parent The parent node.
         */
        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        /**
         * Adds a child to this node.
         *
         * @param child The node to add as a child.
         */
        public void addChild(Node<T> child) {
            if (this.child == null) {
                this.child = child;
            } else {
                child.setNext(this.child);
                child.setPrev(this.child.getPrev());
                this.child.getPrev().setNext(child);
                this.child.setPrev(child);
            }
            child.setParent(this);
            this.degree++;
        }

        /**
         * Removes a child from this node.
         *
         * @param child The node to remove as a child.
         */
        public void removeChild(Node<T> child) {
            if (this.child == child) {
                if (child.getNext() == child) {
                    this.child = null;
                } else {
                    this.child = child.getNext();
                }
            }
            child.setPrev(null);
            child.setNext(null);
            child.setParent(null);
            this.degree--;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public int getDegree() {
            return degree;
        }

        public void setDegree(int degree) {
            this.degree = degree;
        }

        public Node<T> getParent() {
            return parent;
        }

        public Node<T> getChild() {
            return child;
        }

        public void setChild(Node<T> child) {
            this.child = child;
        }

        public boolean isMarked() {
            return marked;
        }

        public void setMarked(boolean marked) {
            this.marked = marked;
        }
    }
}