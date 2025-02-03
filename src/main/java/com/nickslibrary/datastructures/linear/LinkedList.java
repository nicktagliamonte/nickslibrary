package com.nickslibrary.datastructures.linear;

public class LinkedList<T> {
    // Private inner class for list node
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    // Fields
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private boolean isCircular;
    private boolean isSinglyLinked;

    // Constructor
    public LinkedList(boolean isCircular, boolean isSinglyLinked) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.isCircular = isCircular;
        this.isSinglyLinked = isSinglyLinked;
    }

    // Standard Methods
    // Add a node to the list
    public void add(T data) {
        Node<T> newNode = new Node<>(data);

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

            if (isCircular) {
                tail.next = head;
                if (!isSinglyLinked) {
                    head.prev = tail;
                }
            }
        }

        size++;
    }

    // Insert a node at a specific position
    public void insert(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        Node<T> newNode = new Node<>(data);

        if (index == 0) {
            newNode.next = head;
            if (head != null) {
                head.prev = isSinglyLinked ? null : newNode;
            }
            head = newNode;

            if (isCircular) {
                if (tail == null) { // Empty list case
                    tail = newNode;
                    head.next = head;
                    if (!isSinglyLinked)
                        head.prev = head;
                } else {
                    tail.next = head;
                    if (!isSinglyLinked)
                        head.prev = tail;
                }
            }

            if (tail == null)
                tail = head; // First element case
        } else if (index == size) {
            add(data);
            return;
        } else {
            Node<T> current;
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

    // Remove the first occurrence of the specified value
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

    // Remove a node at the specified index
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
        if (index == size - 1) {
            if (isCircular) {
                // Circular tail removal
                tail.prev.next = head;
                if (!isSinglyLinked) {
                    head.prev = tail.prev;
                }
            }
            tail = tail.prev; // Move tail to the previous node
            if (!isCircular && !isSinglyLinked) {
                tail.next = null; // Update tail's next in non-circular doubly linked list
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
    }

    // Search the array for some data and return the index of the first occurrence
    // of that data
    public int search(T data) {
        if (head == null) return -1;  // Handle empty list
    
        Node<T> current = head;
        for (int i = 0; i < size; i++) {
            if (current.data == data) {
                return i;  // Return the index of the first match
            }
            current = current.next;
            
            // If it's a circular list, break the loop when we return to the head
            if (isCircular && current == head) {
                break;
            }
        }
        return -1;  // Return -1 if no match is found
    }    

    // Return the data at the specified index
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }

        if (index == size - 1) return tail.data;
    
        // If index is closer to the head (index < size / 2) or it's singly linked, iterate from the head
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
    
        return current.data;  // Return the data at the requested index
    }    

    
    // Return the number of nodes in the list
    public int size() {
        return size;
    }

    // Check if the list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Remove all elements from the list
    public void clear() {
        head = tail = null;
        size = 0;
    }

    // Utility methods
    // Return true if the value is in the list
    public boolean contains(T data) {
        // Return true if the value is in the list
        return search(data) != -1;
    }

    // Convert the list to an array
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

    // Print the list elements
    public void printList() {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }
    
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + ", ");
            current = current.next;
            if (current == head && isCircular) break;  // Prevent infinite loop in circular lists
        }
        System.out.println();
    }    

    // Reverse the list (particularly useful for doubly linked mode)
    public void reverse() {
        if (head == null || head.next == null) {
            // If the list is empty or has only one element, no need to reverse
            return;
        }
    
        Node<T> current = head;
        Node<T> temp = null;
        Node<T> prev = null;
    
        // If doubly linked, need to handle prev and next for each node
        while (current != null) {
            temp = current.next;  // Save the next node
            current.next = prev;  // Reverse the next pointer
            if (!isSinglyLinked) {
                current.prev = temp;  // Reverse the prev pointer (only for doubly linked list)
            }
            prev = current;  // Move prev to the current node
            current = temp;  // Move to the next node
            if (current == head && isCircular) break;  // Prevent infinite loop in circular lists
        }
    
        // After loop, prev will be the new head, so set the head and tail accordingly
        head = prev;
        if (isCircular) {
            tail.next = head;  // In circular mode, make sure the tail points to the head
            if (!isSinglyLinked) {
                head.prev = tail;  // Maintain the circular doubly linked structure
            }
        } else {
            tail = head;  // Tail becomes head after reversing for a non-circular list
        }
    }    
}
