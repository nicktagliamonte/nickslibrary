package com.nickslibrary.datastructures.advanced;

import java.util.HashMap;
import java.util.Map;

/**
 * A Suffix Tree is a data structure that represents the suffixes of a given
 * string.
 * It allows for efficient substring search and other string-related operations.
 */
public class SuffixTree {

    private Node root;

    /**
     * Constructs a Suffix Tree from the given string.
     *
     * @param text The string to construct the Suffix Tree from.
     */
    public SuffixTree(String text) {
        root = new Node();
        buildSuffixTree(text);
    }

    /**
     * Builds the Suffix Tree from the given text.
     *
     * @param text The string to construct the Suffix Tree from.
     */
    private void buildSuffixTree(String text) {
        for (int i = 0; i < text.length(); i++) {
            addSuffix(text.substring(i));
        }
    }

    private void addSuffix(String suffix) {
        Node currentNode = root;
        for (char c : suffix.toCharArray()) {
            if (!currentNode.children.containsKey(c)) {
                currentNode.children.put(c, new Node(c, currentNode));
            }
            currentNode = currentNode.children.get(c);
        }
        System.out.println("Added suffix: " + suffix);
    }

    /**
     * Searches for a substring in the Suffix Tree.
     *
     * @param substring The substring to search for.
     * @return True if the substring is found, false otherwise.
     */
    public boolean search(String substring) {
        Node currentNode = root;
        for (char c : substring.toCharArray()) {
            currentNode = currentNode.children.get(c);
            if (currentNode == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the longest repeated substring in the Suffix Tree.
     *
     * @return The longest repeated substring.
     */
    public String longestRepeatedSubstring() {
        return findLongestRepeatedSubstring(root, "");
    }

    /**
     * Recursively finds the longest repeated substring.
     * 
     * @param node   The current node in the tree.
     * @param prefix The accumulated substring so far.
     * @return The longest repeated substring.
     */
    private String findLongestRepeatedSubstring(Node node, String prefix) {
        System.out.println("Checking prefix: " + prefix);

        String longestSubstring = "";
        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            char ch = entry.getKey();
            Node child = entry.getValue();
            String candidate = findLongestRepeatedSubstring(child, prefix + ch);

            if (candidate.length() > longestSubstring.length()) {
                longestSubstring = candidate;
            }
        }

        if (node.children.size() > 1 && prefix.length() > longestSubstring.length()) {
            longestSubstring = prefix;
        }

        System.out.println("Longest substring at node " + prefix + ": " + longestSubstring);
        return longestSubstring;
    }

    /**
     * Helper method to get the substring represented by a node at a given depth.
     *
     * @param node  The node in the Suffix Tree.
     * @param depth The depth of the node in the Suffix Tree.
     * @return The substring represented by the node.
     */
    private String getSubstringFromNode(Node node, int depth) {
        StringBuilder sb = new StringBuilder();
        Node currentNode = node;
        while (currentNode != root) {
            sb.insert(0, currentNode.character);
            currentNode = currentNode.parent;
        }
        String substring = sb.toString();
        System.out.println("Substring from node: " + substring + " at depth " + depth);
        return substring;
    }

    /**
     * Returns the longest common substring between two strings using the Suffix
     * Tree.
     *
     * @param text1 The first string.
     * @param text2 The second string.
     * @return The longest common substring.
     */
    public String longestCommonSubstring(String text1, String text2) {
        SuffixTree suffixTree1 = new SuffixTree(text1 + "#" + text2 + "$");
        return suffixTree1.findLongestCommonSubstring(suffixTree1.root, text1, text2, 0);
    }

    /**
     * Helper method to find the longest common substring between two strings using
     * the Suffix Tree.
     *
     * @param node  The current node in the Suffix Tree.
     * @param text1 The first string.
     * @param text2 The second string.
     * @param depth The current depth in the Suffix Tree.
     * @return The longest common substring.
     */
    private String findLongestCommonSubstring(Node node, String text1, String text2, int depth) {
        String longestSubstring = "";
        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            Node child = entry.getValue();
            String candidate = findLongestCommonSubstring(child, text1, text2, depth + 1);
            if (candidate.length() > longestSubstring.length()) {
                longestSubstring = candidate;
            }
        }
        if (node.children.size() > 1) {
            String currentSubstring = getSubstringFromNode(node, depth);
            if (currentSubstring.length() > longestSubstring.length() &&
                    currentSubstring.contains("#") && currentSubstring.contains("$")) {
                longestSubstring = currentSubstring;
            }
        }
        return longestSubstring;
    }

    /**
     * Prints the Suffix Tree.
     */
    public void printTree() {
        printNode(root, 0);
    }

    /**
     * Helper method to print the details of a node in the Suffix Tree.
     *
     * @param node  The current node in the Suffix Tree.
     * @param depth The current depth in the Suffix Tree.
     */
    private void printNode(Node node, int depth) {
        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            Node child = entry.getValue();
            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }
            System.out.println(child.character);
            printNode(child, depth + 1);
        }
    }

    /**
     * Node class for the Suffix Tree.
     */
    private static class Node {
        private Map<Character, Node> children;
        private char character;
        private Node parent;

        public Node() {
            children = new HashMap<>();
        }

        public Node(char character, Node parent) {
            this();
            this.character = character;
            this.parent = parent;
        }
    }
}