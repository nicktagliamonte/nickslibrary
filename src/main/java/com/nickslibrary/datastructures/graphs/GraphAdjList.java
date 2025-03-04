package com.nickslibrary.datastructures.graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphAdjList {
    private List<List<Integer>> adjList;
    private int numVertices;

    // Constructor
    public GraphAdjList(int numVertices) {
        this.numVertices = numVertices;
        adjList = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            adjList.add(new LinkedList<>());
        }
    }

    // Add edge
    public void addEdge(int src, int dest) {
        adjList.get(src).add(dest);
        adjList.get(dest).add(src); // For undirected graph
    }

    // Remove edge
    public void removeEdge(int src, int dest) {
        adjList.get(src).remove((Integer) dest);
        adjList.get(dest).remove((Integer) src); // For undirected graph
    }

    // Check if there is an edge between two vertices
    public boolean hasEdge(int src, int dest) {
        return adjList.get(src).contains(dest);
    }

    // Get all neighbors of a vertex
    public List<Integer> getNeighbors(int vertex) {
        return new ArrayList<>(adjList.get(vertex));
    }

    // Print the adjacency list
    public void printGraph() {
        for (int i = 0; i < numVertices; i++) {
            System.out.print(i + ": ");
            for (int neighbor : adjList.get(i)) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }
}