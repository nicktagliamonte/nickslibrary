package com.nickslibrary.datastructures.linear;

public class StackLinkedList<T> {
    private LinkedList<T> list;

    /**
     * Constructs an empty stack.
     */
    public StackLinkedList() {
        list = new LinkedList<>(false, true);
    }

    /**
     * Pushes an element onto the top of the stack.
     *
     * @param data The element to be added.
     */
    public void push(T data) {
        list.insert(0, data);
    }

    /**
     * Removes and returns the top element of the stack.
     *
     * @return The removed top element, or null if the stack is empty.
     */
    public T pop() {
        if (!isEmpty()) {
            T data = list.get(0);
            list.removeAt(0);
            return data;
        }
        return null;
    }

    /**
     * Retrieves, but does not remove, the top element of the stack.
     *
     * @return The top element, or null if the stack is empty.
     */
    public T peek() {
        return isEmpty() ? null : list.get(0);
    }

    /**
     * Checks if the stack is empty.
     *
     * @return True if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return The size of the stack.
     */
    public int size() {
        return list.size();
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear() {
        list.clear();
    }

    /**
     * Checks if the stack contains a specific element.
     *
     * @param data The element to check for.
     * @return True if the element is in the stack, false otherwise.
     */
    public boolean contains(T data) {
        return list.contains(data);
    }

    /**
     * Returns an array representation of the stack.
     *
     * @return An array containing all elements in the stack.
     */
    public T[] toArray() {
        return list.toArray();
    }

    /**
     * Prints the stack elements from top to bottom.
     */
    public void printStack() {
        list.printList();
    }
}
