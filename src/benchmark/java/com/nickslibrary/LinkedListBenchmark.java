package com.nickslibrary;

import com.nickslibrary.datastructures.linear.LinkedList;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class LinkedListBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        LinkedList<Integer> customLinkedList;
        java.util.LinkedList<Integer> javaLinkedList;

        @Setup(Level.Trial)
        public void setUp() {
            customLinkedList = new LinkedList<>(false, true); // Non-circular, singly linked
            javaLinkedList = new java.util.LinkedList<>();

            for (int i = 0; i < 1000; i++) {
                customLinkedList.add(i);
                javaLinkedList.add(i);
            }
        }
    }

    @Benchmark
    public void testCustomLinkedListAdd(BenchmarkState state) {
        state.customLinkedList.add(1001);
    }

    @Benchmark
    public void testJavaLinkedListAdd(BenchmarkState state) {
        state.javaLinkedList.add(1001);
    }

    @Benchmark
    public void testCustomLinkedListRemove(BenchmarkState state) {
        state.customLinkedList.remove(Integer.valueOf(500));
    }

    @Benchmark
    public void testJavaLinkedListRemove(BenchmarkState state) {
        state.javaLinkedList.remove(Integer.valueOf(500));
    }

    @Benchmark
    public void testCustomLinkedListGet(BenchmarkState state) {
        state.customLinkedList.get(500);
    }

    @Benchmark
    public void testJavaLinkedListGet(BenchmarkState state) {
        state.javaLinkedList.get(500);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}