package com.nickslibrary.datastructures.advanced;

import java.util.Random;

public class CountMinSketch {

    private int[][] table;
    private int[] hashA;
    private int depth;
    private int width;
    private Random random;

    /**
     * Constructs a Count-Min Sketch with the given depth and width.
     *
     * @param depth The number of hash functions.
     * @param width The width of the hash table.
     */
    public CountMinSketch(int depth, int width) {
        this.depth = depth;
        this.width = width;
        this.table = new int[depth][width];
        this.hashA = new int[depth];
        this.random = new Random();

        // Initialize hash functions
        for (int i = 0; i < depth; i++) {
            hashA[i] = random.nextInt();
        }
    }

    /**
     * Adds an element to the Count-Min Sketch.
     *
     * @param item  The item to add.
     * @param count The count to add.
     */
    public void add(String item, int count) {
        int[] hashes = hash(item);
        for (int i = 0; i < depth; i++) {
            table[i][hashes[i]] += count;
        }
    }

    /**
     * Estimates the count of an element in the Count-Min Sketch.
     *
     * @param item The item to estimate the count for.
     * @return The estimated count of the item.
     */
    public int estimateCount(String item) {
        int[] hashes = hash(item);
        int minCount = Integer.MAX_VALUE;
        for (int i = 0; i < depth; i++) {
            minCount = Math.min(minCount, table[i][hashes[i]]);
        }
        return minCount;
    }

    /**
     * Merges another Count-Min Sketch into this one.
     *
     * @param other The other Count-Min Sketch to merge.
     */
    public void merge(CountMinSketch other) {
        if (this.depth != other.depth || this.width != other.width) {
            throw new IllegalArgumentException("Dimensions of Count-Min Sketches must match for merging.");
        }

        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                this.table[i][j] += other.table[i][j];
            }
        }
    }

    /**
     * Resets the Count-Min Sketch.
     */
    public void reset() {
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                table[i][j] = 0;
            }
        }
    }

    /**
     * Generates hash values for the given item.
     *
     * @param item The item to hash.
     * @return An array of hash values.
     */
    private int[] hash(String item) {
        int[] hashes = new int[depth];
        for (int i = 0; i < depth; i++) {
            hashes[i] = (hashA[i] ^ item.hashCode()) % width;
            if (hashes[i] < 0) {
                hashes[i] += width;
            }
        }
        return hashes;
    }

    /**
     * Returns the depth of the Count-Min Sketch.
     *
     * @return The depth of the Count-Min Sketch.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Returns the width of the Count-Min Sketch.
     *
     * @return The width of the Count-Min Sketch.
     */
    public int getWidth() {
        return width;
    }
}