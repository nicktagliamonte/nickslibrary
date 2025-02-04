package com.nickslibrary.linear;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.linear.LinkedList;

public class LinkedListTest {
    public LinkedList<Integer> circularSinglyLinked;
    public LinkedList<Integer> circularDoublyLinked;
    public LinkedList<Integer> nonCircularSinglyLinked;
    public LinkedList<Integer> nonCircularDoublyLinked;

    @BeforeEach
    void setUp() {
        circularSinglyLinked = new LinkedList<>(true, true);
        circularDoublyLinked = new LinkedList<>(true, false);
        nonCircularSinglyLinked = new LinkedList<>(false, true);
        nonCircularDoublyLinked = new LinkedList<>(false, false);
    }

    @Test
    void testConstructorInitialization() {
        // Verify all lists are initialized with correct properties
        @SuppressWarnings("unchecked")
        LinkedList<Integer>[] lists = new LinkedList[] {
                circularSinglyLinked, circularDoublyLinked, nonCircularSinglyLinked, nonCircularDoublyLinked
        };

        boolean[] expectedCircular = { true, true, false, false };
        boolean[] expectedSinglyLinked = { true, false, true, false };

        for (int i = 0; i < lists.length; i++) {
            LinkedList<Integer> list = lists[i];
            assertNull(list.head, "Head should be null on initialization");
            assertNull(list.tail, "Tail should be null on initialization");
            assertEquals(0, list.size(), "Size should be 0 on initialization");
            assertEquals(expectedCircular[i], list.isCircular, "Circular flag mismatch");
            assertEquals(expectedSinglyLinked[i], list.isSinglyLinked, "Singly linked flag mismatch");
        }
    }

    @Test
    void testAdd() {
        @SuppressWarnings("unchecked")
        LinkedList<Integer>[] lists = new LinkedList[] {
                circularSinglyLinked, circularDoublyLinked, nonCircularSinglyLinked, nonCircularDoublyLinked
        };

        for (LinkedList<Integer> list : lists) {
            list.add(10);
            list.add(20);
            list.add(30);

            assertEquals(3, list.size(), "Size should be 3 after adding three elements");
            assertEquals(10, list.get(0), "Head should contain the first inserted element");
            assertEquals(30, list.get(2), "Tail should contain the last inserted element");
        }
    }

    @Test
    void testCircularLists() {
        circularSinglyLinked.add(10);
        circularSinglyLinked.add(20);
        circularSinglyLinked.add(30);

        assertEquals(10, circularSinglyLinked.get(3), "In a circular list, index 3 should wrap around to index 0");

        circularDoublyLinked.add(10);
        circularDoublyLinked.add(20);
        circularDoublyLinked.add(30);

        assertEquals(10, circularDoublyLinked.get(3),
                "Circular doubly linked list should wrap around like a singly circular list");
    }

    @Test
    void testInsertAtHead() {
        nonCircularSinglyLinked.insert(0, 10);
        nonCircularSinglyLinked.insert(0, 20);

        assertEquals(20, nonCircularSinglyLinked.get(0), "First element should be inserted at the head.");
        assertEquals(10, nonCircularSinglyLinked.get(1), "Second element should be at index 1.");
    }

    @Test
    void testInsertAtTail() {
        nonCircularSinglyLinked.insert(0, 10);
        nonCircularSinglyLinked.insert(1, 20);
        nonCircularSinglyLinked.insert(2, 30);

        assertEquals(30, nonCircularSinglyLinked.get(2), "Element should be inserted at the tail.");
    }

    @Test
    void testInsertInMiddle() {
        nonCircularSinglyLinked.insert(0, 10);
        nonCircularSinglyLinked.insert(1, 20);
        nonCircularSinglyLinked.insert(1, 15);

        assertEquals(15, nonCircularSinglyLinked.get(1), "Element should be inserted in the middle.");
        assertEquals(20, nonCircularSinglyLinked.get(2), "Element should push the previous element to the right.");
    }

    @Test
    void testInsertAtHeadCircularSinglyLinked() {
        circularSinglyLinked.insert(0, 10);
        circularSinglyLinked.insert(0, 20);

        assertEquals(20, circularSinglyLinked.get(0), "First element should be inserted at the head.");
        assertEquals(10, circularSinglyLinked.get(1), "Second element should be at index 1.");
        assertEquals(20, circularSinglyLinked.get(2),
                "List should wrap around correctly in circular singly linked list.");
    }

