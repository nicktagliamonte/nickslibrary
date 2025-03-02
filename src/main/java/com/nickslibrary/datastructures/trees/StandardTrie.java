package com.nickslibrary.datastructures.trees;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.google.gson.Gson;
import com.nickslibrary.utils.tree.TrieNode;

/**
 * A standard Trie (prefix tree) implementation for storing and searching words
 * efficiently.
 */
public class StandardTrie {
    private final TrieNode root;

    /**
     * Constructs an empty Trie.
     */
    public StandardTrie() {
        this.root = new TrieNode();
    }

    /**
     * Inserts a word into the Trie.
     *
     * @param word the word to insert
     */
    public void insert(String word) {
        if (word == null || word.isEmpty()) {
            return; // Ignore null or empty strings
        }

        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            if (!current.hasChild(ch)) {
                current.addChild(ch);
            }
            current = current.getChild(ch);
        }

        if (!current.isEndOfWord()) {
            current.setEndOfWord(true);
            current.setFrequency(1);
        } else {
            current.incrementFrequency();
        }
    }

    /**
     * Checks if a word exists in the Trie.
     *
     * @param word the word to search for
     * @return true if the word exists, false otherwise
     */
    public boolean search(String word) {
        if (word == null || word.isEmpty()) {
            return false; // Null or empty words do not exist
        }

        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            if (!current.hasChild(ch)) {
                return false; // Character path does not exist
            }
            current = current.getChild(ch);
        }

        return current.isEndOfWord(); // Word exists if it's marked as an end
    }

    /**
     * Checks if there is any word in the Trie that starts with the given prefix.
     *
     * @param prefix the prefix to check
     * @return true if any word starts with the prefix, false otherwise
     */
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return false; // Null or empty prefixes are invalid
        }

        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            if (!current.hasChild(ch)) {
                return false; // Prefix path does not exist
            }
            current = current.getChild(ch);
        }

        return true; // If we successfully traverse the prefix, words exist with this prefix
    }

    /**
     * Deletes a word from the Trie.
     *
     * @param word the word to delete
     * @return true if the word was deleted, false if it was not found
     */
    public boolean delete(String word) {
        if (word == null || word.isEmpty()) {
            return false; // Cannot delete null or empty words
        }
        return deleteHelper(root, word, 0);
    }

    private boolean deleteHelper(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord()) {
                return false; // Word not found
            }
            current.setEndOfWord(false);
            current.setFrequency(0); // Reset frequency since word is removed
            return true; // Indicate that deletion happened
        }

        char ch = word.charAt(index);
        if (!current.hasChild(ch)) {
            return false; // Word not found
        }

        TrieNode nextNode = current.getChild(ch);
        boolean deleted = deleteHelper(nextNode, word, index + 1);

        if (deleted && !nextNode.isEndOfWord() && nextNode.getChildren().isEmpty()) {
            current.removeChild(ch); // Prune empty nodes
        }

        return deleted; // Ensure the function returns true if deletion occurred
    }

    /**
     * Gets the frequency of a word in the Trie.
     *
     * @param word the word whose frequency is to be retrieved
     * @return the frequency of the word, or 0 if it is not found
     */
    public int getFrequency(String word) {
        if (word == null || word.isEmpty()) {
            return 0; // Null or empty words have no frequency
        }

        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            if (!current.hasChild(ch)) {
                return 0; // Word not found
            }
            current = current.getChild(ch);
        }

        return current.isEndOfWord() ? current.getFrequency() : 0;
    }

    /**
     * Clears the entire Trie.
     */
    public void clear() {
        root.getChildren().clear(); // Remove all children
        root.setEndOfWord(false); // Reset end-of-word flag
        root.setFrequency(0); // Reset frequency
    }

    /**
     * Checks if the Trie is empty.
     *
     * @return true if the Trie is empty, false otherwise
     */
    public boolean isEmpty() {
        return root.getChildren().isEmpty() && !root.isEndOfWord();
    }

    /**
     * Returns the number of words stored in the Trie.
     *
     * @return the total word count in the Trie
     */
    public int size() {
        return countWords(root);
    }

    private int countWords(TrieNode node) {
        if (node == null) {
            return 0;
        }

        int count = node.isEndOfWord() ? 1 : 0;

        for (TrieNode child : node.getChildren().values()) {
            count += countWords(child);
        }

        return count;
    }

    /**
     * Sets the frequency of a word in the Trie.
     *
     * @param word      the word whose frequency is to be set
     * @param frequency the new frequency value (must be >= 0)
     * @return true if the frequency was updated, false if the word does not exist
     */
    public boolean setFrequency(String word, int frequency) {
        if (word == null || word.isEmpty() || frequency < 0) {
            return false; // Invalid input
        }

        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            if (!current.hasChild(ch)) {
                return false; // Word not found
            }
            current = current.getChild(ch);
        }

        if (!current.isEndOfWord()) {
            return false; // Word does not exist as a complete word
        }

        current.setFrequency(frequency);
        return true;
    }

    /**
     * Increments the frequency of a word in the Trie.
     *
     * @param word the word whose frequency is to be incremented
     * @return true if the frequency was incremented, false if the word does not
     *         exist
     */
    public boolean incrementFrequency(String word) {
        if (word == null || word.isEmpty()) {
            return false; // Invalid input
        }

        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            if (!current.hasChild(ch)) {
                return false; // Word not found
            }
            current = current.getChild(ch);
        }

        if (!current.isEndOfWord()) {
            return false; // Word does not exist as a complete word
        }

        current.incrementFrequency();
        return true;
    }

    /**
     * Returns the number of distinct words stored in the Trie.
     *
     * @return the total count of distinct words in the Trie
     */
    public int wordCount() {
        return countDistinctWords(root);
    }

    private int countDistinctWords(TrieNode node) {
        if (node == null) {
            return 0;
        }

        int count = node.isEndOfWord() ? 1 : 0;

        for (TrieNode child : node.getChildren().values()) {
            count += countDistinctWords(child);
        }

        return count;
    }

    /**
     * Returns the total number of word instances stored in the Trie,
     * considering word frequencies.
     *
     * @return the total count of all word occurrences in the Trie
     */
    public int totalWordInstances() {
        return countTotalWordInstances(root);
    }

    private int countTotalWordInstances(TrieNode node) {
        if (node == null) {
            return 0;
        }

        int count = node.isEndOfWord() ? node.getFrequency() : 0;

        for (TrieNode child : node.getChildren().values()) {
            count += countTotalWordInstances(child);
        }

        return count;
    }

    /**
     * Returns the depth of the Trie, defined as the length of the longest word.
     *
     * @return the maximum depth of the Trie
     */
    public int depth() {
        return calculateDepth(root);
    }

    private int calculateDepth(TrieNode node) {
        if (node == null || node.getChildren().isEmpty()) {
            return 0;
        }

        int maxDepth = 0;
        for (TrieNode child : node.getChildren().values()) {
            maxDepth = Math.max(maxDepth, calculateDepth(child));
        }

        return maxDepth + 1;
    }

    /**
     * Returns the total number of nodes in the Trie.
     *
     * @return the number of nodes in the Trie
     */
    public int nodeCount() {
        return countNodes(root);
    }

    private int countNodes(TrieNode node) {
        if (node == null) {
            return 0;
        }

        int count = 1; // Count the current node

        for (TrieNode child : node.getChildren().values()) {
            count += countNodes(child); // Recursively count child nodes
        }

        return count;
    }

    /**
     * Returns the total number of characters stored in the Trie.
     *
     * @return the total character count in the Trie
     */
    public int characterCount() {
        return countCharacters(root);
    }

    private int countCharacters(TrieNode node) {
        if (node == null) {
            return 0;
        }

        int count = node.getChildren().size(); // Count direct child characters

        for (TrieNode child : node.getChildren().values()) {
            count += countCharacters(child); // Recursively count characters in children
        }

        return count;
    }

    /**
     * Retrieves all words in the Trie that start with the given prefix.
     *
     * @param prefix the prefix to search for
     * @return a list of words that start with the given prefix
     */
    public List<String> getWordsWithPrefix(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode current = root;

        // Traverse the Trie to find the node corresponding to the last character of the
        // prefix
        for (char ch : prefix.toCharArray()) {
            if (!current.hasChild(ch)) {
                return results; // Prefix not found, return empty list
            }
            current = current.getChild(ch);
        }

        // Collect words starting from the found node
        collectWords(current, prefix, results);
        return results;
    }

    /**
     * Recursively collects words from the given node.
     *
     * @param node    the current Trie node
     * @param prefix  the prefix built so far
     * @param results the list to store words
     */
    private void collectWords(TrieNode node, String prefix, List<String> results) {
        if (node.isEndOfWord()) {
            results.add(prefix); // Found a word
        }

        for (char ch : node.getChildren().keySet()) {
            collectWords(node.getChild(ch), prefix + ch, results);
        }
    }

    /**
     * Suggests a list of words based on the given prefix.
     * This is useful for autocomplete functionality.
     *
     * @param prefix the prefix to search for suggestions
     * @param limit  the maximum number of suggestions to return
     * @return a list of suggested words, up to the specified limit
     */
    public List<String> suggest(String prefix, int limit) {
        List<String> suggestions = new ArrayList<>();
        TrieNode current = root;

        // Traverse the Trie to find the node corresponding to the last character of the
        // prefix
        for (char ch : prefix.toCharArray()) {
            if (!current.hasChild(ch)) {
                return suggestions; // Prefix not found, return empty list
            }
            current = current.getChild(ch);
        }

        // Collect suggestions from the prefix node
        collectSuggestions(current, prefix, suggestions, limit);
        return suggestions;
    }

    /**
     * Recursively collects word suggestions from the given node.
     *
     * @param node        the current Trie node
     * @param prefix      the prefix built so far
     * @param suggestions the list to store suggestions
     * @param limit       the maximum number of suggestions to collect
     */
    private void collectSuggestions(TrieNode node, String prefix, List<String> suggestions, int limit) {
        if (suggestions.size() >= limit) {
            return; // Stop if we have enough suggestions
        }

        if (node.isEndOfWord()) {
            suggestions.add(prefix);
        }

        for (char ch : node.getChildren().keySet()) {
            collectSuggestions(node.getChild(ch), prefix + ch, suggestions, limit);
            if (suggestions.size() >= limit) {
                break; // Stop early if limit is reached
            }
        }
    }

    /**
     * Finds the longest prefix of the given word that exists in the Trie.
     *
     * @param word the word to check
     * @return the longest matching prefix found in the Trie
     */
    public String longestPrefixMatch(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        TrieNode current = root;
        StringBuilder longestPrefix = new StringBuilder();

        for (char ch : word.toCharArray()) {
            if (!current.hasChild(ch)) {
                break; // Stop if the prefix path breaks
            }
            current = current.getChild(ch);
            longestPrefix.append(ch);
        }

        return longestPrefix.toString();
    }

    /**
     * Finds the shortest prefix of the given word that exists as a complete word in
     * the Trie.
     *
     * @param word the word to check
     * @return the shortest matching prefix that forms a complete word, or an empty
     *         string if none exists
     */
    public String shortestPrefixMatch(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        TrieNode current = root;
        StringBuilder prefix = new StringBuilder();

        for (char ch : word.toCharArray()) {
            if (!current.hasChild(ch)) {
                return ""; // No prefix exists in the Trie
            }
            current = current.getChild(ch);
            prefix.append(ch);

            if (current.isEndOfWord()) {
                return prefix.toString(); // Return the first complete word found
            }
        }

        return ""; // No complete word found as a prefix
    }

    /**
     * Checks if there is any word in the Trie that starts with the given prefix.
     *
     * @param prefix the prefix to check
     * @return true if any word starts with the prefix, false otherwise
     */
    public boolean hasPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return false;
        }

        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            if (!current.hasChild(ch)) {
                return false; // Prefix path does not exist
            }
            current = current.getChild(ch);
        }

        return true; // Successfully traversed the prefix, meaning words exist with this prefix
    }

    /**
     * Retrieves all words stored in the Trie.
     *
     * @return a list of all words in the Trie
     */
    public List<String> getAllWords() {
        List<String> words = new ArrayList<>();
        collectWords(root, new StringBuilder(), words);
        return words;
    }

    private void collectWords(TrieNode node, StringBuilder prefix, List<String> words) {
        if (node.isEndOfWord()) {
            words.add(prefix.toString());
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            prefix.append(entry.getKey());
            collectWords(entry.getValue(), prefix, words);
            prefix.deleteCharAt(prefix.length() - 1); // Backtrack
        }
    }

    /**
     * Retrieves all words stored in the Trie in case-sensitive alphabetic order
     * (i.e. Apple, Zebra, banana).
     *
     * @return a sorted list of all words in the Trie
     */
    public List<String> getAllWordsSorted() {
        List<String> words = new ArrayList<>();
        collectWords(root, new StringBuilder(), words);
        Collections.sort(words);
        return words;
    }

    /**
     * Retrieves all words stored in the Trie, sorted by their frequency in
     * descending order.
     *
     * @return a list of words sorted by frequency (most frequent words first)
     */
    public List<String> getAllWordsByFrequency() {
        List<Map.Entry<String, Integer>> wordsWithFrequency = new ArrayList<>();
        collectWordsByFrequency(root, "", wordsWithFrequency);

        // Sort words by frequency in descending order
        wordsWithFrequency.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        // Extract only the words from sorted entries
        List<String> sortedWords = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordsWithFrequency) {
            sortedWords.add(entry.getKey());
        }

        return sortedWords;
    }

    private void collectWordsByFrequency(TrieNode node, String prefix,
            List<Map.Entry<String, Integer>> wordsWithFrequency) {
        if (node.isEndOfWord()) {
            wordsWithFrequency.add(new AbstractMap.SimpleEntry<>(prefix, node.getFrequency()));
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            collectWordsByFrequency(entry.getValue(), prefix + entry.getKey(), wordsWithFrequency);
        }
    }

    /**
     * Prints the Trie in a structured ASCII format.
     */
    public void printTrie() {
        printTrieHelper(root, "", true);
    }

    /**
     * Recursively prints the Trie structure in an ASCII format.
     *
     * @param node   the current TrieNode being processed
     * @param prefix indentation to represent depth
     * @param isLast whether this node is the last child of its parent
     */
    private void printTrieHelper(TrieNode node, String prefix, boolean isLast) {
        if (node != root) {
            System.out.println(prefix + (isLast ? "└── " : "├── ") + (node.isEndOfWord() ? "[" : "")
                    + getCharFromParent(node) + (node.isEndOfWord() ? "]" : ""));
            prefix += isLast ? "    " : "│   ";
        }

        List<Character> childrenKeys = new ArrayList<>(node.getChildren().keySet());
        for (int i = 0; i < childrenKeys.size(); i++) {
            char ch = childrenKeys.get(i);
            printTrieHelper(node.getChildren().get(ch), prefix, i == childrenKeys.size() - 1);
        }
    }

    /**
     * Retrieves the character that leads to this node from its parent.
     *
     * @param node the TrieNode whose associated character is needed
     * @return the character associated with this node, or '*' for the root
     */
    private char getCharFromParent(TrieNode node) {
        if (node.getParent() == null) {
            return '*'; // Root marker
        }
        for (Map.Entry<Character, TrieNode> entry : node.getParent().getChildren().entrySet()) {
            if (entry.getValue() == node) {
                return entry.getKey();
            }
        }
        return '?'; // Should never happen
    }

    /**
     * Returns all words stored in the Trie in dictionary format (one word per
     * line).
     *
     * @return a String containing all words in lexicographical order, each on a new
     *         line
     */
    public String toDictionaryFormat() {
        StringBuilder dictionary = new StringBuilder();
        collectWords(root, new StringBuilder(), dictionary);
        return dictionary.toString();
    }

    /**
     * Helper method to recursively collect words from the Trie.
     *
     * @param node   the current TrieNode being processed
     * @param word   the current word being built
     * @param result the StringBuilder accumulating the words
     */
    private void collectWords(TrieNode node, StringBuilder word, StringBuilder result) {
        if (node.isEndOfWord()) {
            result.append(word).append("\n"); // Append word to result
        }

        List<Character> sortedKeys = new ArrayList<>(node.getChildren().keySet());
        Collections.sort(sortedKeys); // Ensure lexicographical order

        for (char ch : sortedKeys) {
            word.append(ch);
            collectWords(node.getChild(ch), word, result);
            word.deleteCharAt(word.length() - 1); // Backtrack
        }
    }

    /**
     * Serializes the entire Trie structure into a JSON format.
     * This allows the Trie to be saved and later reconstructed.
     *
     * @return a JSON string representing the Trie structure.
     */
    public String toSerializedFormat() {
        Gson gson = new Gson();
        return gson.toJson(serializeNode(root));
    }

    /**
     * Recursively serializes a TrieNode into a map structure suitable for JSON
     * conversion.
     *
     * @param node the TrieNode to serialize
     * @return a Map representing the TrieNode and its children
     */
    private Map<String, Object> serializeNode(TrieNode node) {
        Map<String, Object> data = new HashMap<>();
        if (node.isEndOfWord()) {
            data.put("isEndOfWord", true);
            data.put("frequency", node.getFrequency());
        }

        Map<String, Object> childrenMap = new HashMap<>();
        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            childrenMap.put(String.valueOf(entry.getKey()), serializeNode(entry.getValue()));
        }

        if (!childrenMap.isEmpty()) {
            data.put("children", childrenMap);
        }

        return data;
    }

    /**
     * Performs a fuzzy search in the Trie, returning words within a given edit
     * distance from the input word.
     * This allows for slight misspellings or variations to be matched.
     *
     * @param word     the target word to search for.
     * @param maxEdits the maximum allowed Levenshtein distance.
     * @return a list of words from the Trie that are within the given edit
     *         distance.
     */
    public List<String> fuzzySearch(String word, int maxEdits) {
        List<String> results = new ArrayList<>();
        fuzzySearchHelper(root, "", word, maxEdits, results);
        return results;
    }

    /**
     * Recursively explores the Trie and collects words within the allowed edit
     * distance.
     *
     * @param node           the current TrieNode.
     * @param currentWord    the word formed so far.
     * @param targetWord     the original word being searched.
     * @param remainingEdits the remaining number of allowed edits.
     * @param results        the list to store matching words.
     */
    private void fuzzySearchHelper(TrieNode node, String currentWord, String targetWord, int remainingEdits,
            List<String> results) {
        if (node.isEndOfWord() && levenshteinDistance(currentWord, targetWord) <= remainingEdits) {
            results.add(currentWord);
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char nextChar = entry.getKey();
            TrieNode nextNode = entry.getValue();

            // Explore paths: match, insert, delete, and substitute
            fuzzySearchHelper(nextNode, currentWord + nextChar, targetWord, remainingEdits, results);
            if (remainingEdits > 0) {
                fuzzySearchHelper(node, currentWord, targetWord.substring(1), remainingEdits - 1, results); // Deletion
                fuzzySearchHelper(nextNode, currentWord + targetWord.charAt(0), targetWord.substring(1),
                        remainingEdits - 1, results); // Substitution
                if (!targetWord.isEmpty()) {
                    fuzzySearchHelper(nextNode, currentWord + targetWord.charAt(0) + nextChar, targetWord.substring(1),
                            remainingEdits - 1, results); // Insertion
                }
            }
        }
    }

    /**
     * Computes the Levenshtein Distance between two words.
     *
     * @param s1 the first word.
     * @param s2 the second word.
     * @return the number of edits required to convert s1 to s2.
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(
                            dp[i - 1][j] + 1, // Deletion
                            Math.min(
                                    dp[i][j - 1] + 1, // Insertion
                                    dp[i - 1][j - 1] + cost // Substitution
                            ));
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    /**
     * Performs a wildcard search in the Trie, returning all words that match the
     * given pattern.
     * Supports:
     * - '?' : Matches exactly one character.
     * - '*' : Matches zero or more characters.
     *
     * @param pattern the search pattern containing wildcards.
     * @return a list of words that match the pattern.
     */
    public List<String> wildcardSearch(String pattern) {
        List<String> results = new ArrayList<>();
        wildcardSearchHelper(root, "", pattern, 0, results);
        return results;
    }

    /**
     * Recursively explores the Trie to match the wildcard pattern.
     *
     * @param node        the current TrieNode.
     * @param currentWord the word formed so far.
     * @param pattern     the wildcard pattern.
     * @param index       the current position in the pattern.
     * @param results     the list to store matching words.
     */
    private void wildcardSearchHelper(TrieNode node, String currentWord, String pattern, int index,
            List<String> results) {
        if (index == pattern.length()) {
            if (node.isEndOfWord()) {
                results.add(currentWord);
            }
            return;
        }

        char ch = pattern.charAt(index);

        if (ch == '?') {
            // '?' matches any single character
            for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
                wildcardSearchHelper(entry.getValue(), currentWord + entry.getKey(), pattern, index + 1, results);
            }
        } else if (ch == '*') {
            // '*' matches zero or more characters
            wildcardSearchHelper(node, currentWord, pattern, index + 1, results); // '*' as empty

            for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
                wildcardSearchHelper(entry.getValue(), currentWord + entry.getKey(), pattern, index, results);
                wildcardSearchHelper(entry.getValue(), currentWord + entry.getKey(), pattern, index + 1, results);
            }
        } else {
            // Regular character match
            if (node.hasChild(ch)) {
                wildcardSearchHelper(node.getChild(ch), currentWord + ch, pattern, index + 1, results);
            }
        }
    }

    /**
     * Searches the Trie for words that match a given regular expression.
     *
     * @param regex the regular expression pattern to match words against.
     * @return a list of words that match the regex pattern.
     */
    public List<String> regexSearch(String regex) {
        List<String> results = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex); // Compile regex pattern
        collectMatchingWords(root, "", pattern, results);
        return results;
    }

    /**
     * Recursively explores the Trie and collects words that match the regex.
     *
     * @param node        the current TrieNode.
     * @param currentWord the word formed so far.
     * @param pattern     the compiled regex pattern.
     * @param results     the list to store matching words.
     */
    private void collectMatchingWords(TrieNode node, String currentWord, Pattern pattern, List<String> results) {
        if (node.isEndOfWord()) {
            Matcher matcher = pattern.matcher(currentWord);
            if (matcher.matches()) {
                results.add(currentWord);
            }
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            collectMatchingWords(entry.getValue(), currentWord + entry.getKey(), pattern, results);
        }
    }

    /**
     * Replaces an existing word in the Trie with a new word.
     * 
     * @param oldWord the word to be replaced.
     * @param newWord the word to replace it with.
     * @return {@code true} if the old word was successfully replaced, {@code false}
     *         if the old word did not exist.
     */
    public boolean replaceWord(String oldWord, String newWord) {
        if (oldWord == null || oldWord.isEmpty() || newWord == null || newWord.isEmpty()) {
            return false; // Invalid input
        }

        if (!search(oldWord)) {
            return false; // Old word does not exist
        }

        int frequency = getFrequency(oldWord); // Preserve frequency
        delete(oldWord);
        insert(newWord);
        setFrequency(newWord, frequency); // Transfer frequency to new word

        return true;
    }

    /**
     * Merges another StandardTrie into this trie, adding all words and their
     * frequencies.
     * If a word exists in both tries, their frequencies are summed.
     *
     * @param otherTrie the trie to merge with.
     */
    public void mergeWith(StandardTrie otherTrie) {
        if (otherTrie == null || otherTrie.isEmpty()) {
            return; // Nothing to merge
        }
        mergeNodes(this.root, otherTrie.root, new StringBuilder());
    }

    /**
     * Recursively merges nodes from another trie into the current trie.
     *
     * @param currentNode the node in this trie.
     * @param otherNode   the corresponding node in the other trie.
     * @param prefix      the current prefix being processed.
     */
    private void mergeNodes(TrieNode currentNode, TrieNode otherNode, StringBuilder prefix) {
        if (otherNode.isEndOfWord()) {
            String word = prefix.toString();
            int newFrequency = otherNode.getFrequency();
            if (search(word)) {
                setFrequency(word, getFrequency(word) + newFrequency);
            } else {
                insert(word);
                setFrequency(word, newFrequency);
            }
        }

        for (char ch : otherNode.getChildren().keySet()) {
            if (!currentNode.hasChild(ch)) {
                currentNode.addChild(ch);
            }
            prefix.append(ch);
            mergeNodes(currentNode.getChild(ch), otherNode.getChild(ch), prefix);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Creates a new StandardTrie containing only the words present in both tries.
     * The frequency of each word in the result is the minimum of its frequencies in
     * both tries.
     *
     * @param otherTrie the trie to intersect with.
     * @return a new StandardTrie containing the intersection of words.
     */
    public StandardTrie intersectWith(StandardTrie otherTrie) {
        if (otherTrie == null || otherTrie.isEmpty() || this.isEmpty()) {
            return new StandardTrie(); // Intersection with empty trie is empty
        }
        StandardTrie result = new StandardTrie();
        findIntersection(this.root, otherTrie.root, new StringBuilder(), result);
        return result;
    }

    /**
     * Recursively finds words that exist in both tries and adds them to the result
     * trie.
     *
     * @param node1  the node in the first trie.
     * @param node2  the node in the second trie.
     * @param prefix the current prefix being processed.
     * @param result the trie where the intersection will be stored.
     */
    private void findIntersection(TrieNode node1, TrieNode node2, StringBuilder prefix, StandardTrie result) {
        if (node1.isEndOfWord() && node2.isEndOfWord()) {
            String word = prefix.toString();
            int minFrequency = Math.min(node1.getFrequency(), node2.getFrequency());
            result.insert(word);
            result.setFrequency(word, minFrequency);
        }

        for (char ch : node1.getChildren().keySet()) {
            if (node2.hasChild(ch)) {
                prefix.append(ch);
                findIntersection(node1.getChild(ch), node2.getChild(ch), prefix, result);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    /**
     * Creates a new StandardTrie containing words that are present in this trie but
     * not in the other.
     * The frequencies remain the same as in this trie.
     *
     * @param otherTrie the trie to subtract from this trie.
     * @return a new StandardTrie containing the difference of words.
     */
    public StandardTrie differenceWith(StandardTrie otherTrie) {
        if (otherTrie == null || otherTrie.isEmpty()) {
            return this.clone(); // If the other trie is empty, return a copy of this trie
        }
        if (this.isEmpty()) {
            return new StandardTrie(); // If this trie is empty, the difference is also empty
        }

        StandardTrie result = new StandardTrie();
        findDifference(this.root, otherTrie.root, new StringBuilder(), result);
        return result;
    }

    /**
     * Recursively finds words that exist in this trie but not in the other and adds
     * them to the result trie.
     *
     * @param node1  the node in this trie.
     * @param node2  the node in the other trie.
     * @param prefix the current prefix being processed.
     * @param result the trie where the difference will be stored.
     */
    private void findDifference(TrieNode node1, TrieNode node2, StringBuilder prefix, StandardTrie result) {
        if (node1.isEndOfWord() && (node2 == null || !node2.isEndOfWord())) {
            String word = prefix.toString();
            result.insert(word);
            result.setFrequency(word, node1.getFrequency());
        }

        for (char ch : node1.getChildren().keySet()) {
            TrieNode child1 = node1.getChild(ch);
            TrieNode child2 = (node2 != null) ? node2.getChild(ch) : null;
            prefix.append(ch);
            findDifference(child1, child2, prefix, result);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Creates a deep copy of this StandardTrie.
     *
     * @return a new StandardTrie that is an exact copy of this trie.
     */
    public StandardTrie clone() {
        StandardTrie clonedTrie = new StandardTrie();
        cloneHelper(this.root, clonedTrie, new StringBuilder());
        return clonedTrie;
    }

    /**
     * Recursively copies all words and their frequencies from the original trie to
     * the cloned trie.
     *
     * @param node       the current node in the original trie.
     * @param clonedTrie the trie being constructed as a copy.
     * @param prefix     the current prefix being processed.
     */
    private void cloneHelper(TrieNode node, StandardTrie clonedTrie, StringBuilder prefix) {
        if (node.isEndOfWord()) {
            String word = prefix.toString();
            clonedTrie.insert(word);
            clonedTrie.setFrequency(word, node.getFrequency());
        }

        for (char ch : node.getChildren().keySet()) {
            prefix.append(ch);
            cloneHelper(node.getChild(ch), clonedTrie, prefix);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Trims all words in the Trie by removing leading and trailing spaces.
     * Words with trimmed versions will be reinserted, and the old versions removed.
     */
    public void trim() {
        List<String> words = getAllWords(); // Retrieve all words
        for (String word : words) {
            String trimmedWord = word.trim();
            if (!trimmedWord.equals(word)) { // Only update if trimming changed the word
                int frequency = getFrequency(word);
                delete(word);
                insert(trimmedWord);
                setFrequency(trimmedWord, frequency); // Preserve original frequency
            }
        }
    }
}
