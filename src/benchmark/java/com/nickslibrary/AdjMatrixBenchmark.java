package com.nickslibrary;

import com.nickslibrary.datastructures.graphs.GraphAdjMatrix;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class AdjMatrixBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        GraphAdjMatrix customGraph;
        int[][] javaGraph;
        int numVertices = 1000;

        @Setup(Level.Trial)
        public void setUp() {
            customGraph = new GraphAdjMatrix(numVertices);
            javaGraph = new int[numVertices][numVertices];

            for (int i = 0; i < numVertices; i++) {
                customGraph.addEdge(i, (i + 1) % numVertices);
                javaGraph[i][(i + 1) % numVertices] = 1;
                javaGraph[(i + 1) % numVertices][i] = 1;
            }
        }
    }

    @Benchmark
    public void testCustomGraphAddEdge(BenchmarkState state) {
        state.customGraph.addEdge(1001 % state.numVertices, 1002 % state.numVertices);
    }

    @Benchmark
    public void testJavaGraphAddEdge(BenchmarkState state) {
        state.javaGraph[1001 % state.numVertices][1002 % state.numVertices] = 1;
        state.javaGraph[1002 % state.numVertices][1001 % state.numVertices] = 1;
    }

    @Benchmark
    public void testCustomGraphRemoveEdge(BenchmarkState state) {
        state.customGraph.removeEdge(500, 501);
    }

    @Benchmark
    public void testJavaGraphRemoveEdge(BenchmarkState state) {
        state.javaGraph[500][501] = 0;
        state.javaGraph[501][500] = 0;
    }

    @Benchmark
    public void testCustomGraphHasEdge(BenchmarkState state) {
        state.customGraph.hasEdge(500, 501);
    }

    @Benchmark
    public void testJavaGraphHasEdge(BenchmarkState state) {
        state.javaGraph[500][501] = 1;
    }

    @Benchmark
    public void testCustomGraphGetNeighbors(BenchmarkState state) {
        state.customGraph.getNeighbors(500);
    }

    @Benchmark
    public void testJavaGraphGetNeighbors(BenchmarkState state) {
        int[] neighbors = state.javaGraph[500];
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i] == 1) {
                // Do something with the neighbor
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}