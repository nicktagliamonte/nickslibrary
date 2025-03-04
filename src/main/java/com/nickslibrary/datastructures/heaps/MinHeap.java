package com.nickslibrary.datastructures.heaps;

import java.util.Arrays;
import java.util.Collection;

/**
 * Implementation of a MinHeap using an array-based structure.
 *
 * @param <T> The type of elements in the heap, must be Comparable.
 */
public class MinHeap<T extends Comparable<T>> {
    private T[] heap;
    private int size;
    private int capacity;

    /**
     * Constructs a MinHeap with the given initial capacity.
     *
     * @param capacity The maximum number of elements the heap can hold initially.
     */
    @SuppressWarnings("unchecked")
    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = (T[]) new Comparable[capacity]; // Generic array allocation
    }

    /**
     * Constructs a MinHeap from an existing collection of elements.
     * This efficiently builds the heap using the heapify process.
     *
     * @param elements The collection of elements to initialize the heap.
     */
    @SuppressWarnings("unchecked")
    public MinHeap(Collection<T> elements) {
        // Ensure minimum capacity and initialize size and heap array
        this.capacity = Math.max(elements.size(), 10); // Ensure a minimum capacity
        this.size = elements.size();
        this.heap = (T[]) new Comparable[capacity];

        // Ensure sufficient capacity for the elements
        ensureCapacity();

        // Copy elements into the array
        int i = 0;
        for (T element : elements) {
            heap[i++] = element;
        }

        // Heapify process: start from the last non-leaf node and move downward
        for (int j = parent(size - 1); j >= 0; j--) {
            heapifyDown(j);
        }
    }

    /**
     * Returns the index of the parent node.
     *
     * @param index The index of the child node.
     * @return The index of the parent node.
     */
    private int parent(int index) {
        return (index - 1) / 2;
    }

    /**
     * Returns the index of the left child node.
     *
     * @param index The index of the parent node.
     * @return The index of the left child node.
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    /**
     * Returns the index of the right child node.
     *
     * @param index The index of the parent node.
     * @return The index of the right child node.
     */
    private int rightChild(int index) {
        return 2 * index + 2;
    }

    /**
     * Inserts a new element into the heap.
     *
     * @param value The value to insert.
     */
    public void insert(T value) {
        // Ensure capacity before inserting
        ensureCapacity();

        // Place the new element at the next available position
        heap[size] = value;
        size++;

        // Restore heap property by bubbling up
        heapifyUp(size - 1);
    }

    /**
     * Ensures that the heap array has enough space to accommodate more elements.
     */
    private void ensureCapacity() {
        if (size == heap.length) {
            resize(); // Resize the heap array if it's full
        }
    }

    /**
     * Doubles the capacity of the heap array when full.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = heap.length * 2;
        T[] newHeap = (T[]) new Comparable[newCapacity];

        // Copy elements to the new array
        System.arraycopy(heap, 0, newHeap, 0, size);

        heap = newHeap;
    }

    /**
     * Inserts multiple elements into the heap.
     * This method efficiently builds the heap using the heapify process.
     *
     * @param values The array of values to insert.
     */
    public void insertAll(T[] values) {
        int newSize = size + values.length;

        // Ensure capacity before inserting all elements
        ensureCapacity(newSize);

        // Copy new elements to the heap array
        System.arraycopy(values, 0, heap, size, values.length);
        size = newSize;

        // Heapify: restore heap property from the last non-leaf node
        for (int i = parent(size - 1); i >= 0; i--) {
            heapifyDown(i);
        }
    }

    /**
     * Ensures the heap has at least the given capacity.
     * 
     * @param newCapacity The required new capacity.
     */
    private void ensureCapacity(int newCapacity) {
        if (newCapacity > heap.length) {
            resizeTo(newCapacity);
        }
    }

    /**
     * Resizes the heap to at least the given capacity.
     * 
     * @param newCapacity The required new capacity.
     */
    @SuppressWarnings("unchecked")
    private void resizeTo(int newCapacity) {
        int newSize = Math.max(newCapacity, heap.length * 2);
        T[] newHeap = (T[]) new Comparable[newSize];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    /**
     * Removes and returns the minimum element from the heap.
     *
     * @return The smallest element in the heap.
     * @throws IllegalStateException if the heap is empty.
     */
    public T extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty.");
        }

        T minValue = heap[0]; // The root holds the minimum value
        heap[0] = heap[size - 1]; // Move the last element to the root
        heap[size - 1] = null; // Optional: Help with garbage collection
        size--;

        heapifyDown(0); // Restore heap property

        return minValue;
    }

    /**
     * Returns the minimum element without removing it.
     *
     * @return The smallest element in the heap.
     * @throws IllegalStateException if the heap is empty.
     */
    public T peek() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty.");
        }
        return heap[0];
    }

    /**
     * Restores the heap property by moving an element up the heap.
     *
     * @param index The index of the element to move up.
     */
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = parent(index);

            // If the current node is smaller than its parent, swap them
            if (heap[index].compareTo(heap[parentIndex]) < 0) {
                swap(index, parentIndex);
                index = parentIndex; // Move up to the parent index
            } else {
                break; // Stop if the heap property is satisfied
            }
        }
    }

    /**
     * Restores the heap property by moving an element down the heap.
     *
     * @param index The index of the element to move down.
     */
    private void heapifyDown(int index) {
        while (true) {
            int left = leftChild(index);
            int right = rightChild(index);
            int smallest = index;

            // Find the smallest among index, left child, and right child
            if (left < size && heap[left].compareTo(heap[smallest]) < 0) {
                smallest = left;
            }

            if (right < size && heap[right].compareTo(heap[smallest]) < 0) {
                smallest = right;
            }

            // If the smallest is not the current index, swap and continue down
            if (smallest != index) {
                swap(index, smallest);
                index = smallest; // Move index down
            } else {
                break; // Stop if heap property is satisfied
            }
        }
    }

    /**
     * Swaps two elements in the heap array.
     *
     * @param i The index of the first element.
     * @param j The index of the second element.
     */
    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Returns the number of elements in the heap.
     *
     * @return The size of the heap.
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the heap is empty.
     *
     * @return True if the heap is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all elements from the heap.
     */
    public void clear() {
        // Set size to 0
        size = 0;

        Arrays.fill(heap, null);
    }
}