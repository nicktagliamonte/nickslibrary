package com.nickslibrary.datastructures.advanced;

import java.util.BitSet;
import java.util.function.Function;

/**
 * A BloomFilter is a probabilistic data structure that allows for fast
 * membership testing with a possibility of false positives.
 *
 * @param <T> The type of elements in the BloomFilter.
 */
public class BloomFilterCustom<T> {

    private final BitSet bitSet;
    private final int bitSetSize;
    private final int numHashFunctions;
    private final Function<T, Integer>[] hashFunctions;
    private int elementCount;

    /**
     * Constructs a BloomFilter with the specified size and hash functions.
     *
     * @param bitSetSize       The size of the bit set.
     * @param numHashFunctions The number of hash functions.
     * @param hashFunctions    The hash functions to use.
     */
    public BloomFilterCustom(int bitSetSize, int numHashFunctions, Function<T, Integer>[] hashFunctions) {
        this.bitSetSize = bitSetSize;
        this.numHashFunctions = numHashFunctions;
        this.hashFunctions = hashFunctions;
        this.bitSet = new BitSet(bitSetSize);
        this.elementCount = 0;
    }

    /**
     * Adds an element to the BloomFilter.
     *
     * @param element The element to add.
     */
    public void add(T element) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = hashFunction.apply(element);
            int index = Math.abs(hash % bitSetSize);
            bitSet.set(index);
        }
        elementCount++;
    }

    /**
     * Checks if an element is possibly in the BloomFilter.
     *
     * @param element The element to check.
     * @return True if the element is possibly in the BloomFilter, false otherwise.
     */
    public boolean mightContain(T element) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = hashFunction.apply(element);
            int index = Math.abs(hash % bitSetSize);
            if (!bitSet.get(index)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears the BloomFilter.
     */
    public void clear() {
        bitSet.clear();
        elementCount = 0;
    }

    /**
     * Returns the size of the bit set.
     *
     * @return The size of the bit set.
     */
    public int getBitSetSize() {
        return bitSetSize;
    }

    /**
     * Returns the number of hash functions.
     *
     * @return The number of hash functions.
     */
    public int getNumHashFunctions() {
        return numHashFunctions;
    }

    /**
     * Returns the number of elements added to the BloomFilter.
     *
     * @return The number of elements added to the BloomFilter.
     */
    public int getElementCount() {
        return elementCount;
    }

    /**
     * Returns the estimated false positive probability.
     *
     * @return The estimated false positive probability.
     */
    public double getFalsePositiveProbability() {
        double p = Math.pow(1 - Math.exp(-numHashFunctions * (double) elementCount / bitSetSize), numHashFunctions);
        return p;
    }

    /**
     * Returns the expected number of elements the BloomFilter can handle.
     *
     * @return The expected number of elements the BloomFilter can handle.
     */
    public int getExpectedNumberOfElements() {
        return (int) (-bitSetSize * Math.log(2) / numHashFunctions);
    }

    /**
     * Calculates the optimal number of hash functions based on the size of the bit
     * set and the expected number of elements.
     *
     * @param bitSetSize               The size of the bit set.
     * @param expectedNumberOfElements The expected number of elements.
     * @return The optimal number of hash functions.
     */
    public static int optimalNumOfHashFunctions(int bitSetSize, int expectedNumberOfElements) {
        return (int) Math.round((double) bitSetSize / expectedNumberOfElements * Math.log(2));
    }
}