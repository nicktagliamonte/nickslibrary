package com.nickslibrary.datastructures.linear;

import java.util.Arrays;
import java.util.List;

public class DynamicArray<T> {

    // Fields
    private Object[] array; // Internal storage
    private int size; // Number of elements
    private int capacity; // Max capacity before resizing

    // Constructors
    public DynamicArray() {
        this.capacity = 10; // Default capacity
        this.array = new Object[capacity];
        this.size = 0;
    }

    public DynamicArray(int initialCapacity) {
        this.capacity = initialCapacity;
        this.array = new Object[capacity]; // Internal storage as Object array
        this.size = 0;
    }

    public DynamicArray(List<T> items) {
        this.capacity = items.size();
        this.array = new Object[capacity];
        this.size = 0;
        for (T item : items) {
            add(item);
        }
    }

    // Standard methods
    // Adds an element to the dynamic array
    // If the array is full, it will resize the array to accommodate the new element
    public void add(T element) {
        if (size >= capacity) {
            resize();
        }
        array[size++] = element;
    }

    // Adds a list of elements to the dynamic array
    // Each element in the list is added individually to the dynamic array
    public void add(List<T> items) {
        for (T item : items) {
            add(item); // Reuse the existing add method which will handle resizing
        }
    }

    // Retrieves the element at the specified index
    // Throws IndexOutOfBoundsException if the index is invalid
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (T) array[index];
    }

    // Removes the element at the specified index
    // Shifts all elements after the index to the left to fill the gap
    // Reduces the size by 1 and nullifies the last element to avoid memory leaks
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // Shift the elements to the left to remove the element at the index
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[--size] = null; // Nullify the last element to avoid memory leaks

        // Shrink the array if necessary (when significantly underutilized, rather than
        // at every time utilization falls to 50%)
        if (size > 0 && size <= capacity / 4) {
            resizeShrink();
        }
    }

    // Resizes the internal array to accommodate more elements
    // The capacity is doubled every time resizing occurs
    private void resize() {
        capacity *= 2;
        array = Arrays.copyOf(array, capacity);
        Arrays.fill(array, size, array.length, null);
    }

    // Resizes the internal array to accommodate fewer elements
    // The capacity is halved every time shrink resizing occurs
    private void resizeShrink() {
        capacity /= 2; // Halve the capacity
        array = Arrays.copyOf(array, capacity); // Resize the array to the smaller capacity
    }

    // Utility Methods
    // Returns the number of elements currently in the array
    public int size() {
        return size;
    }

    // Returns the current capacity of the array
    public int capacity() {
        return capacity;
    }

    // Converts the array to a string representation
    // This method is useful for printing the array content
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
}
