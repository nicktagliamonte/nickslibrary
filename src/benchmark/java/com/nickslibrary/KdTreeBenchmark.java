package com.nickslibrary;

import com.nickslibrary.datastructures.advanced.KdTree;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class KdTreeBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        KdTree customKdTree;
        List<double[]> pointList;
        int dimensions = 3;
        int numPoints = 1000;

        @Setup(Level.Trial)
        public void setUp() {
            customKdTree = new KdTree(dimensions);
            pointList = new ArrayList<>();

            for (int i = 0; i < numPoints; i++) {
                double[] point = new double[dimensions];
                for (int j = 0; j < dimensions; j++) {
                    point[j] = Math.random();
                }
                customKdTree.insert(point);
                pointList.add(point);
            }
        }
    }

    @Benchmark
    public void testCustomKdTreeInsert(BenchmarkState state) {
        double[] point = new double[state.dimensions];
        for (int i = 0; i < state.dimensions; i++) {
            point[i] = Math.random();
        }
        state.customKdTree.insert(point);
    }

    @Benchmark
    public void testPointListInsert(BenchmarkState state) {
        double[] point = new double[state.dimensions];
        for (int i = 0; i < state.dimensions; i++) {
            point[i] = Math.random();
        }
        state.pointList.add(point);
    }

    @Benchmark
    public void testCustomKdTreeSearch(BenchmarkState state) {
        double[] point = state.pointList.get(500);
        state.customKdTree.search(point);
    }

    @Benchmark
    public void testPointListSearch(BenchmarkState state) {
        double[] point = state.pointList.get(500);
        for (double[] p : state.pointList) {
            boolean found = true;
            for (int i = 0; i < state.dimensions; i++) {
                if (p[i] != point[i]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
    }

    @Benchmark
    public void testCustomKdTreeRangeSearch(BenchmarkState state) {
        double[] minCoordinates = new double[state.dimensions];
        double[] maxCoordinates = new double[state.dimensions];
        for (int i = 0; i < state.dimensions; i++) {
            minCoordinates[i] = 0.25;
            maxCoordinates[i] = 0.75;
        }
        state.customKdTree.rangeSearch(minCoordinates, maxCoordinates);
    }

    @Benchmark
    public void testPointListRangeSearch(BenchmarkState state) {
        double[] minCoordinates = new double[state.dimensions];
        double[] maxCoordinates = new double[state.dimensions];
        for (int i = 0; i < state.dimensions; i++) {
            minCoordinates[i] = 0.25;
            maxCoordinates[i] = 0.75;
        }
        List<double[]> result = new ArrayList<>();
        for (double[] point : state.pointList) {
            boolean inRange = true;
            for (int i = 0; i < state.dimensions; i++) {
                if (point[i] < minCoordinates[i] || point[i] > maxCoordinates[i]) {
                    inRange = false;
                    break;
                }
            }
            if (inRange) {
                result.add(point);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}