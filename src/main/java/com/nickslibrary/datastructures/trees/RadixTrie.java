package com.nickslibrary.datastructures.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.nickslibrary.utils.tree.RadixTrieNode;

/**
 * A Radix Trie (Compressed Trie) implementation.
 * This data structure optimizes standard Tries by merging common prefixes.
 * Merging and splitting logic will reside in RadixTrie, not RadixTrieNode.
 */
public class RadixTrie {

    // Root node of the Radix Trie
    private RadixTrieNode root;

    /**
     * Constructs an empty Radix Trie.
     */
    public RadixTrie() {
        root = new RadixTrieNode();
    }

    /**
     * Adds a word to the Radix Trie.
     *
     * @param word The word to add.
     * @return true if the word was successfully added, false if it already existed.
     */
    public boolean add(String word) {
        if (word == null || word.isEmpty()) {
            return false; // Cannot add null or empty words
        }

        RadixTrieNode current = root;
        int index = 0;

        while (index < word.length()) {
            char ch = word.charAt(index);

            if (!current.hasChild(ch)) {
                // No matching child, create a new node using addChild
                current.addChild(ch, word.substring(index));
                current = current.getChild(ch);
                break; // New word is fully inserted
            }

            RadixTrieNode child = current.getChild(ch);
            String childPrefix = child.getPrefix();
            int commonLength = getCommonPrefixLength(word.substring(index), childPrefix);

            if (commonLength == childPrefix.length()) {
                // Full match with existing node, move deeper
                current = child;
                index += commonLength;
            } else {
                // Split required
                String remainingChildPrefix = childPrefix.substring(commonLength);
                String newWordSuffix = word.substring(index + commonLength);

                // Remove old child
                current.removeChild(ch);

                // Add new intermediate node for the common prefix
                current.addChild(ch, childPrefix.substring(0, commonLength));
                RadixTrieNode splitNode = current.getChild(ch);

                // Move the existing child under the splitNode
                splitNode.addChild(remainingChildPrefix.charAt(0), remainingChildPrefix);
                RadixTrieNode newChild = splitNode.getChild(remainingChildPrefix.charAt(0));

                // Transfer metadata to the new child
                newChild.setEndOfWord(child.isEndOfWord());
                newChild.setFrequency(child.getFrequency());

                // Move old children to the new child
                for (Map.Entry<Character, RadixTrieNode> entry : child.getChildren().entrySet()) {
                    newChild.getChildren().put(entry.getKey(), entry.getValue());
                    entry.getValue().setParent(newChild);
                }

                // If there's more of the word left, add it as a new child
                if (!newWordSuffix.isEmpty()) {
                    splitNode.addChild(newWordSuffix.charAt(0), newWordSuffix);
                    current = splitNode.getChild(newWordSuffix.charAt(0));
                } else {
                    current = splitNode;
                }

                break;
            }
        }

        // Ensure the last node is marked as an end-of-word node
        if (current.isEndOfWord()) {
            current.incrementFrequency();
            return false; // Word already exists
        }

        current.setEndOfWord(true);
        current.setFrequency(1);
        return true;
    }

    /**
     * Computes the length of the common prefix shared by two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The length of the common prefix between s1 and s2.
     */
    private int getCommonPrefixLength(String s1, String s2) {
        int minLength = Math.min(s1.length(), s2.length());
        int i = 0;
        while (i < minLength && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }
        return i;
    }

