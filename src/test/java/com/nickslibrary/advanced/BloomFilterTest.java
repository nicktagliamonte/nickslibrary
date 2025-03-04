package com.nickslibrary.advanced;

import com.nickslibrary.datastructures.advanced.BloomFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class BloomFilterTest {

    private BloomFilter<String> bloomFilter;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        Function<String, Integer>[] hashFunctions = new Function[] {
                s -> s.hashCode(),
                s -> s.hashCode() * 31
        };
        bloomFilter = new BloomFilter<>(1000, 2, hashFunctions);
    }

    @Test
    void testAddAndMightContain() {
        bloomFilter.add("test");
        assertTrue(bloomFilter.mightContain("test"));
        assertFalse(bloomFilter.mightContain("not_in_filter"));
    }

    @Test
    void testClear() {
        bloomFilter.add("test");
        bloomFilter.clear();
        assertFalse(bloomFilter.mightContain("test"));
    }

    @Test
    void testGetBitSetSize() {
        assertEquals(1000, bloomFilter.getBitSetSize());
    }

    @Test
    void testGetNumHashFunctions() {
        assertEquals(2, bloomFilter.getNumHashFunctions());
    }

    @Test
    void testGetElementCount() {
        bloomFilter.add("test");
        assertEquals(1, bloomFilter.getElementCount());
    }
}