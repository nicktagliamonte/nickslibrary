package com.nickslibrary;

import com.nickslibrary.datastructures.linear.DynamicArray;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DynamicArrayBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        DynamicArray<Integer> customDynamicArray;
        ArrayList<Integer> javaArrayList;

        @Setup(Level.Trial)
        public void setUp() {
            customDynamicArray = new DynamicArray<>();
            javaArrayList = new ArrayList<>();

            for (int i = 0; i < 1000; i++) {
                customDynamicArray.add(i);
                javaArrayList.add(i);
            }
        }
    }

    @Benchmark
    public void testCustomDynamicArrayAdd(BenchmarkState state) {
        state.customDynamicArray.add(1001);
    }

    @Benchmark
    public void testJavaArrayListAdd(BenchmarkState state) {
        state.javaArrayList.add(1001);
    }

    @Benchmark
    public void testCustomDynamicArrayRemove(BenchmarkState state) {
        state.customDynamicArray.remove(500);
    }

    @Benchmark
    public void testJavaArrayListRemove(BenchmarkState state) {
        state.javaArrayList.remove(Integer.valueOf(500));
    }

    @Benchmark
    public void testCustomDynamicArrayGet(BenchmarkState state) {
        state.customDynamicArray.get(500);
    }

    @Benchmark
    public void testJavaArrayListGet(BenchmarkState state) {
        state.javaArrayList.get(500);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}