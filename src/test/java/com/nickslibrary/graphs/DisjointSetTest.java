package com.nickslibrary.graphs;

import com.nickslibrary.datastructures.graphs.DisjointSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DisjointSetTest {

    private DisjointSet disjointSet;

    @BeforeEach
    public void setUp() {
        disjointSet = new DisjointSet(5);
    }

    @Test
    public void testFind() {
        for (int i = 0; i < 5; i++) {
            assertEquals(i, disjointSet.find(i));
        }
    }

    @Test
    public void testUnion() {
        disjointSet.union(0, 1);
        assertEquals(disjointSet.find(0), disjointSet.find(1));

        disjointSet.union(1, 2);
        assertEquals(disjointSet.find(0), disjointSet.find(2));

        disjointSet.union(3, 4);
        assertEquals(disjointSet.find(3), disjointSet.find(4));

        assertNotEquals(disjointSet.find(0), disjointSet.find(3));
    }

    @Test
    public void testConnected() {
        assertFalse(disjointSet.connected(0, 1));
        disjointSet.union(0, 1);
        assertTrue(disjointSet.connected(0, 1));

        assertFalse(disjointSet.connected(1, 2));
        disjointSet.union(1, 2);
        assertTrue(disjointSet.connected(1, 2));

        assertFalse(disjointSet.connected(3, 4));
        disjointSet.union(3, 4);
        assertTrue(disjointSet.connected(3, 4));

        assertFalse(disjointSet.connected(0, 3));
    }

    @Test
    public void testGetCount() {
        assertEquals(5, disjointSet.getCount());
        disjointSet.union(0, 1);
        assertEquals(4, disjointSet.getCount());
        disjointSet.union(1, 2);
        assertEquals(3, disjointSet.getCount());
        disjointSet.union(3, 4);
        assertEquals(2, disjointSet.getCount());
        disjointSet.union(2, 3);
        assertEquals(1, disjointSet.getCount());
    }

    @Test
    public void testReset() {
        disjointSet.union(0, 1);
        disjointSet.union(1, 2);
        disjointSet.union(3, 4);
        disjointSet.reset();
        assertEquals(5, disjointSet.getCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(i, disjointSet.find(i));
        }
    }
}