    @Test
    void testInsertAtTailCircularDoublyLinked() {
        circularDoublyLinked.insert(0, 10);
        circularDoublyLinked.insert(1, 20);
        circularDoublyLinked.insert(2, 30);

        assertEquals(30, circularDoublyLinked.get(2), "Element should be inserted at the tail.");
        assertEquals(10, circularDoublyLinked.get(0), "First element should still be at the head.");
        assertEquals(20, circularDoublyLinked.get(1), "Second element should be in the middle.");
    }

    @Test
    void testInsertOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            nonCircularSinglyLinked.insert(-1, 10); // Negative index
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            nonCircularSinglyLinked.insert(1, 10); // Index greater than size (empty list)
        });
    }

    @Test
    void testRemove() {
        // Test removal from a non-circular, singly linked list
        nonCircularSinglyLinked.add(10);
        nonCircularSinglyLinked.add(20);
        nonCircularSinglyLinked.add(30);

        nonCircularSinglyLinked.remove(20);
        assertEquals(10, nonCircularSinglyLinked.get(0));
        assertEquals(30, nonCircularSinglyLinked.get(1));

        // Test removing head from a circular singly linked list
        circularSinglyLinked.add(10);
        circularSinglyLinked.add(20);
        circularSinglyLinked.add(30);
        circularSinglyLinked.remove(10);
        assertEquals(20, circularSinglyLinked.get(0));
        assertEquals(30, circularSinglyLinked.get(1));

        // Test removal of tail from a circular doubly linked list
        circularDoublyLinked.add(10);
        circularDoublyLinked.add(20);
        circularDoublyLinked.add(30);
        circularDoublyLinked.remove(30);
        assertEquals(10, circularDoublyLinked.get(0));
        assertEquals(20, circularDoublyLinked.get(1));

        // Test removal of a middle element from a non-circular doubly linked list
        nonCircularDoublyLinked.add(10);
        nonCircularDoublyLinked.add(20);
        nonCircularDoublyLinked.add(30);
        nonCircularDoublyLinked.remove(20);
        assertEquals(10, nonCircularDoublyLinked.get(0));
        assertEquals(30, nonCircularDoublyLinked.get(1));
    }

    @Test
    void testRemoveAt() {
        // Test for non-circular singly linked list
        nonCircularSinglyLinked.add(10);
        nonCircularSinglyLinked.add(20);
        nonCircularSinglyLinked.add(30);
        nonCircularSinglyLinked.add(40);

        // Remove head
        //nonCircularSinglyLinked.removeAt(0);
        //assertEquals(20, nonCircularSinglyLinked.get(0)); // Ensure 20 is now at index 0
        //assertEquals(30, nonCircularSinglyLinked.get(1)); // Ensure 30 is now at index 1

        // Remove tail
        nonCircularSinglyLinked.removeAt(1); // Tail is at index 1
        assertEquals(40, nonCircularSinglyLinked.get(2)); // Ensure 20 is the only element left

        // Test for circular singly linked list
        circularSinglyLinked.add(10);
        circularSinglyLinked.add(20);
        circularSinglyLinked.add(30);

        // Remove head
        circularSinglyLinked.removeAt(0);
        assertEquals(20, circularSinglyLinked.get(0)); // Ensure 20 is now at index 0
        assertEquals(30, circularSinglyLinked.get(1)); // Ensure 30 is now at index 1

        // Remove tail (in circular list, this should update the circular link)
        circularSinglyLinked.removeAt(1); // Tail is at index 1 (circular)
        assertEquals(20, circularSinglyLinked.get(0)); // Only one element left, 20

        // Test for circular doubly linked list
        circularDoublyLinked.add(10);
        circularDoublyLinked.add(20);
        circularDoublyLinked.add(30);

        // Remove tail
        circularDoublyLinked.removeAt(2); // Tail is at index 2
        assertEquals(20, circularDoublyLinked.get(1)); // 20 should be at index 1 now

        // Test for non-circular doubly linked list
        nonCircularDoublyLinked.add(10);
        nonCircularDoublyLinked.add(20);
        nonCircularDoublyLinked.add(30);

        // Remove middle
        nonCircularDoublyLinked.removeAt(1); // Remove element at index 1
        assertEquals(10, nonCircularDoublyLinked.get(0)); // 10 should be at index 0
        assertEquals(30, nonCircularDoublyLinked.get(1)); // 30 should be at index 1

        // Remove tail
        nonCircularDoublyLinked.removeAt(1); // Tail is at index 1
        assertEquals(20, nonCircularDoublyLinked.get(0)); // Only one element left, 20
    }
}
