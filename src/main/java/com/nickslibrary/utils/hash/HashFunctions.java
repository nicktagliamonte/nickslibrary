package com.nickslibrary.utils.hash;

import com.nickslibrary.datastructures.hashing.HashTable.Entry;
import com.nickslibrary.datastructures.hashing.HashTable.ProbingStrategy;
import com.nickslibrary.datastructures.linear.DynamicArray;

public class HashFunctions {

    public static <K> int hash(K key, int capacity) {
        int hashCode = key.hashCode();
        return (hashCode & 0x7FFFFFFF) % capacity;
    }

    public static <K, V> int applyProbing(DynamicArray<Entry<K, V>> table, int baseIndex, K key,
            ProbingStrategy probingStrategy) {

        if (table.get(baseIndex) == null) {
            return baseIndex;
        }

        int i = 1; // Probe counter

        if (probingStrategy == ProbingStrategy.LINEAR_PROBING) {
            int index = baseIndex;
            do {
                index = (index + 1) % table.capacity();
                if (table.get(index) == null) {
                    return index;
                }
            } while (index != baseIndex);
        }

        if (probingStrategy == ProbingStrategy.QUADRATIC_PROBING) {
            int index = baseIndex;
            do {
                index = (baseIndex + (i * i)) % table.capacity();
                if (table.get(index) == null) {
                    return index;
                }
                i++;
            } while (i < table.capacity());
        }

        if (probingStrategy == ProbingStrategy.DOUBLE_HASHING) {
            int stepSize = secondaryHash(key, table.capacity());
            int index = baseIndex;
            do {
                index = (index + i * stepSize) % table.capacity();
                if (table.get(index) == null) {
                    return index;
                }
                i++;
            } while (i < table.capacity());
        }

        return -1;
    }

	public static <K, V> int getNextIndex(DynamicArray<Entry<K, V>> table, int index, ProbingStrategy probingStrategy, int probeCount, K key) {
		if (probingStrategy == ProbingStrategy.LINEAR_PROBING) {
            return (index + 1) % table.capacity();
        }

        if (probingStrategy == ProbingStrategy.QUADRATIC_PROBING) {
            return (index + (probeCount * probeCount)) % table.capacity();
        }
        
        if (probingStrategy == ProbingStrategy.DOUBLE_HASHING) {
            int stepSize = secondaryHash(key, table.capacity());
            return (index + probeCount * stepSize) % table.capacity();
        }

        return -1;
	}

    public static <K> int secondaryHash(K key, int capacity) {
        int hashCode = key.hashCode();
        return 1 + (Math.abs(hashCode) % (capacity - 1)); // Ensures step size is never 0
    }    
}
