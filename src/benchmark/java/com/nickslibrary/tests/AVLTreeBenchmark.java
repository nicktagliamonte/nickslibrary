package com.nickslibrary;

import com.nickslibrary.datastructures.trees.AVLTree;
import org.openjdk.jmh.annotations.*;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class AVLTreeBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        AVLTree<Integer> customAVLTree;
        TreeMap<Integer, Integer> javaTreeMap;

        @Setup(Level.Trial)
        public void setUp() {
            customAVLTree = new AVLTree<>();
            javaTreeMap = new TreeMap<>();

            for (int i = 0; i < 1000; i++) {
                customAVLTree.insert(i);
                javaTreeMap.put(i, i);
            }
        }
    }

    @Benchmark
    public void testCustomAVLTreeInsert(BenchmarkState state) {
        state.customAVLTree.insert(1001);
    }

    @Benchmark
    public void testJavaTreeMapInsert(BenchmarkState state) {
        state.javaTreeMap.put(1001, 1001);
    }

    @Benchmark
    public void testCustomAVLTreeContains(BenchmarkState state) {
        state.customAVLTree.search(500);
    }

    @Benchmark
    public void testJavaTreeMapContains(BenchmarkState state) {
        state.javaTreeMap.containsKey(500);
    }

    @Benchmark
    public void testCustomAVLTreeDelete(BenchmarkState state) {
        state.customAVLTree.delete(500);
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