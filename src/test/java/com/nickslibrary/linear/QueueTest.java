package com.nickslibrary.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.linear.CircularQueue;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    private CircularQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new CircularQueue<>(5); // Capacity of 5 for testing
    }

    @Test
    void testEnqueueAndPeek() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(1, queue.peek(), "Peek should return the first element.");
    }

    @Test
    void testDequeue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(1, queue.dequeue(), "Dequeue should return the front element.");
        assertEquals(2, queue.peek(), "Peek should return the new front element.");
    }

    @Test
    void testIsEmpty() {
        assertTrue(queue.isEmpty(), "Queue should be empty initially.");

        queue.enqueue(1);
        assertFalse(queue.isEmpty(), "Queue should not be empty after enqueue.");
    }

    @Test
    void testIsFull() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);

        assertTrue(queue.isFull(), "Queue should be full after enqueuing the capacity.");
    }

    @Test
    void testSize() {
        queue.enqueue(1);
        queue.enqueue(2);

        assertEquals(2, queue.size(), "Size should return the number of elements in the queue.");
    }

    @Test
    void testClear() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        queue.clear();
        assertTrue(queue.isEmpty(), "Queue should be empty after clear.");
    }

    @Test
    void testContains() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertTrue(queue.contains(2), "Queue should contain the element 2.");
        assertFalse(queue.contains(4), "Queue should not contain the element 4.");
    }

    @Test
    void testEnqueueFullQueue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);

        // Queue is full now
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            queue.enqueue(6);
        });

        assertEquals("The queue is full.", exception.getMessage(), "Enqueue should throw exception when the queue is full.");
    }

    @Test
    void testDequeueEmptyQueue() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            queue.dequeue();
        });

        assertEquals("The queue is empty.", exception.getMessage(), "Dequeue should throw exception when the queue is empty.");
    }

    @Test
    void testPeekEmptyQueue() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            queue.peek();
        });

        assertEquals("The queue is empty.", exception.getMessage(), "Peek should throw exception when the queue is empty.");
    }

    @Test
    void testCircularBehavior() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.dequeue(); // Removes 1

        queue.enqueue(4);
        queue.enqueue(5);

        assertTrue(queue.contains(2), "Queue should contain element 2 after wrap-around.");
        assertTrue(queue.contains(5), "Queue should contain element 5 after wrap-around.");
    }
}