    /**
     * Deletes a word from the Radix Trie.
     *
     * @param word The word to delete.
     * @return true if the word was successfully deleted, false if the word was not
     *         found.
     */
    /**
     * Deletes a word from the Radix Trie.
     *
     * @param word The word to delete.
     * @return true if the word was successfully deleted, false if the word was not
     *         found.
     */
    public boolean delete(String word) {
        if (word == null || word.isEmpty()) {
            return false; // Invalid input
        }

        RadixTrieNode current = root;
        Stack<RadixTrieNode> nodeStack = new Stack<>();
        Stack<Character> charStack = new Stack<>();
        int index = 0;

        // Step 1: Traverse the trie to find the node representing the word
        while (index < word.length()) {
            boolean found = false;

            for (Map.Entry<Character, RadixTrieNode> entry : current.getChildren().entrySet()) {
                RadixTrieNode child = entry.getValue();
                String prefix = child.getPrefix();
                int commonLength = getCommonPrefixLength(word.substring(index), prefix);

                if (commonLength == prefix.length()) { // Full prefix match
                    nodeStack.push(current);
                    charStack.push(entry.getKey());
                    current = child;
                    index += commonLength;
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false; // Mismatch found, word does not exist
            }
        }

        if (!current.isEndOfWord()) {
            return false;
        }

        // Step 2: Unmark the node as an end-of-word
        current.setEndOfWord(false);
        current.setFrequency(0);

        // Step 3: Cleanup orphan nodes (only if it has no children)
        while (!nodeStack.isEmpty() && current.getChildren().isEmpty() && !current.isEndOfWord()) {
            RadixTrieNode parent = nodeStack.pop();
            char parentChar = charStack.pop();
            parent.removeChild(parentChar);
            current = parent;
        }

        // Step 4: Merge nodes if the parent has a single child and is not an
        // end-of-word
        if (!current.isEndOfWord() && current.getChildren().size() == 1) {
            RadixTrieNode onlyChild = current.getChildren().values().iterator().next();

            // Merge condition: Current node should not be the root
            if (nodeStack.isEmpty() && current == root) {
            } else {
                current.setPrefix(current.getPrefix() + onlyChild.getPrefix());

                // Move onlyChild's children to current
                current.getChildren().clear();
                for (Map.Entry<Character, RadixTrieNode> entry : onlyChild.getChildren().entrySet()) {
                    current.getChildren().put(entry.getKey(), entry.getValue());
                    entry.getValue().setParent(current);
                }

                // Copy metadata
                current.setEndOfWord(onlyChild.isEndOfWord());
                current.setFrequency(onlyChild.getFrequency());
            }
        }

        return true;
    }

    /**
     * Checks whether the given word exists in the Radix Trie.
     *
     * @param word the word to search for
     * @return true if the word exists, false otherwise
     */
    public boolean search(String word) {
        if (word == null || word.isEmpty()) {
            return false; // Invalid input
        }

        RadixTrieNode current = root;
        int index = 0;

        // Traverse the trie following the prefixes
        while (index < word.length()) {
            boolean found = false;

            for (RadixTrieNode child : current.getChildren().values()) {
                String prefix = child.getPrefix();
                int commonLength = getCommonPrefixLength(word.substring(index), prefix);

                if (commonLength == prefix.length()) { // Full prefix match
                    current = child;
                    index += commonLength;
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false; // Word is not in the trie
            }
        }

        return current.isEndOfWord();
    }

    /**
     * Checks whether any words in the Radix Trie start with the given prefix.
     *
     * @param prefix the prefix to check
     * @return true if at least one word starts with the prefix, false otherwise
     */
    public boolean hasPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return false;
        }

        RadixTrieNode current = root;
        int index = 0;

        while (index < prefix.length()) {
            boolean found = false;

            for (Map.Entry<Character, RadixTrieNode> entry : current.getChildren().entrySet()) {
                RadixTrieNode child = entry.getValue();
                String nodePrefix = child.getPrefix();
                int commonLength = getCommonPrefixLength(prefix.substring(index), nodePrefix);

                if (commonLength > 0) { // There's a match
                    if (commonLength >= prefix.length() - index) {
                        return true; // Found a node that matches the prefix
                    }
                    current = child;
                    index += commonLength;
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false; // Prefix not found
            }
        }

        return true;
    }

    /**
     * Returns the number of words stored in the Radix Trie.
     *
     * @return the word count
     */
    public int wordCount() {
        return countWords(root);
    }

    /**
     * Recursively counts the number of words in the trie.
     */
    private int countWords(RadixTrieNode node) {
        int count = node.isEndOfWord() ? 1 : 0;
        for (RadixTrieNode child : node.getChildren().values()) {
            count += countWords(child);
        }
        return count;
    }

    /**
     * Returns all words stored in the Radix Trie.
     *
     * @return a list of all words
     */
    public List<String> getAllWords() {
        List<String> words = new ArrayList<>();
        collectWords(root, "", words);
        return words;
    }

    /**
     * Recursively collects all words from the given node.
     *
     * @param node   the current node
     * @param prefix the accumulated prefix
     * @param words  the list to store words
     */
    private void collectWords(RadixTrieNode node, String prefix, List<String> words) {
        if (node.isEndOfWord()) {
            words.add(prefix);
        }

        for (RadixTrieNode child : node.getChildren().values()) {
            collectWords(child, prefix + child.getPrefix(), words);
        }
    }

    /**
     * Prints the structure of the Radix Trie in a readable ASCII format.
     */
    public void printTrie() {
        printTrieHelper(root, "", true);
    }

    /**
     * Recursive helper method to print the Radix Trie.
     *
     * @param node   The current node being printed.
     * @param indent The indentation for the current level.
     * @param isLast Whether this node is the last child of its parent.
     */
    private void printTrieHelper(RadixTrieNode node, String indent, boolean isLast) {
        if (node == null) {
            return;
        }

        System.out.print(indent);
        if (isLast) {
            System.out.print("└── ");
            indent += "    ";
        } else {
            System.out.print("├── ");
            indent += "│   ";
        }

        System.out.println("'" + node.getPrefix() + "'" + (node.isEndOfWord() ? " (End)" : ""));

        int childCount = node.getChildren().size();
        int i = 0;
        for (RadixTrieNode child : node.getChildren().values()) {
            printTrieHelper(child, indent, i == childCount - 1);
            i++;
        }
    }

}
