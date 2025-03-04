package com.nickslibrary.advanced;

import com.nickslibrary.datastructures.advanced.SegmentTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SegmentTreeTest {

    private SegmentTree segmentTree;

    @BeforeEach
    void setUp() {
        int[] array = { 1, 2, 3, 4, 5 };
        segmentTree = new SegmentTree(array);
    }

    @Test
    void testBuildAndQuery() {
        assertEquals(1, segmentTree.query(0, 0));
        assertEquals(3, segmentTree.query(0, 1));
        assertEquals(6, segmentTree.query(0, 2));
        assertEquals(10, segmentTree.query(0, 3));
        assertEquals(15, segmentTree.query(0, 4));
    }

    @Test
    void testUpdateAndQuery() {
        segmentTree.update(1, 10);
        assertEquals(1, segmentTree.query(0, 0));
        assertEquals(11, segmentTree.query(0, 1));
        assertEquals(14, segmentTree.query(0, 2));
        assertEquals(18, segmentTree.query(0, 3));
        assertEquals(23, segmentTree.query(0, 4));
    }

    @Test
    void testRangeQuery() {
        assertEquals(9, segmentTree.query(1, 3));
        assertEquals(12, segmentTree.query(2, 4));
    }
}