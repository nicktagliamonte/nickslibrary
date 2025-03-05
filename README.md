# nickslibrary
A library of data structures implemented in Java  

In this normalized plot comparing the differences of individual methods between my items and java's, items in blue indicate that the operation was faster on my implementation. Items in red indicate Java's (or Apache's or Guava's) was faster  
Instances of methods where the difference was zero or very near zero were disregarded, and those methods make up the overwhelming majority of the methods in this repository. Mostly, my implementation is a wash when compared to the real things. But I did get a couple in marginally faster!  
![Alt text](https://raw.githubusercontent.com/nicktagliamonte/nickslibrary/refs/heads/main/scripts/benchmark_plot.png)

## List of structures  
### Linear  
- Dynamic Array
- Linked List
- Array-based stack
- Linked List based stack
- Circular Queue
- Deque
### Hashing
- HashTable (variety of collision resolution methods available)
### Trees
- Binary Search Tree
- AVL Tree
- Red Black Tree
- B Tree
- Standard Trie
- Radix Trie
- Treap
### Heaps
- Min Heap
- Max Heap
- Fibonacci Heap
### Graphs
- Adjacency List
- Adjacency Matrix
- Disjoint Set
### Advanced/Other
- Bloom Filter
- Bloomier Filter
- Count Min Sketch
- Dancing Links
- Fenwick Tree
- HyperLogLog
- KD Tree
- OctTree
- QuadTree
- Rope
- Segment Tree
- Skip List
- Splay Tree
- Suffix Tree
- Van Emde Boas Tree

## Notes
A release is included, so it is technically possible to download the jar and use these if you really wanted.  
Primarily the structures exist for the sake of my having created them and they are largely less efficient or useful than the structures you'd get from java.util, so there's not really any benefit to actually using these.  
Tests are available in the test folder for most of the structures, but as I progressed into the more niche end of Advance/Other, testing was largely done locally in main or even in the files themselves and deleted after they passed.
