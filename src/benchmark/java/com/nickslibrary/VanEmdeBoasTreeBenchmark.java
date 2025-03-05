package com.nickslibrary;

import com.nickslibrary.datastructures.advanced.VanEmdeBoasTree;
import org.openjdk.jmh.annotations.*;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class VanEmdeBoasTreeBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        VanEmdeBoasTree<Integer> customVanEmdeBoasTree;
        TreeMap<Integer, Integer> javaTreeMap;
        int numElements = 1000;

        @Setup(Level.Trial)
        public void setUp() {
            customVanEmdeBoasTree = new VanEmdeBoasTree<>();
            javaTreeMap = new TreeMap<>();

            for (int i = 0; i < numElements; i++) {
                customVanEmdeBoasTree.insert(i);
                javaTreeMap.put(i, i);
            }
        }
    }

    @Benchmark
    public void testCustomVanEmdeBoasTreeInsert(BenchmarkState state) {
        state.customVanEmdeBoasTree.insert(1001);
    }

    @Benchmark
    public void testJavaTreeMapInsert(BenchmarkState state) {
        state.javaTreeMap.put(1001, 1001);
    }

    @Benchmark
    public void testCustomVanEmdeBoasTreeDelete(BenchmarkState state) {
        state.customVanEmdeBoasTree.delete(500);
    }

    @Benchmark
    public void testJavaTreeMapDelete(BenchmarkState state) {
        state.javaTreeMap.remove(500);
    }

    @Benchmark
    public void testCustomVanEmdeBoasTreeContains(BenchmarkState state) {
        state.customVanEmdeBoasTree.contains(500);
    }

    @Benchmark
    public void testJavaTreeMapContains(BenchmarkState state) {
        state.javaTreeMap.containsKey(500);
    }

    @Benchmark
    public void testCustomVanEmdeBoasTreeFindMin(BenchmarkState state) {
        state.customVanEmdeBoasTree.findMin();
    }

    @Benchmark
    public void testJavaTreeMapFindMin(BenchmarkState state) {
        state.javaTreeMap.firstKey();
    }

    @Benchmark
    public void testCustomVanEmdeBoasTreeFindMax(BenchmarkState state) {
        state.customVanEmdeBoasTree.findMax();
    }

    @Benchmark
    public void testJavaTreeMapFindMax(BenchmarkState state) {
        state.javaTreeMap.lastKey();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}