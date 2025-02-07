/**
 * CircularQueue implementation using a fixed-size array.
 * Implements a FIFO queue with circular wrap-around to optimize space usage.
 */
package com.nickslibrary.datastructures.linear;

public class CircularQueue<T> {
    private T[] array;
    private int front, rear, size, capacity;

    /**
     * Constructs a CircularQueue with the given capacity.
     * 
     * @param capacity the maximum number of elements the queue can hold
     */
    @SuppressWarnings("unchecked")
    public CircularQueue(int capacity) {
        this.capacity = capacity;
        this.array = (T[]) new Object[capacity];
        this.front = -1; // Empty queue
        this.rear = -1;
        this.size = 0;
    }

    /**
     * Adds an element to the queue.
     * Wraps around if necessary.
     * 
     * @param data the element to add
     * @throws IllegalStateException if the queue is full
     */
    public void enqueue(T data) {
        if (isFull()) {
            throw new IllegalStateException("The queue is full.");
        }

        if (isEmpty()) {
            front = 0;
            rear = 0; // First element, rear should also be 0
        } else {
            rear = (rear + 1) % capacity; // Normal case: move rear forward
        }

        array[rear] = data;
        size++;
    }

    /**
     * Removes and returns the front element of the queue.
     * 
     * @return the front element
     * @throws IllegalStateException if the queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("The queue is empty.");
        }

        T data = array[front];
        array[front] = null;

        if (front == rear) {
            front = -1;
            rear = -1;
        } else {
            front = (front + 1) % capacity;
        }

        size--;
        return data;
    }

    /**
     * Returns the front element without removing it.
     * 
     * @return the front element
     * @throws IllegalStateException if the queue is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("The queue is empty.");
        }

        return array[front];
    }

    /**
     * Checks if the queue is empty.
     * 
     * @return true if the queue has no elements, false otherwise
     */
    public boolean isEmpty() {
        return front == -1;
    }

    /**
     * Checks if the queue is full.
     * 
     * @return true if the queue is at full capacity, false otherwise
     */
    public boolean isFull() {
        return ((rear + 1) % capacity) == front;
    }

    /**
     * Returns the current number of elements in the queue.
     * 
     * @return the size of the queue
     */
    public int size() {
        return size;
    }

    /**
     * Clears the queue, resetting it to an empty state.
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            array[i] = null;
        }

        front = -1;
        rear = -1;
        size = 0;
    }

    /**
     * Prints the elements of the queue in order.
     * Used for debugging purposes.
     */
    public void printQueue() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }

        int i = front;
        while (i != rear) {
            System.out.print(array[i] + " ");
            i = (i + 1) % capacity;
        }
        System.out.println(array[rear]); // Print the last element (rear)
    }

    /**
     * Searches the queue to see if it contains the data
     * 
     * @param data the element to search for
     * @return true if the data is found, false otherwise
     * @throws IllegalStateException if the queue is empty
     */
    public boolean contains(T data) {
        if (isEmpty()) {
            throw new IllegalStateException("The queue is empty.");
        }

        int i = front;
        while (i != rear) {
            if (array[i].equals(data)) {
                return true;
            }
            i = (i + 1) % capacity;
        }

        // Check the last element (rear)
        return array[rear].equals(data);
    }
}
