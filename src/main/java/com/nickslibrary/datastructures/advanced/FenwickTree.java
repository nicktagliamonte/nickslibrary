package com.nickslibrary.datastructures.advanced;

/**
 * A Fenwick Tree is a data structure that provides efficient methods for
 * cumulative frequency tables. It allows for efficient updates and prefix sum
 * queries.
 */
public class FenwickTree {

    private int[] tree;
    private int size;

    /**
     * Constructs a Fenwick Tree with the specified size.
     *
     * @param size The size of the Fenwick Tree.
     */
    public FenwickTree(int size) {
        this.size = size;
        this.tree = new int[size + 1];
    }

    /**
     * Updates the Fenwick Tree with the given value at the specified index.
     *
     * @param index The index to update.
     * @param value The value to add.
     */
    public void update(int index, int value) {
        index++;
        while (index <= size) {
            tree[index] += value;
            index += index & -index;
        }
    }

    /**
     * Returns the prefix sum from the start to the specified index.
     *
     * @param index The index up to which to compute the prefix sum.
     * @return The prefix sum from the start to the specified index.
     */
    public int query(int index) {
        int sum = 0;
        index++;
        while (index > 0) {
            sum += tree[index];
            index -= index & -index;
        }
        return sum;
    }

    /**
     * Returns the range sum between two indices.
     *
     * @param left  The starting index of the range.
     * @param right The ending index of the range.
     * @return The range sum between the two indices.
     */
    public int rangeQuery(int left, int right) {
        return query(right) - query(left - 1);
    }

    /**
     * Resets the Fenwick Tree.
     */
    public void reset() {
        for (int i = 0; i <= size; i++) {
            tree[i] = 0;
        }
    }

    /**
     * Gets the value at the specified index.
     *
     * @param index The index to get the value from.
     * @return The value at the specified index.
     */
    public int get(int index) {
        return query(index) - query(index - 1);
    }

    /**
     * Sets the value at the specified index.
     *
     * @param index The index to set the value at.
     * @param value The value to set.
     */
    public void set(int index, int value) {
        int currentValue = get(index);
        int delta = value - currentValue;
        update(index, delta);
    }

    /**
     * Constructs the Fenwick Tree from an existing array.
     *
     * @param array The array to construct the Fenwick Tree from.
     */
    public void constructFromArray(int[] array) {
        reset();
        for (int i = 0; i < array.length; i++) {
            update(i, array[i]);
        }
    }
}