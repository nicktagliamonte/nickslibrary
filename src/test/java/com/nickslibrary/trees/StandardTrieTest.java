package com.nickslibrary.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.trees.StandardTrie;

import static org.junit.jupiter.api.Assertions.*;

public class StandardTrieTest {
    private StandardTrie trie;

    @BeforeEach
    void setUp() {
        trie = new StandardTrie();
    }

    @Test
    void testInsertAndSearch() {
        trie.insert("apple");
        assertTrue(trie.search("apple"), "Inserted word should be found.");
        assertFalse(trie.search("app"), "Substring should not be found as a word unless inserted.");

        trie.insert("app");
        assertTrue(trie.search("app"), "Inserted substring should be found as a word.");
    }

    @Test
    void testStartsWith() {
        trie.insert("apple");
        assertTrue(trie.startsWith("app"), "Prefix should exist in Trie.");
        assertFalse(trie.startsWith("b"), "Non-existent prefix should return false.");
    }

    @Test
    void testDelFunction() {
        trie.insert("apple");
        System.out.println("Before delete: " + trie.search("apple")); // Should print true
        boolean deleted = trie.delete("apple");
        System.out.println("Delete result: " + deleted); // Should print true
        System.out.println("After delete: " + trie.search("apple")); // Should print false
        assertTrue(trie.isEmpty(), "the trie should be empty after deleting its only member.");
    }

    @Test
    void testGetFrequency() {
        trie.insert("banana");
        assertEquals(1, trie.getFrequency("banana"), "Inserted word should have frequency 1.");

        trie.insert("banana");
        assertEquals(2, trie.getFrequency("banana"), "Word frequency should increment.");

        assertEquals(0, trie.getFrequency("apple"), "Non-existent word should return frequency 0.");
    }

    @Test
    void testClearAndIsEmpty() {
        assertTrue(trie.isEmpty(), "Newly created Trie should be empty.");

        trie.insert("apple");
        assertFalse(trie.isEmpty(), "Trie should not be empty after insertions.");

        trie.clear();
        assertTrue(trie.isEmpty(), "Cleared Trie should be empty.");
    }

    @Test
    void testSize() {
        assertEquals(0, trie.size(), "New Trie should have size 0.");

        trie.insert("apple");
        assertEquals(1, trie.size(), "Trie should have size 1 after inserting a word.");

        trie.insert("banana");
        trie.insert("grape");
        assertEquals(3, trie.size(), "Trie should correctly count multiple words.");

        trie.insert("apple"); // Duplicate insertion
        assertEquals(3, trie.size(), "Trie size should not increase when inserting duplicate word.");

        trie.delete("banana");
        assertEquals(2, trie.size(), "Size should decrease after deleting a word.");
    }
}
