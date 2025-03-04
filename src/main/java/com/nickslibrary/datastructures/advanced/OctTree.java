package com.nickslibrary.datastructures.advanced;

import java.util.ArrayList;
import java.util.List;

public class OctTree {

    private static class Point {
        double x, y, z;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static class Node {
        double x, y, z, size;
        List<Point> points;
        Node[] children;

        Node(double x, double y, double z, double size) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.size = size;
            this.points = new ArrayList<>();
            this.children = new Node[8];
        }

        boolean contains(Point point) {
            return point.x >= x && point.x <= x + size &&
                    point.y >= y && point.y <= y + size &&
                    point.z >= z && point.z <= z + size;
        }

        boolean intersects(double x, double y, double z, double size) {
            return !(x + size < this.x || x > this.x + this.size ||
                    y + size < this.y || y > this.y + this.size ||
                    z + size < this.z || z > this.z + this.size);
        }
    }

    private Node root;
    private int capacity;

    public OctTree(double x, double y, double z, double size, int capacity) {
        this.root = new Node(x, y, z, size);
        this.capacity = capacity;
    }

    public void insert(double x, double y, double z) {
        insert(root, new Point(x, y, z));
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
        double halfSize = node.size / 2;

        node.children[0] = new Node(node.x, node.y, node.z, halfSize);
        node.children[1] = new Node(node.x + halfSize, node.y, node.z, halfSize);
        node.children[2] = new Node(node.x, node.y + halfSize, node.z, halfSize);
        node.children[3] = new Node(node.x + halfSize, node.y + halfSize, node.z, halfSize);
        node.children[4] = new Node(node.x, node.y, node.z + halfSize, halfSize);
        node.children[5] = new Node(node.x + halfSize, node.y, node.z + halfSize, halfSize);
        node.children[6] = new Node(node.x, node.y + halfSize, node.z + halfSize, halfSize);
        node.children[7] = new Node(node.x + halfSize, node.y + halfSize, node.z + halfSize, halfSize);
    }

    public List<Point> query(double x, double y, double z, double size) {
        List<Point> found = new ArrayList<>();
        query(root, x, y, z, size, found);
        return found;
    }

    private void query(Node node, double x, double y, double z, double size, List<Point> found) {
        if (!node.intersects(x, y, z, size)) {
            return;
        }

        for (Point point : node.points) {
            if (point.x >= x && point.x <= x + size &&
                    point.y >= y && point.y <= y + size &&
                    point.z >= z && point.z <= z + size) {
                found.add(point);
            }
        }

        if (node.children[0] != null) {
            for (Node child : node.children) {
                query(child, x, y, z, size, found);
            }
        }
    }
}