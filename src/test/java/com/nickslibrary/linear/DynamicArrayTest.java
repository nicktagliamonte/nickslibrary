package com.nickslibrary.linear;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nickslibrary.datastructures.linear.DynamicArray;

public class DynamicArrayTest {

    @Test
    public void testDefaultConstructor() {
        DynamicArray<Integer> arr = new DynamicArray<>();

        // Assert the array is not null and size is 0
        assertNotNull(arr, "Array should not be null");
        assertEquals(0, arr.size(), "Size should be 0 after initialization");
        assertEquals(10, arr.capacity(), "Capacity should be 10 by default");
    }

    @Test
    public void testCapacityConstructor() {
        DynamicArray<Integer> arr = new DynamicArray<>(512);

        // Assert the array is not null and capacity is 512
        assertNotNull(arr, "Array should not be null");
        assertEquals(0, arr.size(), "Size should be 0 after initialization");
        assertEquals(512, arr.capacity(), "Capacity should be 10 by default");
    }

    @Test
    public void testListConstructor() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));

        DynamicArray<Integer> arr = new DynamicArray<>(list);

        assertNotNull(arr, "Array should not be null");
        assertEquals(3, arr.size(), "Size should be 3 after initialization");
        assertEquals(list.size(), arr.capacity(), "Capacity should match the list size");
    }

    @Test
    public void testAddElement() {
        DynamicArray<Integer> arr = new DynamicArray<>();

        arr.add(10);

        assertEquals(1, arr.size(), "Size should be 1 after adding an element");
        assertEquals(10, arr.get(0), "First element should be 10");
    }

    @Test
    public void testRetrieveElement() {
        DynamicArray<Integer> arr = new DynamicArray<>();
        arr.add(Arrays.asList(1, 20, 3));

        assertEquals(20, arr.get(1), "The element at index 1 should be 20");
    }

    @Test
    public void testRemoveElement() {
        DynamicArray<Integer> arr = new DynamicArray<>();
        arr.add(Arrays.asList(1, 20, 3));
        arr.remove(1);

        assertEquals(arr.size(), 2, "Array should have size of 2 after removing 1 of 3 elements");
        assertEquals(arr.get(1), 3, "element at index 1 should be 3");
    }

    @Test
    public void testTriggerResize() {
        DynamicArray<Integer> arr = new DynamicArray<>(Arrays.asList(1, 2, 3));
        assertEquals(3, arr.size());

        arr.add(Arrays.asList(10, 20, 30));

        assertEquals(6, arr.capacity(), "Capacity should double");
        assertEquals(6, arr.size(), "Size should match the number of elements after resize");

    }

    @Test
    public void testOutOfBounds() {
        DynamicArray<Integer> arr = new DynamicArray<>(Arrays.asList(10));

        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(-1),
                "Accessing index -1 should throw an exception");
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(1),
                "Accessing index 1 (when size is 1) should throw an exception");
    }

    @Test
    public void testShrinkResize() {
        DynamicArray<Integer> arr = new DynamicArray<>(8);
        arr.add(Arrays.asList(1, 2, 3, 4, 5));
        arr.remove(0);
        arr.remove(0);
        arr.remove(0);
        arr.remove(0);
        assertEquals(2, arr.capacity());
    }

    @Test
    void testAddAtIndex() {
        DynamicArray<Integer> dynamicArray = new DynamicArray<>();
        
        // Add elements at different positions
        dynamicArray.add(0, 10);  // Adding at the start
        dynamicArray.add(1, 20);  // Adding at the end
        dynamicArray.add(1, 15);  // Adding in the middle

        // Validate array contents
        assertEquals(3, dynamicArray.size(), "Size should be 3 after additions.");
        assertEquals(10, dynamicArray.get(0), "First element should be 10.");
        assertEquals(15, dynamicArray.get(1), "Second element should be 15.");
        assertEquals(20, dynamicArray.get(2), "Third element should be 20.");
    }

    @Test
    void testAddAtInvalidIndex() {
        DynamicArray<Integer> dynamicArray = new DynamicArray<>();
        
        dynamicArray.add(0, 10);
        dynamicArray.add(1, 20);

        // Test adding out of bounds
        dynamicArray.add(-1, 5);  // Invalid index, should not add
        dynamicArray.add(3, 30);   // Invalid index, should not add

        assertEquals(2, dynamicArray.size(), "Size should be 2 after invalid adds.");
    }

    @Test
    void testAddResizing() {
        DynamicArray<Integer> dynamicArray = new DynamicArray<>();
        
        // Adding a large number of elements to test resizing
        for (int i = 0; i < 1000; i++) {
            dynamicArray.add(i);
        }

        assertEquals(1000, dynamicArray.size(), "Size should be 1000 after adding elements.");
        assertTrue(dynamicArray.capacity() > 1000, "Capacity should have increased.");
    }
}