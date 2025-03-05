package com.nickslibrary;

import com.nickslibrary.datastructures.trees.Treap;
import org.openjdk.jmh.annotations.*;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class TreapBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        Treap customTreap;
        TreeMap<Integer, Integer> javaTreeMap;

        @Setup(Level.Trial)
        public void setUp() {
            customTreap = new Treap();
            javaTreeMap = new TreeMap<>();

            for (int i = 0; i < 1000; i++) {
                customTreap.insert(i);
                javaTreeMap.put(i, i);
            }
        }
    }

    @Benchmark
    public void testCustomTreapInsert(BenchmarkState state) {
        state.customTreap.insert(1001);
    }

    @Benchmark
    public void testJavaTreeMapInsert(BenchmarkState state) {
        state.javaTreeMap.put(1001, 1001);
    }

    @Benchmark
    public void testCustomTreapSearch(BenchmarkState state) {
        state.customTreap.search(500);
    }

    @Benchmark
    public void testJavaTreeMapSearch(BenchmarkState state) {
        state.javaTreeMap.containsKey(500);
    }

    @Benchmark
    public void testCustomTreapDelete(BenchmarkState state) {
        state.customTreap.delete(500);
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