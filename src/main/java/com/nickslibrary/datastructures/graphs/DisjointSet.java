package com.nickslibrary.datastructures.graphs;

public class DisjointSet {
    private int[] parent;
    private int[] rank;
    private int count; // Number of disjoint sets

    // Constructor to create and initialize sets of n items
    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    // Find the representative of the set containing x
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }

    // Union of two sets containing x and y
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX != rootY) {
            // Union by rank
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            count--; // Decrease the number of disjoint sets
        }
    }

    // Check if two elements are in the same set
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    // Get the number of disjoint sets
    public int getCount() {
        return count;
    }

    // Reset the disjoint set
    public void reset() {
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
        count = parent.length;
    }
}