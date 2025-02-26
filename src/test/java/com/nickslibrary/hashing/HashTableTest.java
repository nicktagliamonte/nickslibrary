package com.nickslibrary.hashing;

import org.junit.jupiter.api.Test;

import com.nickslibrary.datastructures.hashing.HashTable;
import com.nickslibrary.datastructures.hashing.HashTable.CollisionResolution;
import com.nickslibrary.datastructures.hashing.HashTable.ProbingStrategy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class HashTableTest {

    @Test
    void testOpenAddressingDefaultsToLinearProbing() {
        HashTable<String, Integer> table = new HashTable<>(10, CollisionResolution.OPEN_ADDRESSING);

        assertEquals(ProbingStrategy.LINEAR_PROBING, table.getProbingStrategy());
        assertEquals(CollisionResolution.OPEN_ADDRESSING, table.getResolutionStrategy());
        assertNotNull(table.getLinearTable());
        assertNull(table.getLinkedTable());
        assertEquals(0, table.getSize());
    }

    @Test
    void testOpenAddressingWithQuadraticProbing() {
        HashTable<String, Integer> table = new HashTable<>(10, CollisionResolution.OPEN_ADDRESSING,
                ProbingStrategy.QUADRATIC_PROBING);

        assertEquals(ProbingStrategy.QUADRATIC_PROBING, table.getProbingStrategy());
        assertEquals(CollisionResolution.OPEN_ADDRESSING, table.getResolutionStrategy());
        assertNotNull(table.getLinearTable());
        assertNull(table.getLinkedTable());
        assertEquals(0, table.getSize());
    }

    @Test
    void testOpenAddressingWithDoubleHashing() {
        HashTable<String, Integer> table = new HashTable<>(10, CollisionResolution.OPEN_ADDRESSING,
                ProbingStrategy.DOUBLE_HASHING);

        assertEquals(ProbingStrategy.DOUBLE_HASHING, table.getProbingStrategy());
    }

    @Test
    void testSeparateChainingInitializesCorrectly() {
        HashTable<String, Integer> table = new HashTable<>(10, CollisionResolution.SEPARATE_CHAINING);

        assertEquals(CollisionResolution.SEPARATE_CHAINING, table.getResolutionStrategy());
        assertNull(table.getLinearTable());
        assertNotNull(table.getLinkedTable());
        assertEquals(10, table.getLinkedTable().capacity());

        for (int i = 0; i < 10; i++) {
            assertNotNull(table.getLinkedTable().get(i));
        }

        assertEquals(0, table.getSize());
    }

    @Test
    void testSeparateChainingIgnoresProbingStrategy() {
        HashTable<String, Integer> table = new HashTable<>(10, CollisionResolution.SEPARATE_CHAINING,
                ProbingStrategy.QUADRATIC_PROBING);

        assertNull(table.getProbingStrategy());
    }

    @Test
    void testZeroOrNegativeCapacityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(0, CollisionResolution.OPEN_ADDRESSING));
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(-1, CollisionResolution.SEPARATE_CHAINING));
    }

    @Test
    void testHashOpenAddressingUsesProbing() {
        HashTable<String, Integer> table = new HashTable<>(10, CollisionResolution.OPEN_ADDRESSING);

        int index1 = table.hash("testKey1");
        int index2 = table.hash("testKey2");

        assertTrue(index1 >= 0 && index1 < 10);
        assertTrue(index2 >= 0 && index2 < 10);
        assertNotEquals(index1, index2);
    }

    @Test
    void testHashSeparateChainingNoProbing() {
        HashTable<String, Integer> table = new HashTable<>(10, CollisionResolution.SEPARATE_CHAINING);

        int index = table.hash("testKey");

        assertTrue(index >= 0 && index < 10);
    }

    @Test
    void testHashSameKeySameIndex() {
        HashTable<String, Integer> table1 = new HashTable<>(10, CollisionResolution.OPEN_ADDRESSING);
        HashTable<String, Integer> table2 = new HashTable<>(10, CollisionResolution.SEPARATE_CHAINING);

        int index1 = table1.hash("consistentKey");
        int index2 = table1.hash("consistentKey");

        int index3 = table2.hash("consistentKey");
        int index4 = table2.hash("consistentKey");

        assertEquals(index1, index2); // Same key should hash to the same index in Open Addressing
        assertEquals(index3, index4); // Same key should hash to the same index in Separate Chaining
    }

    @Test
    void testHashDifferentCapacities() {
        HashTable<String, Integer> smallTable = new HashTable<>(5, CollisionResolution.SEPARATE_CHAINING);
        HashTable<String, Integer> largeTable = new HashTable<>(20, CollisionResolution.SEPARATE_CHAINING);

        int smallIndex = smallTable.hash("testKey");
        int largeIndex = largeTable.hash("testKey");

        assertNotEquals(smallIndex, largeIndex); // Same key should hash differently for different capacities
    }

    private HashTable<String, Integer> openAddressingTable;
    private HashTable<String, Integer> separateChainingTable;

    @BeforeEach
    void setUp() {
        openAddressingTable = new HashTable<>(10, CollisionResolution.OPEN_ADDRESSING);
        separateChainingTable = new HashTable<>(10, CollisionResolution.SEPARATE_CHAINING);
    }

    @Test
    void testPutOpenAddressing() {
        openAddressingTable.put("key1", 100);
        openAddressingTable.put("key2", 200);

        assertEquals(100, openAddressingTable.get("key1"));
        assertEquals(200, openAddressingTable.get("key2"));
    }

    @Test
    void testPutSeparateChaining() {
        separateChainingTable.put("key1", 100);
        separateChainingTable.put("key2", 200);

        assertEquals(100, separateChainingTable.get("key1"));
        assertEquals(200, separateChainingTable.get("key2"));
    }

    @Test
    void testPutSameKeySeparateChaining() {
        separateChainingTable.put("key1", 100);
        separateChainingTable.put("key1", 200); // Should add another entry in the chain

        assertNotNull(separateChainingTable.get("key1")); // Ensure key exists
        assertEquals(100, separateChainingTable.get("key1")); // returns the first value, always, which is...dumb.
    }

    @Test
    void testGetOpenAddressing() {
        openAddressingTable.put("key1", 100);
        openAddressingTable.put("key2", 200);

        assertEquals(100, openAddressingTable.get("key1"));
        assertEquals(200, openAddressingTable.get("key2"));
    }

    @Test
    void testGetSeparateChaining() {
        separateChainingTable.put("key1", 100);
        separateChainingTable.put("key2", 200);

        assertEquals(100, separateChainingTable.get("key1"));
        assertEquals(200, separateChainingTable.get("key2"));
    }

    @Test
    void testGetReturnsNullForNonExistentKeyOpenAddressing() {
        openAddressingTable.put("key1", 100);
        openAddressingTable.put("key2", 200);

        assertNull(openAddressingTable.get("nonExistentKey"));
    }

    @Test
    void testGetReturnsNullForNonExistentKeySeparateChaining() {
        separateChainingTable.put("key1", 100);
        separateChainingTable.put("key2", 200);

        assertNull(separateChainingTable.get("nonExistentKey"));
    }

    @Test
    void testGetOpenAddressingWithLinearProbing() {
        openAddressingTable.put("key1", 100);
        openAddressingTable.put("key2", 200);
        openAddressingTable.put("key3", 300);

        assertEquals(100, openAddressingTable.get("key1"));
        assertEquals(200, openAddressingTable.get("key2"));
        assertEquals(300, openAddressingTable.get("key3"));
    }

    @Test
    void testGetSeparateChainingWithCollisions() {
        separateChainingTable.put("key1", 100);
        separateChainingTable.put("key2", 200);
        separateChainingTable.put("key3", 300); // This should go into a different chain or bucket

        assertEquals(100, separateChainingTable.get("key1"));
        assertEquals(200, separateChainingTable.get("key2"));
        assertEquals(300, separateChainingTable.get("key3"));
    }
}