package com.nickslibrary.datastructures.advanced;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static class Point {
        double[] coordinates;

        Point(double... coordinates) {
            this.coordinates = coordinates;
        }
    }

    private static class Node {
        Point point;
        Node left, right;

        Node(Point point) {
            this.point = point;
            this.left = this.right = null;
        }
    }

    private Node root;
    private int k; // Number of dimensions

    public KdTree(int k) {
        this.k = k;
        this.root = null;
    }

    public void insert(double... coordinates) {
        if (coordinates.length != k) {
            throw new IllegalArgumentException("Point must have " + k + " dimensions.");
        }
        root = insert(root, new Point(coordinates), 0);
    }

    private Node insert(Node node, Point point, int depth) {
        if (node == null) {
            return new Node(point);
        }

        int cd = depth % k; // Current dimension

        if (point.coordinates[cd] < node.point.coordinates[cd]) {
            node.left = insert(node.left, point, depth + 1);
        } else {
            node.right = insert(node.right, point, depth + 1);
        }

        return node;
    }

    public boolean search(double... coordinates) {
        if (coordinates.length != k) {
            throw new IllegalArgumentException("Point must have " + k + " dimensions.");
        }
        return search(root, new Point(coordinates), 0);
    }

    private boolean search(Node node, Point point, int depth) {
        if (node == null) {
            return false;
        }

        if (arePointsEqual(node.point, point)) {
            return true;
        }

        int cd = depth % k; // Current dimension

        if (point.coordinates[cd] < node.point.coordinates[cd]) {
            return search(node.left, point, depth + 1);
        } else {
            return search(node.right, point, depth + 1);
        }
    }

    private boolean arePointsEqual(Point p1, Point p2) {
        for (int i = 0; i < k; i++) {
            if (p1.coordinates[i] != p2.coordinates[i]) {
                return false;
            }
        }
        return true;
    }

    public List<Point> rangeSearch(double[] minCoordinates, double[] maxCoordinates) {
        if (minCoordinates.length != k || maxCoordinates.length != k) {
            throw new IllegalArgumentException("Points must have " + k + " dimensions.");
        }
        List<Point> result = new ArrayList<>();
        rangeSearch(root, minCoordinates, maxCoordinates, 0, result);
        return result;
    }

    private void rangeSearch(Node node, double[] minCoordinates, double[] maxCoordinates, int depth,
            List<Point> result) {
        if (node == null) {
            return;
        }

        if (isPointInRange(node.point, minCoordinates, maxCoordinates)) {
            result.add(node.point);
        }

        int cd = depth % k; // Current dimension

        if (node.point.coordinates[cd] >= minCoordinates[cd]) {
            rangeSearch(node.left, minCoordinates, maxCoordinates, depth + 1, result);
        }
        if (node.point.coordinates[cd] <= maxCoordinates[cd]) {
            rangeSearch(node.right, minCoordinates, maxCoordinates, depth + 1, result);
        }
    }

    private boolean isPointInRange(Point point, double[] minCoordinates, double[] maxCoordinates) {
        for (int i = 0; i < k; i++) {
            if (point.coordinates[i] < minCoordinates[i] || point.coordinates[i] > maxCoordinates[i]) {
                return false;
            }
        }
        return true;
    }
}