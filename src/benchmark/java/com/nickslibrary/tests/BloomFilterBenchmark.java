package com.nickslibrary;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.nickslibrary.datastructures.advanced.BloomFilterCustom;
import org.openjdk.jmh.annotations.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BloomFilterBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        BloomFilterCustom<String> customBloomFilter;
        com.google.common.hash.BloomFilter<String> guavaBloomFilter;

        @SuppressWarnings("unchecked")
        @Setup(Level.Trial)
        public void setUp() {
            int bitSetSize = 10000;
            int numHashFunctions = 3;
            Function<String, Integer>[] hashFunctions = new Function[] {
                    s -> s.hashCode(),
                    s -> s.hashCode() * 31,
                    s -> s.hashCode() * 17
            };

            customBloomFilter = new BloomFilterCustom<String>(bitSetSize, numHashFunctions, hashFunctions);
            guavaBloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 10000);

            for (int i = 0; i < 1000; i++) {
                customBloomFilter.add("element" + i);
                guavaBloomFilter.put("element" + i);
            }
        }
    }

    @Benchmark
    public void testCustomBloomFilterAdd(BenchmarkState state) {
        state.customBloomFilter.add("newElement");
    }

    @Benchmark
    public void testGuavaBloomFilterAdd(BenchmarkState state) {
        state.guavaBloomFilter.put("newElement");
    }

    @Benchmark
    public void testCustomBloomFilterMightContain(BenchmarkState state) {
        state.customBloomFilter.mightContain("element500");
    }

    @Benchmark
    public void testGuavaBloomFilterMightContain(BenchmarkState state) {
        state.guavaBloomFilter.mightContain("element500");
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}