package com.nickslibrary;

import com.nickslibrary.datastructures.linear.CircularQueue;
import org.openjdk.jmh.annotations.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CircularQueueBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        CircularQueue<Integer> customQueue;
        Queue<Integer> javaQueue;

        @Setup(Level.Trial)
        public void setUp() {
            customQueue = new CircularQueue<>(1000);
            javaQueue = new LinkedList<>();

            for (int i = 0; i < 1000; i++) {
                customQueue.enqueue(i);
                javaQueue.add(i);
            }
        }
    }

    @Benchmark
    public void testCustomQueueEnqueue(BenchmarkState state) {
        state.customQueue.enqueue(1001);
    }

    @Benchmark
    public void testJavaQueueEnqueue(BenchmarkState state) {
        state.javaQueue.add(1001);
    }

    @Benchmark
    public void testCustomQueueDequeue(BenchmarkState state) {
        state.customQueue.dequeue();
    }

    @Benchmark
    public void testJavaQueueDequeue(BenchmarkState state) {
        state.javaQueue.poll();
    }

    @Benchmark
    public void testCustomQueuePeek(BenchmarkState state) {
        state.customQueue.peek();
    }

    @Benchmark
    public void testJavaQueuePeek(BenchmarkState state) {
        state.javaQueue.peek();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}