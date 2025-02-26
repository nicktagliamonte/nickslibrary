/**
 * This class represents a stack data structure implemented using an array-based dynamic array.
 * It provides the standard stack operations such as push, pop, peek, and checking if the stack
 * is empty. Additionally, methods to check the stack's size, capacity, and elements, as well as 
 * to clear and convert the stack to an array, are included.
 * 
 * @param <T> The type of elements in the list.
 */
package com.nickslibrary.datastructures.linear;

public class StackArray<T> {
    private DynamicArray<T> array;

    /**
     * Constructs an empty stack.
     */
    public StackArray() {
        array = new DynamicArray<>();
    }

    /**
     * Pushes an element onto the top of the stack.
     *
     * @param data The element to be added.
     */
    public void push(T data) {
        array.add(data);
    }

    /**
     * Removes and returns the top element of the stack.
     *
     * @return The removed top element, or null if the stack is empty.
     */
    public T pop() {
        if (!isEmpty()) {
            T data = array.get(array.size() - 1);
            array.remove(size() - 1);
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
        return isEmpty() ? null : array.get(size() - 1);
    }

    /**
     * Checks if the stack is empty.
     *
     * @return True if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return The size of the stack.
     */
    public int size() {
        return array.size();
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear() {
        array.clear();
    }

    /**
     * Checks if the stack contains a specific element.
     *
     * @param data The element to check for.
     * @return True if the element is in the stack, false otherwise.
     */
    public boolean contains(T data) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equals(data)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an array representation of the stack.
     *
     * @return An array containing all elements in the stack.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        Object[] arr = new Object[array.size()];
        for (int i = 0; i < array.size(); i++) {
            arr[i] = array.get(array.size() - 1 - i);
        }
        return (T[]) arr;
    }

    /**
     * Returns the current capacity of the underlying DynamicArray.
     *
     * @return The current capacity.
     */
    public int capacity() {
        return array.capacity();
    }

    /**
     * Reduces the capacity of the underlying DynamicArray to match the current
     * size.
     */
    public void shrinkToFit() {
        array.shrinkToFit();
    }

    /**
     * Prints the stack elements from top to bottom.
     */
    public void printStack() {
        for (int i = array.size() - 1; i >= 0; i--) { // Start from the top of the stack
            System.out.print(array.get(i) + " ");
        }
    }
}