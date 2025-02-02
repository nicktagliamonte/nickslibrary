package com.nickslibrary;

import com.nickslibrary.datastructures.linear.DynamicArray;

/**
 * TODO: basic testing of dynamic array
 * TODO: dynamicarray unit tests
 *
 */
public class Main {
    public static void main(String[] args) {
        // Test 1: Create a DynamicArray with default constructor
        DynamicArray<Integer> dynamicArray = new DynamicArray<>();
        System.out.println("Initial array: " + dynamicArray); // Should be empty []

        // Test 2: Add elements to the DynamicArray
        dynamicArray.add(10);
        dynamicArray.add(20);
        dynamicArray.add(30);
        System.out.println("After adding elements: " + dynamicArray); // Should be [10, 20, 30]

        // Test 3: Retrieve elements
        System.out.println("Element at index 1: " + dynamicArray.get(1)); // Should print 20

        // Test 4: Remove an element
        dynamicArray.remove(1); // Removes element at index 1
        System.out.println("After removing element at index 1: " + dynamicArray); // Should be [10, 30]

        // Test 5: Add more elements to trigger resizing
        dynamicArray.add(40);
        dynamicArray.add(50);
        System.out.println("After adding more elements: " + dynamicArray); // Should be [10, 30, 40, 50]

        // Test 6: Check the size and capacity
        System.out.println("Size of array: " + dynamicArray.size()); // Should be 4
        System.out.println("Capacity of array: " + dynamicArray.capacity()); // Should be more than 4 (resized)

        // Test 7: Try to access an out-of-bounds index
        try {
            dynamicArray.get(5); // This should throw an exception
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught exception: " + e.getMessage()); // Expecting "Index out of bounds"
        }

        // Test 8: Shrink the array by removing elements and see if it shrinks
        dynamicArray.remove(0); // Removes 10
        dynamicArray.remove(0); // Removes 30
        System.out.println("After removing elements: " + dynamicArray); // Should be [40, 50]
        System.out.println("Capacity after shrinking: " + dynamicArray.capacity()); // Should be smaller than before
    }
}
