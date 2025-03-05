package com.nickslibrary;

import com.nickslibrary.datastructures.advanced.SplayTree;
import org.openjdk.jmh.annotations.*;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SplayTreeBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        SplayTree customSplayTree;
        TreeMap<Integer, Integer> javaTreeMap;

        @Setup(Level.Trial)
        public void setUp() {
            customSplayTree = new SplayTree();
            javaTreeMap = new TreeMap<>();

            for (int i = 0; i < 1000; i++) {
                customSplayTree.insert(i);
                javaTreeMap.put(i, i);
            }
        }
    }

    @Benchmark
    public void testCustomSplayTreeInsert(BenchmarkState state) {
        state.customSplayTree.insert(1001);
    }

    @Benchmark
    public void testJavaTreeMapInsert(BenchmarkState state) {
        state.javaTreeMap.put(1001, 1001);
    }

    @Benchmark
    public void testCustomSplayTreeSearch(BenchmarkState state) {
        state.customSplayTree.search(500);
    }

    @Benchmark
    public void testJavaTreeMapSearch(BenchmarkState state) {
        state.javaTreeMap.containsKey(500);
    }

    @Benchmark
    public void testCustomSplayTreeDelete(BenchmarkState state) {
        state.customSplayTree.delete(500);
    }

    @Benchmark
    public void testJavaTreeMapDelete(BenchmarkState state) {
        state.javaTreeMap.remove(500);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}