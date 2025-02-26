package com.nickslibrary.trees;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.trees.BinarySearchTree;

import static org.junit.jupiter.api.Assertions.*;

class BSTTest {
    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    @Test
    void testInsertAndContains() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);

        assertTrue(bst.contains(10));
        assertTrue(bst.contains(5));
        assertTrue(bst.contains(15));
        assertFalse(bst.contains(20));
    }

    @Test
    void testDelete() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(12);

        assertTrue(bst.contains(10));
        bst.delete(10);
        assertFalse(bst.contains(10));
    }

    @Test
    void testPreorderTraversal() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);
        bst.insert(13);
        bst.insert(17);

        List<Integer> expected = Arrays.asList(10, 5, 3, 7, 15, 13, 17);
        assertEquals(expected, bst.preorderTraversal());
    }

    @Test
    void testPostorderTraversal() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);
        bst.insert(13);
        bst.insert(17);

        List<Integer> expected = Arrays.asList(3, 7, 5, 13, 17, 15, 10);
        assertEquals(expected, bst.postorderTraversal());
    }

    @Test
    void testLevelOrderTraversal() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);
        bst.insert(13);
        bst.insert(17);

        List<Integer> expected = Arrays.asList(10, 5, 15, 3, 7, 13, 17);
        assertEquals(expected, bst.levelOrderTraversal());
    }

    @Test
    void testGet() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);

        assertEquals(10, bst.get(10));
        assertEquals(5, bst.get(5));
        assertEquals(15, bst.get(15));
        assertNull(bst.get(20)); // Value not in tree
    }

    @Test
    void testGetNode() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);

        assertNotNull(bst.getNode(10));
        assertNotNull(bst.getNode(5));
        assertNotNull(bst.getNode(15));
        assertNull(bst.getNode(20)); // Non-existent value
    }

    @Test
    void testFindMin() {
        assertNull(bst.findMin()); // Empty tree
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(2);
        assertEquals(2, bst.findMin());
    }

    @Test
    void testFindMax() {
        assertNull(bst.findMax()); // Empty tree
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(20);
        assertEquals(20, bst.findMax());
    }

    @Test
    void testGetHeight() {
        assertEquals(-1, bst.getHeight()); // Empty tree
        bst.insert(10);
        assertEquals(0, bst.getHeight()); // Single node
        bst.insert(5);
        bst.insert(15);
        bst.insert(2);
        bst.insert(1);
        assertEquals(3, bst.getHeight()); // Unbalanced tree
    }

    @Test
    void testSize() {
        assertEquals(0, bst.size()); // Empty tree
        bst.insert(10);
        assertEquals(1, bst.size());
        bst.insert(5);
        bst.insert(15);
        bst.insert(20);
        assertEquals(4, bst.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(bst.isEmpty());
        bst.insert(10);
        assertFalse(bst.isEmpty());
    }

    @Test
    void testClear() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.clear();
        assertTrue(bst.isEmpty());
    }
}
