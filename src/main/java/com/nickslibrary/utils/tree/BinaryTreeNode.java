package com.nickslibrary.utils.tree;

public class BinaryTreeNode<T> extends TreeNode<T> {
    private BinaryTreeNode<T> left;
    private BinaryTreeNode<T> right;

    public BinaryTreeNode(T value) {
        super(value);
        this.left = null;
        this.right = null;
    }

    // Getter and Setter for Left Child
    public BinaryTreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(BinaryTreeNode<T> left) {
        this.left = left;
    }

    // Getter and Setter for Right Child
    public BinaryTreeNode<T> getRight() {
        return right;
    }

    public void setRight(BinaryTreeNode<T> right) {
        this.right = right;
    }

    // Example method: Check if node is a leaf
    public boolean isLeaf() {
        return left == null && right == null;
    }
}
