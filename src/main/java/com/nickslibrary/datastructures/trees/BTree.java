package com.nickslibrary.datastructures.trees;

import java.util.ArrayList;
import java.util.List;

public class BTree<Key extends Comparable<Key>, Value> {
    private final int M = 4;
    private Node root;
    private int height;
    private int n;

    private final class Node {
        private int m;
        private List<Entry<Key, Value>> children;

        private Node(int k) {
            m = k;
            children = new ArrayList<>(M); // Avoids unchecked array creation
        }
    }

    private class Entry<K extends Comparable<K>, V> {
        private K key;
        private V val;
        private Node next;

        public Entry(K key, V val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    public BTree() {
        root = new Node(0);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return n;
    }

    public int height() {
        return height;
    }

    public Value get(Key key) {
        if (key == null)
            throw new IllegalArgumentException("argument to get() is null");
        return search(root, key, height);
    }

    private Value search(Node x, Key key, int ht) {
        List<Entry<Key, Value>> children = x.children;

        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (eq(key, children.get(j).key))
                    return children.get(j).val;
            }
        } else {
            for (int j = 0; j < x.m; j++) {
                if (j + 1 == x.m || less(key, children.get(j + 1).key))
                    return search(children.get(j).next, key, ht - 1);
            }
        }
        return null;
    }

    public void put(Key key, Value val) {
        if (key == null)
            throw new IllegalArgumentException("argument key to put() is null");
        Node u = insert(root, key, val, height);
        n++;
        if (u == null)
            return;

        Node t = new Node(2);
        t.children.add(new Entry<>(root.children.get(0).key, null, root));
        t.children.add(new Entry<>(u.children.get(0).key, null, u));
        root = t;
        height++;
    }

    private Node insert(Node h, Key key, Value val, int ht) {
        int j;
        Entry<Key, Value> t = new Entry<>(key, val, null);

        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children.get(j).key))
                    break;
            }
        } else {
            for (j = 0; j < h.m; j++) {
                if ((j + 1 == h.m) || less(key, h.children.get(j + 1).key)) {
                    Node u = insert(h.children.get(j++).next, key, val, ht - 1);
                    if (u == null)
                        return null;
                    t.key = u.children.get(0).key;
                    t.val = null;
                    t.next = u;
                    break;
                }
            }
        }

        h.children.add(j, t); // List handles shifting automatically
        h.m++;
        return h.m < M ? null : split(h);
    }

    private Node split(Node h) {
        Node t = new Node(M / 2);
        for (int j = 0; j < M / 2; j++) {
            t.children.add(h.children.get(M / 2 + j));
        }
        for (int j = M / 2; j < h.children.size(); j++) {
            h.children.remove(M / 2); // Remove elements that were moved
        }
        h.m = M / 2;
        return t;
    }

    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        List<Entry<Key, Value>> children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s.append(indent).append(children.get(j).key).append(" ").append(children.get(j).val).append("\n");
            }
        } else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0)
                    s.append(indent).append("(").append(children.get(j).key).append(")\n");
                s.append(toString(children.get(j).next, ht - 1, indent + "     "));
            }
        }
        return s.toString();
    }

    private boolean less(Key k1, Key k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Key k1, Key k2) {
        return k1.compareTo(k2) == 0;
    }
}
