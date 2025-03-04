package com.nickslibrary.datastructures.advanced;

import java.util.Arrays;

/**
 * This is a really weird one that I debated even including, thus the
 * ridiculously extensive preliminary comments.
 * 
 * HyperLogLog is a probabilistic data structure used for cardinality
 * estimation,
 * i.e., counting the number of distinct elements in a dataset.
 * 
 * This implementation uses a specified number of registers to maintain the
 * state
 * and provides methods to add elements, estimate cardinality, merge with
 * another
 * HyperLogLog, clear the registers, and serialize/deserialize the state.
 * 
 * The accuracy of the estimation depends on the number of registers, which is
 * determined by the log base 2 of the number of registers (log2m).
 * 
 * Methods:
 * - {@link #HyperLogLog(int)}: Constructs a HyperLogLog with the specified
 * number of registers.
 * - {@link #add(String)}: Adds an element to the HyperLogLog.
 * - {@link #estimate()}: Estimates the cardinality of the elements added to the
 * HyperLogLog.
 * - {@link #merge(HyperLogLog)}: Merges another HyperLogLog into this one.
 * - {@link #clear()}: Clears the HyperLogLog, resetting all registers to zero.
 * - {@link #serialize()}: Serializes the HyperLogLog to a byte array.
 * - {@link #deserialize(byte[])}: Deserializes a byte array to a HyperLogLog.
 * - {@link #getAlphaMM(int)}: Returns the alphaMM value based on the number of
 * registers.
 * 
 * Constants:
 * - ALPHA_16, ALPHA_32, ALPHA_64, ALPHA_LARGE: Constants used for alphaMM
 * calculation.
 * 
 * Fields:
 * - registers: An array of integers representing the registers.
 * - numRegisters: The number of registers.
 * - log2m: The log base 2 of the number of registers.
 * - alphaMM: The alphaMM value used for estimation.
 * 
 * Usage:
 * 
 * <pre>
 * {@code
 * HyperLogLog hll = new HyperLogLog(14); // 2^14 registers
 * hll.add("apple");
 * hll.add("banana");
 * hll.add("cherry");
 * System.out.println("Estimated cardinality: " + hll.estimate());
 * }
 * </pre>
 * 
 * @see <a href="https://en.wikipedia.org/wiki/HyperLogLog">HyperLogLog on
 *      Wikipedia</a>
 */
public class HyperLogLog {

    private static final double ALPHA_16 = 0.673;
    private static final double ALPHA_32 = 0.697;
    private static final double ALPHA_64 = 0.709;
    private static final double ALPHA_LARGE = 0.7213 / (1 + 1.079 / 16384);

    private int[] registers;
    private int numRegisters;
    private int log2m;
    private double alphaMM;

    /**
     * Constructs a HyperLogLog with the specified number of registers.
     *
     * @param log2m The log base 2 of the number of registers.
     */
    public HyperLogLog(int log2m) {
        this.log2m = log2m;
        this.numRegisters = 1 << log2m;
        this.registers = new int[numRegisters];
        this.alphaMM = getAlphaMM(numRegisters);
    }

    /**
     * Adds an element to the HyperLogLog.
     *
     * @param item The item to add.
     */
    public void add(String item) {
        int hash = item.hashCode();
        int registerIndex = hash >>> (Integer.SIZE - log2m);
        int w = hash << log2m | (1 << (log2m - 1));
        int leadingZeros = Integer.numberOfLeadingZeros(w) + 1;
        registers[registerIndex] = Math.max(registers[registerIndex], leadingZeros);
    }

    /**
     * Estimates the cardinality of the elements added to the HyperLogLog.
     *
     * @return The estimated cardinality.
     */
    public double estimate() {
        double sum = 0.0;
        int zeroCount = 0;
        for (int register : registers) {
            sum += 1.0 / (1 << register);
            if (register == 0) {
                zeroCount++;
            }
        }
        double estimate = alphaMM / sum;
        if (estimate <= 2.5 * numRegisters) {
            if (zeroCount > 0) {
                estimate = numRegisters * Math.log((double) numRegisters / zeroCount);
            }
        } else if (estimate > (1 << 30) / 30.0) {
            estimate = -(1 << 30) * Math.log(1.0 - estimate / (1 << 30));
        }
        return estimate;
    }

    /**
     * Merges another HyperLogLog into this one.
     *
     * @param other The other HyperLogLog to merge.
     */
    public void merge(HyperLogLog other) {
        if (this.numRegisters != other.numRegisters) {
            throw new IllegalArgumentException("Number of registers must match for merging.");
        }
        for (int i = 0; i < numRegisters; i++) {
            this.registers[i] = Math.max(this.registers[i], other.registers[i]);
        }
    }

    /**
     * Clears the HyperLogLog, resetting all registers to zero.
     */
    public void clear() {
        Arrays.fill(registers, 0);
    }

    /**
     * Serializes the HyperLogLog to a byte array.
     *
     * @return The serialized byte array.
     */
    public byte[] serialize() {
        byte[] bytes = new byte[numRegisters];
        for (int i = 0; i < numRegisters; i++) {
            bytes[i] = (byte) registers[i];
        }
        return bytes;
    }

    /**
     * Deserializes a byte array to a HyperLogLog.
     *
     * @param bytes The byte array to deserialize.
     */
    public void deserialize(byte[] bytes) {
        if (bytes.length != numRegisters) {
            throw new IllegalArgumentException("Byte array length must match number of registers.");
        }
        for (int i = 0; i < numRegisters; i++) {
            registers[i] = bytes[i];
        }
    }

    private double getAlphaMM(int numRegisters) {
        switch (numRegisters) {
            case 16:
                return ALPHA_16 * numRegisters * numRegisters;
            case 32:
                return ALPHA_32 * numRegisters * numRegisters;
            case 64:
                return ALPHA_64 * numRegisters * numRegisters;
            default:
                return ALPHA_LARGE * numRegisters * numRegisters;
        }
    }
}