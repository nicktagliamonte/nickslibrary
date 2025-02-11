package com.nickslibrary.datastructures.linear;

import java.util.Iterator;

/**
 * A generic LinkedList class that supports both singly and doubly linked lists
 * with optional circular behavior.
 *
 * @param <T> The type of elements in the list.
 */
public class LinkedList<T> implements Iterable<T> {

    /**
     * Private inner class representing a node in the list.
     */
    public static class Node<T> {
        T data; // Data stored in the node
        public Node<T> next; // Pointer to the next node
        Node<T> prev; // Pointer to the previous node (for doubly linked lists)

        /**
         * Constructs a new node with the given data.
         * 
         * @param data The data to store in the node.
         */
        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    // Fields for the LinkedList class
    public Node<T> head;
    public Node<T> tail;
    private int size;
    public boolean isCircular;
    public boolean isSinglyLinked;

    /**
     * Constructs a new LinkedList with the specified properties.
     * 
     * @param isCircular     If true, the list will be circular.
     * @param isSinglyLinked If true, the list will be singly linked.
     */
    public LinkedList(boolean isCircular, boolean isSinglyLinked) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.isCircular = isCircular;
        this.isSinglyLinked = isSinglyLinked;
    }

    /**
     * Adds a new node with the specified data to the list.
     * 
     * @param data The data to store in the new node.
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        // Handle adding to an empty list
        if (head == null) {
            head = tail = newNode;
            if (isCircular) {
                head.next = head;
                if (!isSinglyLinked) {
                    head.prev = head;
                }
            }
        } else {
            tail.next = newNode;
            if (!isSinglyLinked) {
                newNode.prev = tail;
            }
            tail = newNode;

            // Adjust circular references if necessary
            if (isCircular) {
                tail.next = head;
                if (!isSinglyLinked) {
                    head.prev = tail;
                }
            }
        }

        size++;
    }

    /**
     * Inserts a new node at the specified index.
     * 
     * @param index The index where the new node should be inserted.
     * @param data  The data to store in the new node.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public void insert(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        Node<T> newNode = new Node<>(data);

        // Case 1: Insert at the beginning
        if (index == 0) {
            newNode.next = head;
            if (head != null) {
                head.prev = isSinglyLinked ? null : newNode;
            }
            head = newNode;

            // Adjust circular references
            if (isCircular) {
                if (tail == null) {
                    tail = newNode;
                    head.next = head;
                    if (!isSinglyLinked) {
                        head.prev = head;
                    }
                } else {
                    tail.next = head;
                    if (!isSinglyLinked) {
                        head.prev = tail;
                    }
                }
            }

            if (tail == null) {
                tail = head; // If the list was empty, set the tail
            }
        } else if (index == size) {
            add(data); // Use the add method to insert at the end
            return;
        } else {
            Node<T> current;
            // Case 2: Insert at a middle position
            if (index < size / 2 || isSinglyLinked) {
                current = head;
                for (int i = 0; i < index - 1; i++) {
                    current = current.next;
                }
            } else {
                current = tail;
                for (int i = size - 1; i > index - 1; i--) {
                    current = current.prev;
                }
            }

            newNode.next = current.next;
            current.next = newNode;

            if (!isSinglyLinked) {
                newNode.prev = current;
                if (newNode.next != null) {
                    newNode.next.prev = newNode;
                }
            }
        }

        // Adjust circular references conditionally
        if (isCircular && tail != null) {
            tail.next = head;
            if (!isSinglyLinked) {
                head.prev = tail;
            }
        }

        size++;
    }

    /**
     * Removes the first occurrence of the specified value from the list.
     * If the value is not found, the list remains unchanged.
     *
     * @param data The data to be removed from the list.
     */
    public void remove(T data) {
        // Case 1: Empty list
        if (head == null)
            return; // No element to remove

        // Case 2: Removing head node
        if (head.data == data) {
            // Handle head removal without iteration
            if (head.next == head || head.next == null) {
                clear(); // One item list case
                return;
            }

            head = head.next;
            if (isCircular) {
                tail.next = head;
                if (!isSinglyLinked) {
                    head.prev = tail;
                }
            } else {
                if (!isSinglyLinked) {
                    head.prev = null;
                }
            }
            size--;
            return;
        }

        // Case 3: Removing tail node
        if (tail.data == data) {
            // Only handle tail node removal, no iteration needed
            if (!isSinglyLinked) {
                tail.prev.next = isCircular ? head : null;
                if (isCircular) {
                    head.prev = tail.prev;
                }
            }
            tail = tail.prev; // Update tail
            size--;
            return;
        }

        // Case 4: Removing data from the middle (iterate to find the node)
        Node<T> current = head;
        while (current != null && current.next != null) {
            if (current.next.data == data) {
                current.next = current.next.next;
                if (!isSinglyLinked && current.next != null) {
                    current.next.prev = current;
                }
                size--;
                return;
            }
            current = current.next;
        }
    }

