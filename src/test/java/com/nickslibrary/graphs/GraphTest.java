package com.nickslibrary.graphs;

import com.nickslibrary.datastructures.graphs.GraphAdjList;
import com.nickslibrary.datastructures.graphs.GraphAdjMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    private GraphAdjList graphList;
    private GraphAdjMatrix graphMatrix;

    @BeforeEach
    public void setUp() {
        graphList = new GraphAdjList(5);
        graphMatrix = new GraphAdjMatrix(5);
    }

    @Test
    public void testAddEdgeList() {
        graphList.addEdge(0, 1);
        assertTrue(graphList.hasEdge(0, 1));
        assertTrue(graphList.hasEdge(1, 0)); // For undirected graph
    }

    @Test
    public void testAddEdgeMatrix() {
        graphMatrix.addEdge(0, 1);
        assertTrue(graphMatrix.hasEdge(0, 1));
        assertTrue(graphMatrix.hasEdge(1, 0)); // For undirected graph
    }

    @Test
    public void testRemoveEdgeList() {
        graphList.addEdge(0, 1);
        graphList.removeEdge(0, 1);
        assertFalse(graphList.hasEdge(0, 1));
        assertFalse(graphList.hasEdge(1, 0)); // For undirected graph
    }

    @Test
    public void testRemoveEdgeMatrix() {
        graphMatrix.addEdge(0, 1);
        graphMatrix.removeEdge(0, 1);
        assertFalse(graphMatrix.hasEdge(0, 1));
        assertFalse(graphMatrix.hasEdge(1, 0)); // For undirected graph
    }

    @Test
    public void testGetNeighborsList() {
        graphList.addEdge(0, 1);
        graphList.addEdge(0, 2);
        List<Integer> neighbors = graphList.getNeighbors(0);
        assertTrue(neighbors.contains(1));
        assertTrue(neighbors.contains(2));
        assertEquals(2, neighbors.size());
    }

    @Test
    public void testGetNeighborsMatrix() {
        graphMatrix.addEdge(0, 1);
        graphMatrix.addEdge(0, 2);
        List<Integer> neighbors = graphMatrix.getNeighbors(0);
        assertTrue(neighbors.contains(1));
        assertTrue(neighbors.contains(2));
        assertEquals(2, neighbors.size());
    }

    @Test
    public void testHasEdgeList() {
        graphList.addEdge(0, 1);
        assertTrue(graphList.hasEdge(0, 1));
        assertFalse(graphList.hasEdge(0, 2));
    }

    @Test
    public void testHasEdgeMatrix() {
        graphMatrix.addEdge(0, 1);
        assertTrue(graphMatrix.hasEdge(0, 1));
        assertFalse(graphMatrix.hasEdge(0, 2));
    }
}