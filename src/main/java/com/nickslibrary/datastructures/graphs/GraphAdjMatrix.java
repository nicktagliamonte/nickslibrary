package com.nickslibrary.datastructures.graphs;

import java.util.ArrayList;
import java.util.List;

public class GraphAdjMatrix {
    private int[][] adjMatrix;
    private int numVertices;

    // Constructor
    public GraphAdjMatrix(int numVertices) {
        this.numVertices = numVertices;
        adjMatrix = new int[numVertices][numVertices];
    }

    // Add edge
    public void addEdge(int src, int dest) {
        adjMatrix[src][dest] = 1;
        adjMatrix[dest][src] = 1; // For undirected graph
    }

    // Remove edge
    public void removeEdge(int src, int dest) {
        adjMatrix[src][dest] = 0;
        adjMatrix[dest][src] = 0; // For undirected graph
    }

    // Check if there is an edge between two vertices
    public boolean hasEdge(int src, int dest) {
        return adjMatrix[src][dest] == 1;
    }

    // Get all neighbors of a vertex
    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[vertex][i] == 1) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    // Print the adjacency matrix
    public void printGraph() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                System.out.print(adjMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
