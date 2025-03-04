package com.nickslibrary.datastructures.advanced;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BloomierFilter {

    private static final int HASH_COUNT = 3;
    private static final int BITSET_SIZE = 1024;

    private BitSet bitSet;
    private Map<Integer, Integer> keyValueMap;
    private Random random;

    public BloomierFilter() {
        this.bitSet = new BitSet(BITSET_SIZE);
        this.keyValueMap = new HashMap<>();
        this.random = new Random();
    }

    private int[] getHashes(String key) {
        int[] hashes = new int[HASH_COUNT];
        random.setSeed(key.hashCode());
        for (int i = 0; i < HASH_COUNT; i++) {
            hashes[i] = random.nextInt(BITSET_SIZE);
        }
        return hashes;
    }

    public void set(String key, int value) {
        int[] hashes = getHashes(key);
        for (int hash : hashes) {
            bitSet.set(hash);
        }
        keyValueMap.put(key.hashCode(), value);
    }

    public Integer get(String key) {
        int[] hashes = getHashes(key);
        for (int hash : hashes) {
            if (!bitSet.get(hash)) {
                return null;
            }
        }
        return keyValueMap.get(key.hashCode());
    }
}