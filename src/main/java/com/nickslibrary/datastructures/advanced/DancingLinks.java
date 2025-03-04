package com.nickslibrary.datastructures.advanced;

public class DancingLinks {

    private static class Node {
        Node left, right, up, down;
        ColumnNode column;

        Node() {
            left = right = up = down = this;
        }

        Node(ColumnNode column) {
            this();
            this.column = column;
        }

        void linkRight(Node node) {
            node.right = right;
            node.right.left = node;
            node.left = this;
            right = node;
        }

        void linkDown(Node node) {
            node.down = down;
            node.down.up = node;
            node.up = this;
            down = node;
        }

        void unlinkUpDown() {
            up.down = down;
            down.up = up;
        }

        void relinkUpDown() {
            up.down = this;
            down.up = this;
        }
    }

    private static class ColumnNode extends Node {
        int size;

        ColumnNode(String name) {
            super();
            this.size = 0;
            column = this;
        }

        void cover() {
            unlinkLeftRight();
            for (Node row = down; row != this; row = row.down) {
                for (Node node = row.right; node != row; node = node.right) {
                    node.unlinkUpDown();
                    node.column.size--;
                }
            }
        }

        void uncover() {
            for (Node row = up; row != this; row = row.up) {
                for (Node node = row.left; node != row; node = node.left) {
                    node.column.size++;
                    node.relinkUpDown();
                }
            }
            relinkLeftRight();
        }

        void unlinkLeftRight() {
            left.right = right;
            right.left = left;
        }

        void relinkLeftRight() {
            left.right = this;
            right.left = this;
        }
    }

    private ColumnNode header;
    private int solutionCount;

    public DancingLinks(int[][] matrix) {
        header = new ColumnNode("header");
        ColumnNode[] columnNodes = new ColumnNode[matrix[0].length];

        for (int i = 0; i < matrix[0].length; i++) {
            columnNodes[i] = new ColumnNode(Integer.toString(i));
            header.linkRight(columnNodes[i]);
        }

        for (int i = 0; i < matrix.length; i++) {
            Node prev = null;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    ColumnNode col = columnNodes[j];
                    Node newNode = new Node(col);
                    col.up.linkDown(newNode);
                    if (prev == null) {
                        prev = newNode;
                    }
                    prev.linkRight(newNode);
                    prev = newNode;
                    col.size++;
                }
            }
        }
    }

    public void search(int k) {
        if (header.right == header) {
            solutionCount++;
            return;
        }

        ColumnNode col = selectColumnNode();
        col.cover();

        for (Node row = col.down; row != col; row = row.down) {
            for (Node node = row.right; node != row; node = node.right) {
                node.column.cover();
            }

            search(k + 1);

            for (Node node = row.left; node != row; node = node.left) {
                node.column.uncover();
            }
        }

        col.uncover();
    }

    private ColumnNode selectColumnNode() {
        int minSize = Integer.MAX_VALUE;
        ColumnNode selected = null;
        for (ColumnNode col = (ColumnNode) header.right; col != header; col = (ColumnNode) col.right) {
            if (col.size < minSize) {
                minSize = col.size;
                selected = col;
            }
        }
        return selected;
    }

    public int getSolutionCount() {
        return solutionCount;
    }
}