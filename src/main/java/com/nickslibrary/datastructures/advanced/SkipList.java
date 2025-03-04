package com.nickslibrary.datastructures.advanced;

import java.util.Random;

/**
 * A SkipList is a probabilistic data structure that allows fast search,
 * insertion, and deletion operations.
 *
 * @param <T> The type of elements in the SkipList, which must be comparable.
 */
public class SkipList<T extends Comparable<T>> {

    private static final int MAX_LEVEL = 16;
    private final Node<T> head;
    private final Random random;
    private int size;

    /**
     * Constructs an empty SkipList.
     */
    public SkipList() {
        head = new Node<>(null, MAX_LEVEL);
        random = new Random();
        size = 0;
    }

    /**
     * Inserts a value into the SkipList.
     *
     * @param value The value to insert.
     */
    public void insert(T value) {
        @SuppressWarnings("unchecked")
        Node<T>[] update = (Node<T>[]) new Node[MAX_LEVEL + 1];
        Node<T> current = head;

        // Start from the highest level of the skip list and move downwards
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].getValue().compareTo(value) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        // Move to the next node at level 0
        current = current.forward[0];

        // If the current node is null or its value is not equal to the value to be
        // inserted
        if (current == null || !current.getValue().equals(value)) {
            int level = randomLevel();
            Node<T> newNode = new Node<>(value, level);

            // Insert the new node by adjusting the forward pointers
            for (int i = 0; i <= level; i++) {
                newNode.forward[i] = update[i].forward[i];
                update[i].forward[i] = newNode;
            }

            size++;
        }
    }

    /**
     * Generates a random level for the new node.
     *
     * @return The random level.
     */
    private int randomLevel() {
        int level = 0;
        while (random.nextInt(2) == 0 && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }

    /**
     * Deletes a value from the SkipList.
     *
     * @param value The value to delete.
     */
    @SuppressWarnings("unchecked")
    public void delete(T value) {
        Node<T>[] update = new Node[MAX_LEVEL + 1];
        Node<T> current = head;

        // Start from the highest level of the skip list and move downwards
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].getValue().compareTo(value) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        // Move to the next node at level 0
        current = current.forward[0];

        // If the current node is not null and its value is equal to the value to be
        // deleted
        if (current != null && current.getValue().equals(value)) {
            // Adjust the forward pointers to remove the node
            for (int i = 0; i <= MAX_LEVEL; i++) {
                if (update[i].forward[i] != current) {
                    break;
                }
                update[i].forward[i] = current.forward[i];
            }

            size--;
        }
    }

    /**
     * Searches for a value in the SkipList.
     *
     * @param value The value to search for.
     * @return True if the value is found, false otherwise.
     */
    public boolean search(T value) {
        Node<T> current = head;

        // Start from the highest level of the skip list and move downwards
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].getValue().compareTo(value) < 0) {
                current = current.forward[i];
            }
        }

        // Move to the next node at level 0
        current = current.forward[0];

        // Check if the current node's value is equal to the value to be searched
        return current != null && current.getValue().equals(value);
    }

    /**
     * Prints the SkipList.
     */
    public void printList() {
        Node<T> current = head.forward[0];
        while (current != null) {
            System.out.print(current.getValue() + " ");
            current = current.forward[0];
        }
        System.out.println();
    }

    /**
     * Returns the number of elements in the SkipList.
     *
     * @return The number of elements in the SkipList.
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the SkipList is empty.
     *
     * @return True if the SkipList is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all elements from the SkipList.
     */
    public void clear() {
        for (int i = 0; i <= MAX_LEVEL; i++) {
            head.forward[i] = null;
        }
        size = 0;
    }

    /**
     * Node class for SkipList.
     *
     * @param <T> The type of elements in the node.
     */
    private static class Node<T> {
        private final T value;
        private final Node<T>[] forward;

        /**
         * Constructs a new node with the specified value and level.
         *
         * @param value The value to store in the node.
         * @param level The level of the node.
         */
        @SuppressWarnings("unchecked")
        public Node(T value, int level) {
            this.value = value;
            this.forward = new Node[level + 1];
        }

        /**
         * Gets the value of the node.
         *
         * @return The value of the node.
         */
        public T getValue() {
            return value;
        }
    }
}