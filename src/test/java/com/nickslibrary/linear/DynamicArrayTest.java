package com.nickslibrary.linear;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.nickslibrary.datastructures.linear.DynamicArray;

public class DynamicArrayTest {
    
    @Test
    public void testAddElement() {
        DynamicArray<Integer> arr = new DynamicArray<>();

        arr.add(10);

        assertEquals(1, arr.size(), "Size should be 1 after adding an element");
        assertEquals(10, arr.get(0), "First element should be 10");
    }
}