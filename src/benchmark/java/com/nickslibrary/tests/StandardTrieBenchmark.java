package com.nickslibrary;

import com.nickslibrary.datastructures.trees.StandardTrie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StandardTrieBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        StandardTrie customTrie;
        PatriciaTrie<String> apacheTrie;
        int numElements = 1000;

        @Setup(Level.Trial)
        public void setUp() {
            customTrie = new StandardTrie();
            apacheTrie = new PatriciaTrie<>();

            for (int i = 0; i < numElements; i++) {
                String word = "word" + i;
                customTrie.insert(word);
                apacheTrie.put(word, String.valueOf(i));
            }
        }
    }

    @Benchmark
    public void testCustomTrieInsert(BenchmarkState state) {
        state.customTrie.insert("newWord");
    }

    @Benchmark
    public void testApacheTrieInsert(BenchmarkState state) {
        state.apacheTrie.put("newWord", "1001");
    }

    @Benchmark
    public void testCustomTrieSearch(BenchmarkState state) {
        state.customTrie.search("word500");
    }

    @Benchmark
    public void testApacheTrieSearch(BenchmarkState state) {
        state.apacheTrie.containsKey("word500");
    }

    @Benchmark
    public void testCustomTrieDelete(BenchmarkState state) {
        state.customTrie.delete("word500");
    }

    @Benchmark
    public void testApacheTrieDelete(BenchmarkState state) {
        state.apacheTrie.remove("word500");
    }

    @Benchmark
    public void testCustomTrieStartsWith(BenchmarkState state) {
        state.customTrie.startsWith("word");
    }

    @Benchmark
    public void testApacheTrieStartsWith(BenchmarkState state) {
        state.apacheTrie.prefixMap("word");
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jmh.ignoreLock", "true");
        org.openjdk.jmh.Main.main(args);
    }
}