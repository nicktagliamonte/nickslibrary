package com.nickslibrary.utils.tree;

public abstract class TreeNode<T> {
    protected T value;

    public TreeNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}