package com.nickslibrary.utils.tree;

public class BTreeNode<T extends Comparable<T>> extends TreeNode<T> {
    private int degree;
    private T[] keys;
    private BTreeNode<T>[] children;
    private int numKeys;
    private boolean isLeaf;

    @SuppressWarnings("unchecked")
    public BTreeNode(int degree, boolean isLeaf, T value) {
        super(value);
        this.degree = degree;
        this.isLeaf = isLeaf;
        this.keys = (T[]) new Comparable[2 * degree - 1];
        this.children = new BTreeNode[2 * degree];
        this.numKeys = 0;
    }

    public T getKey(int index) {
        return keys[index];
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public T[] getKeys() {
        return keys;
    }

    public void setKeys(T[] keys) {
        this.keys = keys;
    }

    public BTreeNode<T>[] getChildren() {
        return children;
    }

    public void setChildren(BTreeNode<T>[] children) {
        this.children = children;
    }

    public void setKey(int index, T value) {
        keys[index] = value;
    }

    public BTreeNode<T> getChild(int index) {
        return children[index];
    }

    public void setChild(int index, BTreeNode<T> child) {
        children[index] = child;
    }

    public int getNumKeys() {
        return numKeys;
    }

    public void setNumKeys(int numKeys) {
        this.numKeys = numKeys;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getDegree() {
        return degree;
    }

    public int findKeyIndex(T key) {
        int i = 0;
        while (i < numKeys && key.compareTo(keys[i]) > 0) {
            i++;
        }
        return i;
    }

    public void insertNonFull(T key) {
        int i = numKeys - 1;

        if (isLeaf) {
            while (i >= 0 && key.compareTo(keys[i]) < 0) {
                keys[i + 1] = keys[i];
                i--;
            }
            keys[i + 1] = key;
            numKeys++;
        } else {
            while (i >= 0 && key.compareTo(keys[i]) < 0) {
                i--;
            }
            i++;

            if (children[i].numKeys == (2 * degree - 1)) {
                splitChild(i);
                if (key.compareTo(keys[i]) > 0) {
                    i++;
                }
            }
            children[i].insertNonFull(key);
        }
    }

    public void splitChild(int index) {
        BTreeNode<T> fullChild = children[index];
        BTreeNode<T> newChild = new BTreeNode<>(degree, fullChild.isLeaf, fullChild.keys[degree]);

        newChild.numKeys = degree - 1;

        for (int j = 0; j < degree - 1; j++) {
            newChild.keys[j] = fullChild.keys[j + degree];
        }
        if (!fullChild.isLeaf) {
            for (int j = 0; j < degree; j++) {
                newChild.children[j] = fullChild.children[j + degree];
            }
        }

        for (int j = numKeys; j > index; j--) {
            children[j + 1] = children[j];
        }
        children[index + 1] = newChild;

        for (int j = numKeys - 1; j >= index; j--) {
            keys[j + 1] = keys[j];
        }
        keys[index] = fullChild.keys[degree - 1];

        fullChild.numKeys = degree - 1;
        numKeys++;
    }

    public boolean searchKey(T key) {
        int i = findKeyIndex(key);
        if (i < numKeys && keys[i].compareTo(key) == 0) {
            return true;
        }
        return !isLeaf && children[i].searchKey(key);
    }

    public T getPredecessor(int index) {
        BTreeNode<T> current = children[index];
        while (!current.isLeaf) {
            current = current.children[current.numKeys];
        }
        return current.keys[current.numKeys - 1];
    }

    public T getSuccessor(int index) {
        BTreeNode<T> current = children[index + 1];
        while (!current.isLeaf) {
            current = current.children[0];
        }
        return current.keys[0];
    }

    public void removeKey(int index) {
        for (int i = index; i < numKeys - 1; i++) {
            keys[i] = keys[i + 1];
        }
        numKeys--;
    }
}
