package com.nickslibrary.utils.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a node in a Radix Trie, storing compressed prefixes.
 */
public class RadixTrieNode {
    private String prefix; // The shared prefix for this node
    private final Map<Character, RadixTrieNode> children; // Child nodes
    private boolean isEndOfWord; // Whether this node marks the end of a word
    private int frequency; // Number of times the word has been inserted
    private RadixTrieNode parent; // Parent node for backtracking

    /**
     * Constructs an empty RadixTrieNode.
     */
    public RadixTrieNode() {
        this.prefix = "";
        this.children = new HashMap<>();
        this.isEndOfWord = false;
        this.frequency = 0;
        this.parent = null;
    }

    /**
     * Constructs a RadixTrieNode with a given prefix.
     *
     * @param prefix The prefix stored in this node.
     */
    public RadixTrieNode(String prefix) {
        this.prefix = prefix;
        this.children = new HashMap<>();
        this.isEndOfWord = false;
        this.frequency = 0;
        this.parent = null;
    }

    /**
     * Checks if this node has a child that starts with a given character.
     *
     * @param ch The starting character to check.
     * @return true if a child exists with this character, false otherwise.
     */
    public boolean hasChild(char ch) {
        return children.containsKey(ch);
    }

    /**
     * Retrieves the child node associated with the given character.
     *
     * @param ch The character key.
     * @return The corresponding child RadixTrieNode, or null if not present.
     */
    public RadixTrieNode getChild(char ch) {
        return children.get(ch);
    }

    /**
     * Adds a child node with a given prefix.
     *
     * @param ch     The character key.
     * @param prefix The prefix string stored in the new node.
     */
    public void addChild(char ch, String prefix) {
        if (!children.containsKey(ch)) {
            RadixTrieNode child = new RadixTrieNode(prefix);
            child.setParent(this);
            children.put(ch, child);
        }
    }

    /**
     * Removes the child node associated with the given character.
     *
     * @param ch The character key.
     */
    public void removeChild(char ch) {
        children.remove(ch);
    }

    /**
     * Checks if this node is a leaf (has no children).
     *
     * @return true if the node has no children, false otherwise.
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    // Getters and Setters

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Map<Character, RadixTrieNode> getChildren() {
        return children;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean isEndOfWord) {
        this.isEndOfWord = isEndOfWord;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

    public RadixTrieNode getParent() {
        return parent;
    }

    public void setParent(RadixTrieNode parent) {
        this.parent = parent;
    }
}
