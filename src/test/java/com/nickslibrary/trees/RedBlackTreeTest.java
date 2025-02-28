package com.nickslibrary.trees;

import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.trees.RedBlackTree;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class RedBlackTreeTest {

    @Test
    void testInsertionMaintainsRedBlackProperties() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(15);
        tree.insert(25);

        assertTrue(tree.isValidRedBlackTree(), "Tree should maintain Red-Black properties after insertion.");
    }

    @Test
    void testDeletionMaintainsRedBlackProperties() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(15);
        tree.insert(25);

        tree.delete(20);

        assertTrue(tree.isValidRedBlackTree(), "Tree should maintain Red-Black properties after deletion.");
    }

    @Test
    void testSize() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        assertEquals(0, tree.size());

        tree.insert(10);
        tree.insert(20);
        tree.insert(30);

        assertEquals(3, tree.size());

        tree.delete(20);
        assertEquals(2, tree.size());
    }

    @Test
    void testIsEmpty() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        assertTrue(tree.isEmpty());

        tree.insert(10);
        assertFalse(tree.isEmpty());

        tree.delete(10);
        assertTrue(tree.isEmpty());
    }

    @Test
    void testTraversals() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(15);
        tree.insert(25);

        assertEquals(List.of(10, 15, 20, 25, 30), tree.inOrderTraversal());
        assertEquals(List.of(20, 10, 15, 30, 25), tree.preOrderTraversal());
        assertEquals(List.of(15, 10, 25, 30, 20), tree.postOrderTraversal());
        assertEquals(List.of(20, 10, 30, 15, 25), tree.levelOrderTraversal());
    }
}
