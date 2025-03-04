package com.nickslibrary.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.trees.Treap;

import static org.junit.jupiter.api.Assertions.*;

class TreapTest {

    private Treap treap;

    @BeforeEach
    void setUp() {
        treap = new Treap();
    }

    @Test
    void testInsertAndSearch() {
        treap.insert(50);
        treap.insert(30);
        treap.insert(20);
        treap.insert(40);
        treap.insert(70);
        treap.insert(60);
        treap.insert(80);

        assertTrue(treap.search(50));
        assertTrue(treap.search(30));
        assertTrue(treap.search(20));
        assertTrue(treap.search(40));
        assertTrue(treap.search(70));
        assertTrue(treap.search(60));
        assertTrue(treap.search(80));
        assertFalse(treap.search(90));
    }

    @Test
    void testDelete() {
        treap.insert(50);
        treap.insert(30);
        treap.insert(20);
        treap.insert(40);
        treap.insert(70);
        treap.insert(60);
        treap.insert(80);

        treap.delete(20);
        assertFalse(treap.search(20));

        treap.delete(30);
        assertFalse(treap.search(30));

        treap.delete(50);
        assertFalse(treap.search(50));
    }

    @Test
    void testInorderTraversal() {
        treap.insert(50);
        treap.insert(30);
        treap.insert(20);
        treap.insert(40);
        treap.insert(70);
        treap.insert(60);
        treap.insert(80);

        // Capture the output of the inorder traversal
        StringBuilder output = new StringBuilder();
        treap.inorder(node -> output.append(node.key).append(" "));

        assertEquals("20 30 40 50 60 70 80 ", output.toString());
    }
}