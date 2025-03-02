package com.nickslibrary.utils.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a node in a Trie structure, supporting arbitrary Unicode characters.
 */
public class TrieNode {
    private final Map<Character, TrieNode> children;
    private TrieNode parent;
    private boolean isEndOfWord;
    private int frequency;

    /**
     * Constructs a new TrieNode.
     */
    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndOfWord = false;
        this.frequency = 0;
        this.parent = null;
    }

    /**
     * Checks if this node has a child for the given character.
     *
     * @param ch the character to check
     * @return true if a child exists for the character, false otherwise
     */
    public boolean hasChild(char ch) {
        return children.containsKey(ch);
    }

    /**
     * Retrieves the child node associated with the given character.
     *
     * @param ch the character key
     * @return the corresponding child TrieNode, or null if not present
     */
    public TrieNode getChild(char ch) {
        return children.get(ch);
    }

    /**
     * Adds a child node for the given character if it doesn't already exist.
     *
     * @param ch the character key
     */
    public void addChild(char ch) {
        children.putIfAbsent(ch, new TrieNode());
        children.get(ch).parent = this;
    }

    /**
     * Removes the child node associated with the given character.
     *
     * @param ch the character key
     */
    public void removeChild(char ch) {
        children.remove(ch);
    }

    /**
     * Checks if this node is a leaf (has no children).
     *
     * @return true if the node has no children, false otherwise
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    // Getters and Setters

    public Map<Character, TrieNode> getChildren() {
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

    public TrieNode getParent() {
        return parent;
    }

    public void setParent(TrieNode parent) {
        this.parent = parent;
    }
}
