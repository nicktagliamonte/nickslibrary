package com.nickslibrary.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.linear.StackArray;
import com.nickslibrary.datastructures.linear.StackLinkedList;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {
    private StackArray<Integer> stackArray;
    private StackLinkedList<Integer> stackLinkedList;

    @BeforeEach
    void setUp() {
        stackArray = new StackArray<>();
        stackLinkedList = new StackLinkedList<>();
    }

    @Test
    void testPushAndPop() {
        stackArray.push(1);
        stackArray.push(2);
        stackArray.push(3);
        assertEquals(3, stackArray.pop());
        assertEquals(2, stackArray.pop());
        assertEquals(1, stackArray.pop());
        assertNull(stackArray.pop()); // Empty stack case

        stackLinkedList.push(1);
        stackLinkedList.push(2);
        stackLinkedList.push(3);
        assertEquals(3, stackLinkedList.pop());
        assertEquals(2, stackLinkedList.pop());
        assertEquals(1, stackLinkedList.pop());
        assertNull(stackLinkedList.pop()); // Empty stack case
    }

    @Test
    void testPeek() {
        stackArray.push(10);
        assertEquals(10, stackArray.peek());
        stackArray.push(20);
        assertEquals(20, stackArray.peek());
        stackArray.pop();
        assertEquals(10, stackArray.peek());
        stackArray.pop();
        assertNull(stackArray.peek()); // Empty stack

        stackLinkedList.push(10);
        assertEquals(10, stackLinkedList.peek());
        stackLinkedList.push(20);
        assertEquals(20, stackLinkedList.peek());
        stackLinkedList.pop();
        assertEquals(10, stackLinkedList.peek());
        stackLinkedList.pop();
        assertNull(stackLinkedList.peek()); // Empty stack
    }

    @Test
    void testIsEmptyAndSize() {
        assertTrue(stackArray.isEmpty());
        assertEquals(0, stackArray.size());

        stackArray.push(5);
        assertFalse(stackArray.isEmpty());
        assertEquals(1, stackArray.size());

        stackArray.pop();
        assertTrue(stackArray.isEmpty());
        assertEquals(0, stackArray.size());

        assertTrue(stackLinkedList.isEmpty());
        assertEquals(0, stackLinkedList.size());

        stackLinkedList.push(5);
        assertFalse(stackLinkedList.isEmpty());
        assertEquals(1, stackLinkedList.size());

        stackLinkedList.pop();
        assertTrue(stackLinkedList.isEmpty());
        assertEquals(0, stackLinkedList.size());
    }

    @Test
    void testClear() {
        stackArray.push(1);
        stackArray.push(2);
        stackArray.clear();
        assertTrue(stackArray.isEmpty());
        assertEquals(0, stackArray.size());

        stackLinkedList.push(1);
        stackLinkedList.push(2);
        stackLinkedList.clear();
        assertTrue(stackLinkedList.isEmpty());
        assertEquals(0, stackLinkedList.size());
    }

    @Test
    void testContains() {
        stackArray.push(5);
        stackArray.push(10);
        stackArray.push(15);
        assertTrue(stackArray.contains(10));
        assertFalse(stackArray.contains(20));

        stackLinkedList.push(5);
        stackLinkedList.push(10);
        stackLinkedList.push(15);
        assertTrue(stackLinkedList.contains(10));
        assertFalse(stackLinkedList.contains(20));
    }

    @Test
    void testToArray() {
        stackArray.push(1);
        stackArray.push(2);
        stackArray.push(3);
        assertArrayEquals(new Integer[]{3, 2, 1}, stackArray.toArray());

        stackLinkedList.push(1);
        stackLinkedList.push(2);
        stackLinkedList.push(3);
        assertArrayEquals(new Integer[]{3, 2, 1}, stackLinkedList.toArray());
    }

    @Test
    void testCapacityAndShrinkToFit() {
        assertEquals(10, stackArray.capacity()); // Assuming initial DynamicArray capacity is 10
        for (int i = 0; i < 15; i++) {
            stackArray.push(i);
        }
        assertTrue(stackArray.capacity() >= 15);
        stackArray.shrinkToFit();
        assertEquals(stackArray.size(), stackArray.capacity());
    }
}