    /**
     * Removes the node at the specified index in the list.
     * If the index is out of bounds, an {@link IndexOutOfBoundsException} will be
     * thrown.
     *
     * @param index The index of the node to be removed.
     * @throws IndexOutOfBoundsException if the index is invalid (less than 0 or
     *                                   greater than or equal to the size of the
     *                                   list).
     */
    public void removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        // Case 1: Removing the head (index == 0)
        if (index == 0) {
            if (isCircular) {
                // Circular head removal
                tail.next = head.next;
                if (!isSinglyLinked) {
                    head.next.prev = tail;
                }
            }
            head = head.next; // Set head to the next node
            if (!isCircular && !isSinglyLinked) {
                head.prev = null; // Update head's prev in non-circular doubly linked list
            }
            size--;
            return;
        }

        // Case 2: Removing the tail (index == size - 1)
        else if (index == size - 1 && !isSinglyLinked) {
            if (isCircular) {
                // Circular tail removal
                tail.prev.next = head;
                if (!isSinglyLinked) {
                    head.prev = tail.prev;
                }
            }
            tail = tail.prev; // Move tail to the previous node
            if (!isCircular) {
                tail.next = null; // Update tail's next in non-circular doubly linked list
            }
            if (tail == null) { // If the list becomes empty, reset both head and tail to null
                head = null;
            }
            size--;
            return;
        }

        // Case 3: Removing a middle node (index > 0 && index < size - 1)
        Node<T> current;

