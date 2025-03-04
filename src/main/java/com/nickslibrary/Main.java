package com.nickslibrary;

import com.nickslibrary.datastructures.advanced.SuffixTree;

public class Main {
    public static void main(String[] args) {
        SuffixTree suffixTree = new SuffixTree("banana");
        System.out.println(suffixTree.longestRepeatedSubstring());
    }
}
