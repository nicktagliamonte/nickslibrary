package com.nickslibrary.utils.tree;

public class AVLTreeNode<T> extends TreeNode<T> {
    protected AVLTreeNode<T> left;
    protected AVLTreeNode<T> right;
    protected int height;

    public AVLTreeNode(T value) {
        super(value);
        this.height = 1; // New nodes start at height 1
    }

    public AVLTreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(AVLTreeNode<T> left) {
        this.left = left;
        updateHeight();
    }

    public AVLTreeNode<T> getRight() {
        return right;
    }

    public void setRight(AVLTreeNode<T> right) {
        this.right = right;
        updateHeight();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void updateHeight() {
        int leftHeight = (left == null) ? 0 : left.height;
        int rightHeight = (right == null) ? 0 : right.height;
        this.height = Math.max(leftHeight, rightHeight) + 1;
    }

    public int getBalanceFactor() {
        int leftHeight = (left == null) ? 0 : left.height;
        int rightHeight = (right == null) ? 0 : right.height;
        return leftHeight - rightHeight;
    }
}
