package com.nickslibrary.advanced;

import com.nickslibrary.datastructures.advanced.SkipList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkipListTest {

    private SkipList<Integer> skipList;

    @BeforeEach
    void setUp() {
        skipList = new SkipList<>();
    }

    @Test
    void testInsertAndSearch() {
        skipList.insert(1);
        skipList.insert(2);
        skipList.insert(3);

        assertTrue(skipList.search(1));
        assertTrue(skipList.search(2));
        assertTrue(skipList.search(3));
        assertFalse(skipList.search(4));
    }

    @Test
    void testDelete() {
        skipList.insert(1);
        skipList.insert(2);
        skipList.insert(3);

        skipList.delete(2);
        assertFalse(skipList.search(2));
        assertTrue(skipList.search(1));
        assertTrue(skipList.search(3));
    }

    @Test
    void testSize() {
        assertEquals(0, skipList.size());
        skipList.insert(1);
        skipList.insert(2);
        assertEquals(2, skipList.size());
        skipList.delete(1);
        assertEquals(1, skipList.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(skipList.isEmpty());
        skipList.insert(1);
        assertFalse(skipList.isEmpty());
        skipList.delete(1);
        assertTrue(skipList.isEmpty());
    }

    @Test
    void testClear() {
        skipList.insert(1);
        skipList.insert(2);
        skipList.insert(3);
        skipList.clear();
        assertTrue(skipList.isEmpty());
        assertEquals(0, skipList.size());
    }
}