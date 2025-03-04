package com.nickslibrary.datastructures.hashing;

import com.nickslibrary.datastructures.linear.DynamicArray;
import com.nickslibrary.datastructures.linear.LinkedList;
import com.nickslibrary.utils.hash.HashFunctions;

/**
 * A hash table implementation that supports both open addressing and separate
 * chaining collision resolution methods.
 *
 * @param <K> the type of keys in this hash table
 * @param <V> the type of values in this hash table
 */
public class HashTable<K, V> {

    /**
     * Enum representing the two collision resolution strategies: Open Addressing
     * and Separate Chaining.
     */
    public enum CollisionResolution {
        OPEN_ADDRESSING, SEPARATE_CHAINING
    }

    /**
     * Enum representing the probing strategies available for Open Addressing
     * collision resolution.
     */
    public enum ProbingStrategy {
        LINEAR_PROBING, QUADRATIC_PROBING, DOUBLE_HASHING
    }

    private CollisionResolution resolutionStrategy;
    private ProbingStrategy probingStrategy;
    private DynamicArray<Entry<K, V>> linearTable;
    private DynamicArray<LinkedList<Entry<K, V>>> linkedTable;
    private int size;

    /**
     * Constructs a new hash table with the specified collision resolution strategy.
     * Defaults to Linear Probing if an alternative is not specified
     *
     * @param capacity           the initial capacity of the table
     * @param resolutionStrategy the collision resolution strategy (OPEN_ADDRESSING
     *                           or SEPARATE_CHAINING)
     */
    public HashTable(int capacity, CollisionResolution resolutionStrategy) {
        this(capacity, resolutionStrategy, ProbingStrategy.LINEAR_PROBING);
    }

    /**
     * Constructs a new hash table with the specified collision resolution strategy
     * and probing strategy.
     *
     * @param capacity           the initial capacity of the table
     * @param resolutionStrategy the collision resolution strategy (OPEN_ADDRESSING
     *                           or SEPARATE_CHAINING)
     * @param probingStrategy    the probing strategy (only applicable for
     *                           OPEN_ADDRESSING)
     */
    public HashTable(int capacity, CollisionResolution resolutionStrategy, ProbingStrategy probingStrategy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.resolutionStrategy = resolutionStrategy;
        this.probingStrategy = (resolutionStrategy == CollisionResolution.OPEN_ADDRESSING) ? probingStrategy : null;
        this.size = 0;

        if (resolutionStrategy == CollisionResolution.SEPARATE_CHAINING) {
            this.linkedTable = new DynamicArray<>(capacity);
            this.linearTable = null;
            for (int i = 0; i < capacity; i++) {
                linkedTable.add(i, new LinkedList<Entry<K, V>>(false, true));
            }
        } else {
            this.linearTable = new DynamicArray<>(capacity);
            for (int i = 0; i < capacity; i++) {
                linearTable.add(i, null);
            }
            this.linkedTable = null;
        }
    }

    /**
     * Hashes the given key and returns the index where it should be stored.
     *
     * @param key the key to hash
     * @return the index in the table where the key should be placed
     */
    public int hash(K key) {
        if (resolutionStrategy == CollisionResolution.OPEN_ADDRESSING) {
            int baseIndex = HashFunctions.hash(key, linearTable.capacity());
            return HashFunctions.applyProbing(linearTable, baseIndex, key, probingStrategy);
        } else {
            return HashFunctions.hash(key, linkedTable.capacity());
        }
    }

    /**
     * Inserts a key-value pair into the hash table.
     *
     * @param key   the key to insert
     * @param value the value to associate with the key
     */
    public void put(K key, V value) {
        int index = hash(key);

        if (resolutionStrategy == CollisionResolution.OPEN_ADDRESSING) {
            if (index == -1) {
                linearTable.add(HashFunctions.hash(key, linearTable.capacity()), new Entry<>(key, value));
            } else {
                linearTable.remove(index);
                linearTable.add(index, new Entry<>(key, value));
            }
        } else {
            LinkedList<Entry<K, V>> chain = linkedTable.get(index);
            if (chain == null) {
                chain = new LinkedList<Entry<K, V>>(false, true);
                linkedTable.add(index, chain);
            }
            chain.add(new Entry<>(key, value));
        }

        size++;
    }

    /**
     * Retrieves the value associated with the given key.
     *
     * @param key the key to look up
     * @return the value associated with the key, or null if not found
     */
    public V get(K key) {
        if (resolutionStrategy == CollisionResolution.SEPARATE_CHAINING) {
            int linkedIndex = hash(key);
            LinkedList<Entry<K, V>> chain = linkedTable.get(linkedIndex);

            if (chain != null) {
                for (Entry<K, V> entry : chain) {
                    if (entry.key.equals(key)) {
                        return entry.value;
                    }
                }
            }
        } else {
            int linearIndex = HashFunctions.hash(key, linearTable.capacity());
            int probeCount = 0;
            int capacity = linearTable.capacity();

            while (probeCount < capacity) {
                if (linearTable.get(linearIndex) == null) {
                    return null;
                }

                Entry<K, V> entry = linearTable.get(linearIndex);
                if (entry.key.equals(key)) {
                    return entry.value;
                }

                linearIndex = HashFunctions.getNextIndex(linearTable, linearIndex, probingStrategy, probeCount, key);
                probeCount++;
            }
        }

        return null;
    }

    /**
     * Removes the entry with the specified key from the hash table.
     * No action is taken if the key is not found.
     *
     * @param key the key to remove
     */
    public void remove(K key) {
        int index = hash(key);

        if (resolutionStrategy == CollisionResolution.OPEN_ADDRESSING) {
            if (linearTable.get(index) == null) {
                return;
            }

            Entry<K, V> entry = linearTable.get(index);
            if (entry.key.equals(key)) {
                linearTable.remove(index);
                size--;
            }
        } else {
            LinkedList<Entry<K, V>> chain = linkedTable.get(index);
            if (chain != null) {
                for (int i = 0; i < chain.size(); i++) {
                    if (chain.get(i).key.equals(key)) {
                        chain.removeAt(i);
                        size--;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Removes all entries from the hash table, resetting it to an empty state.
     */
    public void clear() {
        if (resolutionStrategy == CollisionResolution.OPEN_ADDRESSING) {
            linearTable.clear();
        } else {
            linkedTable.clear();
        }

        size = 0;
    }

    public CollisionResolution getResolutionStrategy() {
        return resolutionStrategy;
    }

    public ProbingStrategy getProbingStrategy() {
        return probingStrategy;
    }

    public DynamicArray<Entry<K, V>> getLinearTable() {
        return linearTable;
    }

    public DynamicArray<LinkedList<Entry<K, V>>> getLinkedTable() {
        return linkedTable;
    }

    public int getSize() {
        return size;
    }

    /**
     * Entry class that holds key-value pairs.
     */
    public static class Entry<K, V> {
        public K key;
        public V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}