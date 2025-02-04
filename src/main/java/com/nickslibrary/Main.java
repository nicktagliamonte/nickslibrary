package com.nickslibrary;

import com.nickslibrary.datastructures.linear.*;

/**
 *
 */
public class Main {
    public static void main(String[] args) {
        LinkedList<Integer> linkedList = new LinkedList<>(false, true);
        linkedList.add(10);
        linkedList.add(20);
        linkedList.add(30);
        linkedList.add(40);
        linkedList.removeAt(3);
    }
}
