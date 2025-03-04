package com.nickslibrary.advanced;

import com.nickslibrary.datastructures.advanced.FenwickTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FenwickTreeTest {

    private FenwickTree fenwickTree;

    @BeforeEach
    void setUp() {
        fenwickTree = new FenwickTree(10);
    }

    @Test
    void testUpdateAndQuery() {
        fenwickTree.update(0, 1);
        fenwickTree.update(1, 2);
        fenwickTree.update(2, 3);
        assertEquals(1, fenwickTree.query(0));
        assertEquals(3, fenwickTree.query(1));
        assertEquals(6, fenwickTree.query(2));
    }

    @Test
    void testRangeQuery() {
        fenwickTree.update(0, 1);
        fenwickTree.update(1, 2);
        fenwickTree.update(2, 3);
        assertEquals(6, fenwickTree.rangeQuery(0, 2));
        assertEquals(5, fenwickTree.rangeQuery(1, 2));
    }

    @Test
    void testReset() {
        fenwickTree.update(0, 1);
        fenwickTree.update(1, 2);
        fenwickTree.reset();
        assertEquals(0, fenwickTree.query(0));
        assertEquals(0, fenwickTree.query(1));
    }

    @Test
    void testGetAndSet() {
        fenwickTree.update(0, 1);
        fenwickTree.update(1, 2);
        assertEquals(1, fenwickTree.get(0));
        assertEquals(2, fenwickTree.get(1));
        fenwickTree.set(1, 5);
        assertEquals(5, fenwickTree.get(1));
    }

    @Test
    void testConstructFromArray() {
        int[] array = { 1, 2, 3, 4, 5 };
        fenwickTree.constructFromArray(array);
        assertEquals(1, fenwickTree.query(0));
        assertEquals(3, fenwickTree.query(1));
        assertEquals(6, fenwickTree.query(2));
        assertEquals(10, fenwickTree.query(3));
        assertEquals(15, fenwickTree.query(4));
    }
}