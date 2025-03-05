package com.nickslibrary;

import com.nickslibrary.datastructures.graphs.GraphAdjList;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class AdjListBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        GraphAdjList customGraph;
        HashMap<Integer, List<Integer>> javaGraph;

        @Setup(Level.Trial)
        public void setUp() {
            customGraph = new GraphAdjList(1000);
            javaGraph = new HashMap<>();

            for (int i = 0; i < 1000; i++) {
                javaGraph.put(i, new ArrayList<>());
            }

            for (int i = 0; i < 1000; i++) {
                customGraph.addEdge(i, (i + 1) % 1000);
                javaGraph.get(i).add((i + 1) % 1000);
                javaGraph.get((i + 1) % 1000).add(i);
            }
        }
    }

    @Benchmark
    public void testCustomGraphAddEdge(BenchmarkState state) {
        state.customGraph.addEdge(1001, 1002);
    }

    @Benchmark
    public void testJavaGraphAddEdge(BenchmarkState state) {
        state.javaGraph.get(1001).add(1002);
        state.javaGraph.get(1002).add(1001);
    }

    @Benchmark
    public void testCustomGraphRemoveEdge(BenchmarkState state) {
        state.customGraph.removeEdge(500, 501);
    }

    @Benchmark
    public void testJavaGraphRemoveEdge(BenchmarkState state) {
        state.javaGraph.get(500).remove((Integer) 501);
        state.javaGraph.get(501).remove((Integer) 500);
    }

    @Benchmark
    public void testCustomGraphHasEdge(BenchmarkState state) {
        state.customGraph.hasEdge(500, 501);
    }

    @Benchmark
    public void testJavaGraphHasEdge(BenchmarkState state) {
        state.javaGraph.get(500).contains(501);
    }

    @Benchmark
    public void testCustomGraphGetNeighbors(BenchmarkState state) {
        state.customGraph.getNeighbors(500);
    }

    @Benchmark
    public void testJavaGraphGetNeighbors(BenchmarkState state) {
        new ArrayList<>(state.javaGraph.get(500));
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}