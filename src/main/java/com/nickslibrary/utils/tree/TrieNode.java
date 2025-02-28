package com.nickslibrary.utils.tree;

import java.util.Map;

public class TrieNode<T> extends TreeNode<T> {
    public TrieNode(T value) {
        super(value);
    }

    Map<Character, TreeNode<T>> children;
}