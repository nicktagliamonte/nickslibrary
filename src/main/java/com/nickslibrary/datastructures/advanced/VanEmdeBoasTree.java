package com.nickslibrary.datastructures.advanced;

/**
 * Van Emde Boas Tree is a tree data structure that supports efficient
 * operations
 * on a universe of keys. It provides methods for insertion, deletion, and
 * predecessor/successor queries in O(log log U) time, where U is the size of
 * the universe.
 * 
 * @param <T> the type of elements in this tree
 */
public class VanEmdeBoasTree<T extends Comparable<T>> {

    private static final int UNIVERSE_SIZE = 16; // Example universe size, adjust as needed
    private boolean isEmpty;
    private T min;
    private T max;
    private VanEmdeBoasTree<T> summary;
    private VanEmdeBoasTree<T>[] clusters;

    /**
     * Constructs an empty Van Emde Boas Tree.
     */
    @SuppressWarnings("unchecked")
    public VanEmdeBoasTree() {
        this.isEmpty = true;
        this.min = null;
        this.max = null;
        this.summary = null;
        this.clusters = new VanEmdeBoasTree[UNIVERSE_SIZE];
    }

    /**
     * Inserts an element into the Van Emde Boas Tree.
     *
     * @param element the element to insert
     */
    @SuppressWarnings("unchecked")
    public void insert(T element) {
        if (isEmpty) {
            min = max = element;
            isEmpty = false;
        } else {
            if (element.compareTo(min) < 0) {
                T temp = element;
                element = min;
                min = temp;
            }
            if (element.compareTo(max) > 0) {
                max = element;
            }
            if (summary == null) {
                summary = new VanEmdeBoasTree<>();
            }
            int high = high(element);
            int low = low(element);
            if (clusters[high] == null) {
                clusters[high] = new VanEmdeBoasTree<>();
            }
            if (clusters[high].isEmpty) {
                summary.insert((T) Integer.valueOf(high));
            }
            clusters[high].insert((T) Integer.valueOf(low));
        }
    }

    /**
     * Deletes an element from the Van Emde Boas Tree.
     *
     * @param element the element to delete
     */
    @SuppressWarnings("unchecked")
    public void delete(T element) {
        if (min.equals(max)) {
            min = max = null;
            isEmpty = true;
        } else {
            if (element.equals(min)) {
                int firstCluster = (Integer) summary.findMin();
                element = (T) Integer.valueOf(index(firstCluster, clusters[firstCluster].findMin().hashCode()));
                min = element;
            }
            int high = high(element);
            int low = low(element);
            clusters[high].delete((T) Integer.valueOf(low));
            if (clusters[high].isEmpty) {
                summary.delete((T) Integer.valueOf(high));
                clusters[high] = null;
            }
            if (element.equals(max)) {
                if (summary.isEmpty) {
                    max = min;
                } else {
                    int lastCluster = ((Integer) summary.findMax()).intValue();
                    max = (T) Integer.valueOf(index(lastCluster, clusters[lastCluster].findMax().hashCode()));
                }
            }
        }
    }

    /**
     * Calculates the high part of the element.
     *
     * @param element the element to calculate the high part for
     * @return the high part of the element
     */
    private int high(T element) {
        int value = element.hashCode(); // Assuming T can be hashed to an integer
        return value / (int) Math.sqrt(UNIVERSE_SIZE);
    }

    /**
     * Calculates the low part of the element.
     *
     * @param element the element to calculate the low part for
     * @return the low part of the element
     */
    private int low(T element) {
        int value = element.hashCode(); // Assuming T can be hashed to an integer
        return value % (int) Math.sqrt(UNIVERSE_SIZE);
    }

    /**
     * Combines the high and low parts to get the original element.
     *
     * @param high the high part of the element
     * @param low  the low part of the element
     * @return the original element
     */
    private int index(int high, int low) {
        return high * (int) Math.sqrt(UNIVERSE_SIZE) + low;
    }

    /**
     * Finds the minimum element in the Van Emde Boas Tree.
     *
     * @return the minimum element, or null if the tree is empty
     */
    public T findMin() {
        return min;
    }

    /**
     * Finds the maximum element in the Van Emde Boas Tree.
     *
     * @return the maximum element, or null if the tree is empty
     */
    public T findMax() {
        return max;
    }

    /**
     * Finds the predecessor of a given element in the Van Emde Boas Tree.
     *
     * @param element the element to find the predecessor for
     * @return the predecessor of the element, or null if no predecessor exists
     */
    @SuppressWarnings("unchecked")
    public T predecessor(T element) {
        if (max != null && element.compareTo(min) > 0) {
            int high = high(element);
            int low = low(element);
            int minLow = clusters[high].findMin().hashCode();
            if (low > minLow) {
                T predLow = clusters[high].predecessor((T) Integer.valueOf(low));
                return (T) Integer.valueOf(index(high, predLow.hashCode()));
            } else {
                int predCluster = ((Integer) summary.predecessor((T) Integer.valueOf(high))).intValue();
                if (predCluster == -1) {
                    if (min != null && element.compareTo(min) > 0) {
                        return min;
                    } else {
                        return null;
                    }
                } else {
                    int predLow = clusters[predCluster].findMax().hashCode();
                    return (T) Integer.valueOf(index(predCluster, predLow));
                }
            }
        } else {
            return null;
        }
    }

    /**
     * Finds the successor of a given element in the Van Emde Boas Tree.
     *
     * @param element the element to find the successor for
     * @return the successor of the element, or null if no successor exists
     */
    @SuppressWarnings("unchecked")
    public T successor(T element) {
        if (min != null && element.compareTo(max) < 0) {
            int high = high(element);
            int low = low(element);
            int maxLow = clusters[high].findMax().hashCode();
            if (low < maxLow) {
                T succLow = clusters[high].successor((T) Integer.valueOf(low));
                return (T) Integer.valueOf(index(high, succLow.hashCode()));
            } else {
                Integer succCluster = (Integer) summary.successor((T) Integer.valueOf(high));
                if (succCluster == null) {
                    if (max != null && element.compareTo(max) < 0) {
                        return max;
                    } else {
                        return null;
                    }
                } else {
                    int succLow = clusters[succCluster].findMin().hashCode();
                    return (T) Integer.valueOf(index(succCluster, succLow));
                }
            }
        } else {
            return null;
        }
    }

    /**
     * Checks if the Van Emde Boas Tree contains a given element.
     *
     * @param element the element to check for
     * @return true if the element is in the tree, false otherwise
     */
    @SuppressWarnings("unchecked")
    public boolean contains(T element) {
        if (element.equals(min) || element.equals(max)) {
            return true;
        } else {
            int high = high(element);
            int low = low(element);
            if (clusters[high] == null) {
                return false;
            } else {
                return clusters[high].contains((T) Integer.valueOf(low));
            }
        }
    }

    /**
     * Clears the Van Emde Boas Tree, removing all elements.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        this.isEmpty = true;
        this.min = null;
        this.max = null;
        this.summary = null;
        this.clusters = new VanEmdeBoasTree[UNIVERSE_SIZE];
    }
}