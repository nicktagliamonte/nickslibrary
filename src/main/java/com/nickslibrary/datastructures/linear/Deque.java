package com.nickslibrary.datastructures.linear;

/**
 * A Deque (Double-Ended Queue) implementation using a doubly linked list.
 * Supports insertion and deletion from both front and rear in O(1) time.
 *
 * @param <T> The type of elements stored in the deque.
 */
public class Deque<T> {
    private LinkedList<T> list;

    /**
     * Constructs an empty deque using a doubly linked list.
     */
    public Deque() {
        list = new LinkedList<>(false, false); // Non-circular, doubly linked
    }

    /**
     * Adds an element to the front of the deque.
     * 
     * @param data The element to add.
     */
    public void addFront(T data) {
        list.insert(0, data);
    }

    /**
     * Adds an element to the rear of the deque.
     * 
     * @param data The element to add.
     */
    public void addRear(T data) {
        list.insert(list.size(), data);
    }

    /**
     * Removes and returns the front element of the deque.
     * 
     * @return The removed element, or null if the deque is empty.
     * @throws IllegalStateException if the deque is empty
     */
    public T removeFront() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }

        T data = list.get(0);
        list.removeAt(0);
        return data;
    }    

    /**
     * Removes and returns the rear element of the deque.
     * 
     * @return The removed element, or null if the deque is empty.
     * * @throws IllegalStateException if the deque is empty
     */
    public T removeRear() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }

        T data = list.get(list.size() - 1);
        list.removeAt(list.size() - 1);
        return data;
    }

    /**
     * Retrieves, but does not remove, the front element of the deque.
     * 
     * @return The front element, or null if the deque is empty.
     */
    public T peekFront() {
        return list.get(0);
    }

    /**
     * Retrieves, but does not remove, the rear element of the deque.
     * 
     * @return The rear element, or null if the deque is empty.
     */
    public T peekRear() {
        return list.get(list.size() - 1);
    }

    /**
     * Checks if the deque is empty.
     * 
     * @return True if the deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the deque.
     * 
     * @return The size of the deque.
     */
    public int size() {
        return list.size();
    }

    /**
     * Clears the deque, removing all elements.
     */
    public void clear() {
        list.clear();
    }

    /**
     * Checks if the deque contains a specific element.
     * 
     * @param data The element to check for.
     * @return True if the element is found, false otherwise.
     */
    public boolean contains(T data) {
        return list.contains(data);
    }

    /**
     * Returns an array representation of the deque.
     * 
     * @return An array containing all elements in the deque.
     */
    public T[] toArray() {
        return list.toArray();
    }

    /**
     * Prints the deque elements in order.
     */
    public void printDeque() {
        list.printList();
    }

    /**
     * Reverses the deque
     * although who knows why you'd want to.
     */
    public void reverse() {
        list.reverse();
    }
}
