package com.nickslibrary.datastructures.heaps;

import java.util.Collection;

/**
 * A MaxHeap data structure that maintains the largest element at the root.
 * The heap property ensures that each parent is greater than or equal to its
 * children.
 *
 * @param <T> The type of elements in the heap, which must be comparable.
 */
public class MaxHeap<T extends Comparable<T>> {

    private T[] heap;
    private int size;
    private int capacity;

    /**
     * Constructs a MaxHeap with a given initial capacity.
     *
     * @param initialCapacity The initial capacity of the heap.
     */
    @SuppressWarnings("unchecked")
    public MaxHeap(int initialCapacity) {
        this.capacity = Math.max(initialCapacity, 10);
        this.size = 0;
        this.heap = (T[]) new Comparable[capacity];
    }

    /**
     * Constructs a MaxHeap with the elements from the specified collection.
     *
     * @param collection A collection of elements to add to the heap.
     */
    @SuppressWarnings("unchecked")
    public MaxHeap(Collection<T> collection) {
        this.capacity = Math.max(collection.size(), 10);
        this.size = 0;
        this.heap = (T[]) new Comparable[capacity];

        // Add all elements from the collection to the heap
        for (T element : collection) {
            insert(element);
        }
    }

    /**
     * Inserts a new element into the heap.
     *
     * @param value The value to insert.
     */
    public void insert(T value) {
        ensureCapacity();

        heap[size] = value;
        size++;

        heapifyUp(size - 1);
    }

    /**
     * Ensures that the heap array has enough space to accommodate more elements.
     */
    private void ensureCapacity() {
        if (size == heap.length) {
            resize();
        }
    }

    /**
     * Doubles the capacity of the heap array when full.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = heap.length * 2;
        T[] newHeap = (T[]) new Comparable[newCapacity];

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

        ensureCapacity(newSize);

        System.arraycopy(values, 0, heap, size, values.length);
        size = newSize;

        for (int i = parent(size - 1); i >= 0; i--) {
            heapifyDown(i);
        }
    }

    /**
     * Ensures that the heap has enough capacity to accommodate the specified number
     * of elements.
     *
     * @param newSize The new size the heap should accommodate.
     */
    private void ensureCapacity(int newSize) {
        if (newSize > heap.length) {
            resize();
        }
    }

    /**
     * Removes and returns the maximum element from the heap.
     *
     * @return The largest element in the heap.
     * @throws IllegalStateException if the heap is empty.
     */
    public T extractMax() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        T max = heap[0];

        heap[0] = heap[size - 1];
        size--;

        heapifyDown(0);

        return max;
    }

    /**
     * Returns the maximum element without removing it.
     *
     * @return The largest element in the heap.
     * @throws IllegalStateException if the heap is empty.
     */
    public T peek() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap[0];
    }

    /**
     * Restores the heap property by moving an element up the heap.
     *
     * @param index The index of the element to move up.
     */
    private void heapifyUp(int index) {
        while (index > 0 && heap[parent(index)].compareTo(heap[index]) < 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    /**
     * Restores the heap property by moving an element down the heap.
     *
     * @param index The index of the element to move down.
     */
    private void heapifyDown(int index) {
        int largest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < size && heap[left].compareTo(heap[largest]) > 0) {
            largest = left;
        }
        if (right < size && heap[right].compareTo(heap[largest]) > 0) {
            largest = right;
        }
        if (largest != index) {
            swap(index, largest);
            heapifyDown(largest);
        }
    }

    /**
     * Swaps two elements in the heap.
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
     * Clears all elements from the heap.
     */
    public void clear() {
        size = 0;
    }
}
