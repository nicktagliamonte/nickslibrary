package com.nickslibrary;

import com.nickslibrary.datastructures.hashing.HashTable;
import com.nickslibrary.datastructures.hashing.HashTable.CollisionResolution;

/**
 *
 */
public class Main {
    public static void main(String[] args) {
        HashTable<String, Integer> smallTable = new HashTable<>(2, CollisionResolution.OPEN_ADDRESSING);
        smallTable.put("key1", 100);
        smallTable.put("key2", 200);
        smallTable.put("key3", 300); // Should trigger rehash
    }
}
