package com.nickslibrary;

import com.nickslibrary.datastructures.advanced.FenwickTree;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class FenwickTreeBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        FenwickTree customFenwickTree;
        int[] prefixSumArray;
        int size = 1000;

        @Setup(Level.Trial)
        public void setUp() {
            customFenwickTree = new FenwickTree(size);
            prefixSumArray = new int[size + 1];

            for (int i = 0; i < size; i++) {
                customFenwickTree.update(i, i + 1);
                prefixSumArray[i + 1] = prefixSumArray[i] + (i + 1);
            }
        }
    }

    @Benchmark
    public void testCustomFenwickTreeUpdate(BenchmarkState state) {
        state.customFenwickTree.update(500, 10);
    }

    @Benchmark
    public void testPrefixSumArrayUpdate(BenchmarkState state) {
        int index = 500;
        int value = 10;
        for (int i = index + 1; i <= state.size; i++) {
            state.prefixSumArray[i] += value;
        }
    }

    @Benchmark
    public void testCustomFenwickTreeQuery(BenchmarkState state) {
        state.customFenwickTree.query(500);
    }

    @SuppressWarnings("unused")
    @Benchmark
    public void testPrefixSumArrayQuery(BenchmarkState state) {
        int sum = 0;
        for (int i = 0; i <= 500; i++) {
            sum += i + 1;
        }
    }

    @Benchmark
    public void testCustomFenwickTreeRangeQuery(BenchmarkState state) {
        state.customFenwickTree.rangeQuery(100, 500);
    }

    @SuppressWarnings("unused")
    @Benchmark
    public void testPrefixSumArrayRangeQuery(BenchmarkState state) {
        int left = 100;
        int right = 500;
        int sum = 0;
        for (int i = left; i <= right; i++) {
            sum += i + 1;
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}