package com.nickslibrary.advanced;

import com.nickslibrary.datastructures.advanced.SuffixTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SuffixTreeTest {

    private SuffixTree suffixTree;

    @BeforeEach
    void setUp() {
        suffixTree = new SuffixTree("banana");
    }

    @Test
    void testSearch() {
        assertTrue(suffixTree.search("ban"));
        assertTrue(suffixTree.search("ana"));
        assertFalse(suffixTree.search("apple"));
    }
}