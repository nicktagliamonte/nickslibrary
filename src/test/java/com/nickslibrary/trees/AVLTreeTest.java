package com.nickslibrary.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.trees.AVLTree;
import com.nickslibrary.utils.AVLTreeNode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {
    private AVLTree<Integer> tree;

    @BeforeEach
    void setUp() {
        tree = new AVLTree<>();
    }

    @Test
    void testInsertAndSearch() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30); // Should trigger a rotation
        assertTrue(tree.search(10));
        assertTrue(tree.search(20));
        assertTrue(tree.search(30));
        assertFalse(tree.search(40));
    }

    @Test
    void testDelete() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.delete(20);

        assertFalse(tree.search(20));
        assertTrue(tree.search(10));
        assertTrue(tree.search(30));
    }

    @Test
    void testRotations() {
        tree.insert(30);
        tree.insert(20);
        tree.insert(10); // Should cause right rotation

        assertEquals(20, tree.getRoot().getValue());
        assertEquals(10, tree.getRoot().getLeft().getValue());
        assertEquals(30, tree.getRoot().getRight().getValue());

        tree.insert(40);
        tree.insert(50); // Should cause left rotation

        assertEquals(20, tree.getRoot().getValue());
        assertEquals(10, tree.getRoot().getLeft().getValue());
        assertEquals(40, tree.getRoot().getRight().getValue());
    }

    @Test
    void testBalanceFactor() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        assertEquals(0, tree.getRoot().getBalanceFactor());
    }

    @Test
    void testInOrderTraversal() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        tree.insert(25);
        tree.insert(35);

        List<Integer> result = tree.inOrderTraversal();
        assertEquals(List.of(10, 20, 25, 30, 35), result);
    }

    @Test
    void testPreOrderTraversal() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        tree.insert(25);
        tree.insert(35);

        List<Integer> result = tree.preOrderTraversal();
        assertEquals(List.of(20, 10, 30, 25, 35), result);
    }

    @Test
    void testPostOrderTraversal() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        tree.insert(25);
        tree.insert(35);

        List<Integer> result = tree.postOrderTraversal();
        assertEquals(List.of(10, 25, 35, 30, 20), result);
    }

    @Test
    void testLevelOrderTraversal() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        tree.insert(25);
        tree.insert(35);

        List<Integer> result = tree.levelOrderTraversal();
        assertEquals(List.of(20, 10, 30, 25, 35), result);
    }

    @Test
    void testFindMax() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        tree.insert(25);
        tree.insert(35);

        AVLTreeNode<Integer> maxNode = tree.findMax();
        assertNotNull(maxNode);
        assertEquals(35, maxNode.getValue());
    }

    @Test
    void testClear() {
        tree.insert(10);
        tree.insert(20);
        tree.clear();
        assertTrue(tree.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertTrue(tree.isEmpty());
        tree.insert(10);
        assertFalse(tree.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, tree.size());
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        assertEquals(3, tree.size());
    }

    @Test
    void testGetNodeAndGet() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);

        AVLTreeNode<Integer> node = tree.getNode(20);
        assertNotNull(node);
        assertEquals(20, node.getValue());

        Integer value = tree.get(20);
        assertNotNull(value);
        assertEquals(20, value);
    }
}
