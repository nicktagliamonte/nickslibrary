package com.nickslibrary.datastructures.advanced;

/**
 * A Segment Tree is a data structure that allows for efficient range queries
 * and updates.
 */
public class SegmentTree {

    private int[] tree;
    private int[] array;
    private int n;

    /**
     * Constructs a Segment Tree from the given array.
     *
     * @param array The array to construct the Segment Tree from.
     */
    public SegmentTree(int[] array) {
        this.array = array;
        this.n = array.length;
        this.tree = new int[4 * n];
        build(0, 0, n - 1);
    }

    /**
     * Builds the Segment Tree.
     *
     * @param node  The current node in the Segment Tree.
     * @param start The starting index of the segment.
     * @param end   The ending index of the segment.
     */
    private void build(int node, int start, int end) {
        if (start == end) {
            tree[node] = array[start];
        } else {
            int mid = (start + end) / 2;
            build(2 * node + 1, start, mid);
            build(2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }

    /**
     * Updates the Segment Tree with the given value at the specified index.
     *
     * @param idx   The index to update.
     * @param value The value to update.
     */
    public void update(int idx, int value) {
        int diff = value - array[idx];
        array[idx] = value;
        update(0, 0, n - 1, idx, diff);
    }

    /**
     * Updates the Segment Tree with the given value at the specified index.
     *
     * @param node  The current node in the Segment Tree.
     * @param start The starting index of the segment.
     * @param end   The ending index of the segment.
     * @param idx   The index to update.
     * @param diff  The difference to update.
     */
    private void update(int node, int start, int end, int idx, int diff) {
        if (start == end) {
            tree[node] += diff;
        } else {
            int mid = (start + end) / 2;
            if (idx <= mid) {
                update(2 * node + 1, start, mid, idx, diff);
            } else {
                update(2 * node + 2, mid + 1, end, idx, diff);
            }
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }

    /**
     * Queries the Segment Tree for the sum in the given range.
     *
     * @param l The starting index of the range.
     * @param r The ending index of the range.
     * @return The sum in the given range.
     */
    public int query(int l, int r) {
        return query(0, 0, n - 1, l, r);
    }

    /**
     * Queries the Segment Tree for the sum in the given range.
     *
     * @param node  The current node in the Segment Tree.
     * @param start The starting index of the segment.
     * @param end   The ending index of the segment.
     * @param l     The starting index of the range.
     * @param r     The ending index of the range.
     * @return The sum in the given range.
     */
    private int query(int node, int start, int end, int l, int r) {
        if (r < start || l > end) {
            // Range represented by a node is completely outside the given range
            return 0;
        }
        if (l <= start && end <= r) {
            // Range represented by a node is completely inside the given range
            return tree[node];
        }
        // Range represented by a node is partially inside and partially outside the
        // given range
        int mid = (start + end) / 2;
        int leftSum = query(2 * node + 1, start, mid, l, r);
        int rightSum = query(2 * node + 2, mid + 1, end, l, r);
        return leftSum + rightSum;
    }
}