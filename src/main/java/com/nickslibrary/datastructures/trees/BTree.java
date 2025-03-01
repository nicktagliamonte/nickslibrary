package com.nickslibrary.datastructures.trees;

import java.util.Iterator;
import com.nickslibrary.utils.tree.BTreeNode;

/**
 * B-Tree implementation supporting insertions, deletions, searches, and
 * traversals.
 *
 * @param <T> The type of elements stored in the B-Tree.
 */
public class BTree<T extends Comparable<T>> implements Iterable<T> {

    // Pointer to root node
    private BTreeNode<T> root;

    // Minimum degree
    private int t;

    public BTree(int t) {
        this.t = t;
        root = null;
    }

    // Function to search a key in this tree
    public BTreeNode<T> search(int key) {
        return (root == null) ? null : root.search(key);
    }

    // Function to insert a key into the B-tree
    public void insert(int key) {
        if (root == null) {
            root = new BTreeNode<T>(t, true);
            root.keys[0] = key;
            root.n = 1;
        } else {
            if (root.n == 2 * t - 1) {
                BTreeNode<T> newRoot = new BTreeNode<T>(t, false);
                newRoot.children[0] = root;
                newRoot.splitChild(0, root);

                int i = 0;

                if (newRoot.keys[0] < key)
                    i++;

                newRoot.children[i].insertNonFull(key);
                root = newRoot;
            } else {
                root.insertNonFull(key);
            }
        }
    }

    // Function to print the entire B-tree
    public void printBTree() {
        if (root != null)
            root.printInOrder();

        System.out.println();
    }
    
    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

}
