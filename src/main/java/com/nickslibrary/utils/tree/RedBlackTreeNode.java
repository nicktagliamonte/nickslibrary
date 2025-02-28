package com.nickslibrary.utils.tree;

public class RedBlackTreeNode<T> extends TreeNode<T> {
    public enum Color {
        RED, BLACK
    }

    protected RedBlackTreeNode<T> left;
    protected RedBlackTreeNode<T> right;
    protected RedBlackTreeNode<T> parent;
    protected Color color;

    public RedBlackTreeNode(T value) {
        super(value);
        this.color = Color.RED;
    }
    
    public RedBlackTreeNode<T> getLeft() {
        return left;
    }
    
    public void setLeft(RedBlackTreeNode<T> left) {
        this.left = left;
    }
    
    public RedBlackTreeNode<T> getRight() {
        return right;
    }
    
    public void setRight(RedBlackTreeNode<T> right) {
        this.right = right;
    }
    
    public RedBlackTreeNode<T> getParent() {
        return parent;
    }
    
    public void setParent(RedBlackTreeNode<T> parent) {
        this.parent = parent;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public boolean isRed() {
        return color == Color.RED;
    }
    
    public boolean isBlack() {
        return color == Color.BLACK;
    }

    public void setBlack() {
        this.color = Color.BLACK;
    }

    public void setRed() {
        this.color = Color.RED;
    }    
}
