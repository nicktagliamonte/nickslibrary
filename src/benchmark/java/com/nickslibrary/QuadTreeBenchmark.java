package com.nickslibrary;

import com.nickslibrary.datastructures.advanced.QuadTree;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class QuadTreeBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        QuadTree customQuadTree;
        List<double[]> pointList;
        int numPoints = 1000;

        @Setup(Level.Trial)
        public void setUp() {
            customQuadTree = new QuadTree(0, 0, 100, 100, 4);
            pointList = new ArrayList<>();

            for (int i = 0; i < numPoints; i++) {
                double x = Math.random() * 100;
                double y = Math.random() * 100;
                customQuadTree.insert(x, y);
                pointList.add(new double[] { x, y });
            }
        }
    }

    @Benchmark
    public void testCustomQuadTreeInsert(BenchmarkState state) {
        double x = Math.random() * 100;
        double y = Math.random() * 100;
        state.customQuadTree.insert(x, y);
    }

    @Benchmark
    public void testPointListInsert(BenchmarkState state) {
        double x = Math.random() * 100;
        double y = Math.random() * 100;
        state.pointList.add(new double[] { x, y });
    }

    @Benchmark
    public void testCustomQuadTreeQuery(BenchmarkState state) {
        state.customQuadTree.query(25, 25, 50, 50);
    }

    @Benchmark
    public void testPointListQuery(BenchmarkState state) {
        double x = 25;
        double y = 25;
        double width = 50;
        double height = 50;
        List<double[]> result = new ArrayList<>();
        for (double[] point : state.pointList) {
            if (point[0] >= x && point[0] <= x + width && point[1] >= y && point[1] <= y + height) {
                result.add(point);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}