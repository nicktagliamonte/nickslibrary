package com.nickslibrary.utils;

import java.util.List;

public class BTreeNode<T> extends TreeNode<T> {
    public BTreeNode(T value) {
        super(value);
    }

    List<TreeNode<T>> children;
}