        // If index is closer to the head, start from the head
        if (index < size / 2 || isSinglyLinked) {
            current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
        }
        // If index is closer to the tail, start from the tail
        else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }

        // Removing the node at index
        Node<T> nodeToRemove = current.next;
        current.next = nodeToRemove.next;

        if (nodeToRemove.next != null) {
            nodeToRemove.next.prev = current; // Update previous node's prev pointer (in doubly linked list)
        }

        // For circular list, ensure the tail's next points to head
        if (isCircular && nodeToRemove == tail) {
            tail = current; // Update tail if we're removing the tail node in circular list
        }

        size--;
        printList();
    }

    /**
     * Searches for the specified data in the list and returns the index of the
     * first occurrence.
     * If the data is not found, it returns -1.
     * The search is performed by iterating through the list until a match is found.
     * If the list is circular, the search stops once the loop reaches the head
     * again.
     *
     * @param data The data to search for in the list.
     * @return The index of the first occurrence of the data, or -1 if not found.
     */
    public int search(T data) {
        if (head == null)
            return -1; // Handle empty list

        Node<T> current = head;
        for (int i = 0; i < size; i++) {
            if (current.data == data) {
                return i; // Return the index of the first match
            }
            current = current.next;

            // If it's a circular list, break the loop when we return to the head
            if (isCircular && current == head) {
                break;
            }
        }
        return -1; // Return -1 if no match is found
    }

    /**
     * Returns the data at the specified index in the list.
     * If the index is out of bounds, an {@link IndexOutOfBoundsException} is
     * thrown.
     * In the case of a circular list, the index is adjusted to wrap around.
     * If the list is empty, an {@link IllegalStateException} is thrown.
     * If the index is closer to the head, the list is traversed starting from the
     * head,
     * otherwise, traversal begins from the tail. The traversal method depends on
     * whether
     * the list is singly or doubly linked.
     *
     * @param index The index of the element to retrieve.
     * @return The data at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     * @throws IllegalStateException     If the list is empty.
     */
    public T get(int index) {

        // Adjust for circular behavior before bounds checking
        if (isCircular) {
            index = index % size;
        }

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        if (head == null) {
            throw new IllegalStateException("List is empty");
        }

        if (index == size - 1)
            return tail.data;

        // If index is closer to the head (index < size / 2) or it's singly linked,
        // iterate from the head
        Node<T> current;
        if (index < size / 2 || isSinglyLinked) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else { // Otherwise, iterate from the tail
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }

        return current.data; // Return the data at the requested index
    }

    /**
     * Returns the number of nodes in the list.
     *
     * @return The size of the list, indicating the number of elements it contains.
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the list is empty.
     *
     * @return {@code true} if the list contains no elements, {@code false}
     *         otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the list, effectively clearing the list.
     * After calling this method, the list will be empty.
     */
    public void clear() {
        head = tail = null;
        size = 0;
    }

    // Utility methods
    /**
     * Checks if the specified value exists in the list.
     * 
     * @param data the value to search for in the list
     * @return true if the value is found, false otherwise
     */
    public boolean contains(T data) {
        return search(data) != -1;
    }

    /**
     * Converts the list to an array of the same type.
     * 
     * @return an array containing all the elements of the list
     * @throws ClassCastException if the type of the list's elements is incompatible
     *                            with the array
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] array = (T[]) new Object[size];
        Node<T> current = head;

        for (int i = 0; i < size; i++) {
            array[i] = current.data;
            current = current.next;
        }

        return array;
    }

    /**
     * Prints the elements of the list to the console.
     * 
     * If the list is empty, a message indicating so is printed. For circular lists,
     * the method
     * ensures that the list is not printed indefinitely.
     */
    public void printList() {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }

        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + ", ");
            current = current.next;
            if (current == head && isCircular)
                break; // Prevent infinite loop in circular lists
        }
        System.out.println();
    }

    /**
     * Reverses the list, adjusting the next and prev pointers.
     * 
     * This method is particularly useful for doubly linked lists, as it modifies
     * both
     * the next and prev pointers of each node. If the list is empty or has only one
     * element,
     * no reversal occurs. In circular lists, the tail will correctly point to the
     * head after reversal.
     */
    public void reverse() {
        if (head == null || head.next == null) {
            // If the list is empty or has only one element, no need to reverse
            return;
        }

        Node<T> current = head;
        Node<T> prev = null;
        Node<T> temp;

        // Swap next and prev pointers for every node
        do {
            temp = current.next; // Save next node
            current.next = prev; // Reverse next pointer
            if (!isSinglyLinked) {
                current.prev = temp; // Reverse prev pointer (only for doubly linked)
            }
            prev = current; // Move prev to current
            current = temp; // Move to next node
        } while (isCircular ? current != head : current != null);

        // After reversal, prev is the new head
        tail = head;
        head = prev;

        if (isCircular) {
            tail.next = head; // Ensure circular linking
            if (!isSinglyLinked) {
                head.prev = tail; // Maintain circular doubly linked property
            }
        } else {
            // Traverse from head to find the correct tail
            current = head;
            while (current.next != null) {
                current = current.next;
            }
            tail = current; // Set new tail
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node<T> currentNode;
        private Node<T> startNode; // Keeps track of the head of the list for circular check

        public LinkedListIterator() {
            this.currentNode = head;
            this.startNode = head;
        }

        @Override
        public boolean hasNext() {
            // If the list is circular, we need to check if we've looped back to the start
            // node
            return currentNode != null && (!isCircular || currentNode != startNode);
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            T data = currentNode.data;
            currentNode = currentNode.next; // Move to the next node
            return data;
        }
    }
}
