package com.nickslibrary.datastructures.advanced;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {

    private static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Node {
        double x, y, width, height;
        List<Point> points;
        Node[] children;

        Node(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.points = new ArrayList<>();
            this.children = new Node[4];
        }

        boolean contains(Point point) {
            return point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height;
        }

        boolean intersects(double x, double y, double width, double height) {
            return !(x + width < this.x || x > this.x + this.width || y + height < this.y || y > this.y + this.height);
        }
    }

    private Node root;
    private int capacity;

    public QuadTree(double x, double y, double width, double height, int capacity) {
        this.root = new Node(x, y, width, height);
        this.capacity = capacity;
    }

    public void insert(double x, double y) {
        insert(root, new Point(x, y));
    }

    private void insert(Node node, Point point) {
        if (!node.contains(point)) {
            return;
        }

        if (node.points.size() < capacity) {
            node.points.add(point);
        } else {
            if (node.children[0] == null) {
                subdivide(node);
            }

            for (Node child : node.children) {
                insert(child, point);
            }
        }
    }

    private void subdivide(Node node) {
        double halfWidth = node.width / 2;
        double halfHeight = node.height / 2;

        node.children[0] = new Node(node.x, node.y, halfWidth, halfHeight);
        node.children[1] = new Node(node.x + halfWidth, node.y, halfWidth, halfHeight);
        node.children[2] = new Node(node.x, node.y + halfHeight, halfWidth, halfHeight);
        node.children[3] = new Node(node.x + halfWidth, node.y + halfHeight, halfWidth, halfHeight);
    }

    public List<Point> query(double x, double y, double width, double height) {
        List<Point> found = new ArrayList<>();
        query(root, x, y, width, height, found);
        return found;
    }

    private void query(Node node, double x, double y, double width, double height, List<Point> found) {
        if (!node.intersects(x, y, width, height)) {
            return;
        }

        for (Point point : node.points) {
            if (point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height) {
                found.add(point);
            }
        }

        if (node.children[0] != null) {
            for (Node child : node.children) {
                query(child, x, y, width, height, found);
            }
        }
    }
}