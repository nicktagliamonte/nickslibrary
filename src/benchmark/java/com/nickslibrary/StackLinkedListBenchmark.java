package com.nickslibrary;

import com.nickslibrary.datastructures.linear.StackLinkedList;
import org.openjdk.jmh.annotations.*;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StackLinkedListBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        StackLinkedList<Integer> customStack;
        Stack<Integer> javaStack;

        @Setup(Level.Trial)
        public void setUp() {
            customStack = new StackLinkedList<>();
            javaStack = new Stack<>();

            for (int i = 0; i < 1000; i++) {
                customStack.push(i);
                javaStack.push(i);
            }
        }
    }

    @Benchmark
    public void testCustomStackPush(BenchmarkState state) {
        state.customStack.push(1001);
    }

    @Benchmark
    public void testJavaStackPush(BenchmarkState state) {
        state.javaStack.push(1001);
    }

    @Benchmark
    public void testCustomStackPop(BenchmarkState state) {
        state.customStack.pop();
    }

    @Benchmark
    public void testJavaStackPop(BenchmarkState state) {
        state.javaStack.pop();
    }

    @Benchmark
    public void testCustomStackPeek(BenchmarkState state) {
        state.customStack.peek();
    }

    @Benchmark
    public void testJavaStackPeek(BenchmarkState state) {
        state.javaStack.peek();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}