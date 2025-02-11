package com.nickslibrary.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.linear.Deque;

import static org.junit.jupiter.api.Assertions.*;

class DequeTest {
    private Deque<Integer> deque;

    @BeforeEach
    void setUp() {
        deque = new Deque<>();
    }

    @Test
    void testAddFront() {
        deque.addFront(10);
        deque.addFront(20);
        deque.addFront(30);
        assertEquals(30, deque.peekFront());
        assertEquals(10, deque.peekRear());
        assertEquals(3, deque.size());
    }

    @Test
    void testAddRear() {
        deque.addRear(10);
        deque.addRear(20);
        deque.addRear(30);
        assertEquals(10, deque.peekFront());
        assertEquals(30, deque.peekRear());
        assertEquals(3, deque.size());
    }

    @Test
    void testRemoveFront() {
        deque.addFront(10);
        deque.addFront(20);
        deque.addFront(30);
        assertEquals(30, deque.removeFront());
        assertEquals(20, deque.peekFront());
        assertEquals(2, deque.size());
    }

    @Test
    void testRemoveRear() {
        deque.addRear(10);
        deque.addRear(20);
        deque.addRear(30);
        assertEquals(30, deque.removeRear());
        assertEquals(20, deque.peekRear());
        assertEquals(2, deque.size());
    }

    @Test
    void testPeekFrontAndRear() {
        deque.addRear(10);
        deque.addRear(20);
        assertEquals(10, deque.peekFront());
        assertEquals(20, deque.peekRear());
    }

    @Test
    void testIsEmpty() {
        assertTrue(deque.isEmpty());
        deque.addFront(5);
        assertFalse(deque.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, deque.size());
        deque.addRear(1);
        deque.addRear(2);
        assertEquals(2, deque.size());
    }

    @Test
    void testClear() {
        deque.addFront(5);
        deque.addFront(10);
        deque.clear();
        assertTrue(deque.isEmpty());
    }

    @Test
    void testContains() {
        deque.addRear(10);
        deque.addRear(20);
        assertTrue(deque.contains(10));
        assertFalse(deque.contains(30));
    }

    @Test
    void testToArray() {
        deque.addRear(10);
        deque.addRear(20);
        deque.addRear(30);
        Integer[] expected = {10, 20, 30};
        assertArrayEquals(expected, deque.toArray());
    }

    @Test
    void testReverse() {
        deque.addRear(10);
        deque.addRear(20);
        deque.addRear(30);
        deque.reverse();
        assertEquals(30, deque.peekFront());
        assertEquals(10, deque.peekRear());
    }

    @Test
    void testRemoveFrontOnEmptyDequeThrowsException() {
        assertThrows(IllegalStateException.class, () -> deque.removeFront());
    }

    @Test
    void testRemoveRearOnEmptyDequeThrowsException() {
        assertThrows(IllegalStateException.class, () -> deque.removeRear());
    }
}