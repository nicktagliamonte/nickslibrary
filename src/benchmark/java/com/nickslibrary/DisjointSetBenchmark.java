package com.nickslibrary;

import com.nickslibrary.datastructures.graphs.DisjointSet;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DisjointSetBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        DisjointSet customDisjointSet;
        Map<Integer, Integer> parentMap;
        Map<Integer, Integer> rankMap;
        int size = 1000;

        @Setup(Level.Trial)
        public void setUp() {
            customDisjointSet = new DisjointSet(size);
            parentMap = new HashMap<>();
            rankMap = new HashMap<>();

            for (int i = 0; i < size; i++) {
                parentMap.put(i, i);
                rankMap.put(i, 0);
            }
        }

        public int find(Map<Integer, Integer> parent, int x) {
            if (parent.get(x) != x) {
                parent.put(x, find(parent, parent.get(x))); // Path compression
            }
            return parent.get(x);
        }

        public void union(Map<Integer, Integer> parent, Map<Integer, Integer> rank, int x, int y) {
            int rootX = find(parent, x);
            int rootY = find(parent, y);

            if (rootX != rootY) {
                // Union by rank
                if (rank.get(rootX) > rank.get(rootY)) {
                    parent.put(rootY, rootX);
                } else if (rank.get(rootX) < rank.get(rootY)) {
                    parent.put(rootX, rootY);
                } else {
                    parent.put(rootY, rootX);
                    rank.put(rootX, rank.get(rootX) + 1);
                }
            }
        }
    }

    @Benchmark
    public void testCustomDisjointSetUnion(BenchmarkState state) {
        state.customDisjointSet.union(500, 501);
    }

    @Benchmark
    public void testMapUnion(BenchmarkState state) {
        state.union(state.parentMap, state.rankMap, 500, 501);
    }

    @Benchmark
    public void testCustomDisjointSetFind(BenchmarkState state) {
        state.customDisjointSet.find(500);
    }

    @Benchmark
    public void testMapFind(BenchmarkState state) {
        state.find(state.parentMap, 500);
    }

    @Benchmark
    public void testCustomDisjointSetReset(BenchmarkState state) {
        state.customDisjointSet.reset();
    }

    @Benchmark
    public void testMapReset(BenchmarkState state) {
        for (int i = 0; i < state.size; i++) {
            state.parentMap.put(i, i);
            state.rankMap.put(i, 0);
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}