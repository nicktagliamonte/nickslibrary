/**
 * A dynamic array implementation that automatically resizes as elements are added or removed.
 * 
 * This class provides a resizable array with the ability to add, remove, and access elements
 * efficiently. The array doubles in capacity when it becomes full and shrinks when the number of
 * elements falls below a certain threshold. It also supports indexing and iteration for ease of use.
 * 
 * @param <T> the type of elements in the dynamic array
 */
package com.nickslibrary.datastructures.linear;

import java.util.Arrays;
import java.util.List;

public class DynamicArray<T> {

    // Fields
    private Object[] array; // Internal storage
    private int size; // Number of elements
    private int capacity; // Max capacity before resizing

    // Constructors

    /**
     * Constructs a dynamic array with a default initial capacity of 10.
     */
    public DynamicArray() {
        this.capacity = 10; // Default capacity
        this.array = new Object[capacity];
        this.size = 0;
    }

    /**
     * Constructs a dynamic array with the specified initial capacity.
     * 
     * @param initialCapacity the initial capacity of the array
     */
    public DynamicArray(int initialCapacity) {
        this.capacity = initialCapacity;
        this.array = new Object[capacity]; // Internal storage as Object array
        this.size = 0;
    }

    /**
     * Constructs a dynamic array with the elements from the provided list.
     * 
     * @param items the list of items to initialize the array with
     */
    public DynamicArray(List<T> items) {
        this.capacity = items.size();
        this.array = new Object[capacity];
        this.size = 0;
        for (T item : items) {
            add(item);
        }
    }

    // Standard methods

    /**
     * Adds an element to the dynamic array.
     * If the array is full, it will resize the array to accommodate the new
     * element.
     * 
     * @param element the element to add to the array
     */
    public void add(T element) {
        if (size >= capacity) {
            resize();
        }
        array[size++] = element;
    }

    /**
     * Adds a list of elements to the dynamic array.
     * Each element in the list is added individually to the dynamic array.
     * 
     * @param items the list of elements to add to the array
     */
    public void add(List<T> items) {
        for (T item : items) {
            add(item); // Reuse the existing add method which will handle resizing
        }
    }

    /**
     * Adds an element at the specified index.
     * If the array is full, it will resize the array to accommodate the new
     * element.
     * 
     * @param index   the index to insert the element at
     * @param element the element to insert at the specified index
     */
    public void add(int index, T element) {
        if (index < 0 || index > size)
            return; // Index must be in range [0, size]

        if (size >= capacity) {
            resize();
        }

        // Shift elements starting from the index
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }

        array[index] = element; // Insert element at the specified index
        size++; // Increment size
    }

    /**
     * Retrieves the element at the specified index.
     * 
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= capacity) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds. Size of array is " + size);
        }
        return (T) array[index];
    }

    /**
     * Removes the element at the specified index.
     * Shifts all elements after the index to the left to fill the gap.
     * Reduces the size by 1 and nullifies the last element to avoid memory leaks.
     * 
     * @param index the index of the element to remove
     */
    public void remove(int index) {
        if (index < 0 || index >= capacity) {
            throw new IndexOutOfBoundsException();
        }

        // Shift the elements to the left to remove the element at the index
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        if (size != 0) {
            array[--size] = null; // Nullify the last element to avoid memory leaks
        }        

        // Shrink the array if necessary (when significantly underutilized, rather than
        // at every time utilization falls to 50%)
        if (size > 0 && size <= capacity / 4) {
            resizeShrink();
        }
    }

    /**
     * Resizes the internal array to accommodate more elements.
     * The capacity is doubled every time resizing occurs.
     */
    public void resize() {
        capacity *= 2;
        array = Arrays.copyOf(array, capacity);
        Arrays.fill(array, size, array.length, null);
    }

    /**
     * Resizes the internal array to accommodate fewer elements.
     * The capacity is halved every time shrink resizing occurs.
     */
    private void resizeShrink() {
        capacity /= 2; // Halve the capacity
        array = Arrays.copyOf(array, capacity); // Resize the array to the smaller capacity
    }

    // Utility Methods

    /**
     * Returns the number of elements currently in the array.
     * 
     * @return the number of elements in the array
     */
    public int size() {
        return size;
    }

    /**
     * Returns the current capacity of the array.
     * 
     * @return the current capacity of the array
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Converts the array to a string representation.
     * This method is useful for printing the array content.
     * 
     * @return a string representation of the array
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1)
                sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Reduces the capacity to match the current size.
     */
    public void shrinkToFit() {
        this.capacity = this.size;
    }

    /**
     * Removes all elements from the array.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }
}
