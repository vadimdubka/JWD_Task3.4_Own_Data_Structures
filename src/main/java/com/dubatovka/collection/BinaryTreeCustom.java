package com.dubatovka.collection;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class BinaryTreeCustom<K, V> implements Iterable<BinaryTreeCustom.Node<K, V>> {
    private Node<K, V> root;
    private int size;
    private int modificationCount = 0;
    
    private final Comparator<? super K> comparator;
    
    public BinaryTreeCustom() {
        comparator = null;
    }
    
    public BinaryTreeCustom(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }
    
    public V get(K key) {
        V value = null;
        Node<K, V> x = root;
        while (x != null) {
            int cmp = compare(key, x.key);
            if (cmp == 0) {
                value = x.value;
            }
            x = (cmp < 0) ? x.left : x.right;
        }
        return value;
    }
    
    public void add(K key, V value) {
        modificationCount++;
        Node<K, V> x = root;
        Node<K, V> y = null;
        while (x != null) {
            int cmp = compare(key, x.key);
            if (cmp == 0) {
                x.value = value;
                return;
            } else {
                y = x;
                x = (cmp < 0) ? x.left : x.right;
            }
        }
        
        Node<K, V> newNode = new Node<>(key, value, y);
        if (y == null) {
            root = newNode;
        } else {
            if (compare(key, y.key) < 0) {
                y.left = newNode;
            } else {
                y.right = newNode;
            }
        }
        size++;
    }
    
    public void remove(K key) {
        modificationCount++;
        Node<K, V> x = root;
        Node<K, V> prevLevelRoot = null;
        while (x != null) {
            int cmp = compare(key, x.key);
            if (cmp == 0) {
                unlinkNode(x, prevLevelRoot);
                return;
            } else {
                prevLevelRoot = x;
                x = (cmp < 0) ? x.left : x.right;
            }
        }
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public Iterator<Node<K, V>> iterator() {
        Node<K, V> firstInOrderNode = getFirstInOrderNode();
        return new IteratorCustom(firstInOrderNode);
    }
    
    private void unlinkNode(Node<K, V> x, Node<K, V> prevLevelRoot) {
        if (x.right == null) {
            if (prevLevelRoot == null) {
                root = x.left;
            } else {
                if (Objects.equals(x, prevLevelRoot.left)) {
                    prevLevelRoot.left = x.left;
                } else {
                    prevLevelRoot.right = x.left;
                }
            }
        } else {
            prevLevelRoot = null;
            Node<K, V> leftMost = x.right;
            while (leftMost.left != null) {
                prevLevelRoot = leftMost;
                leftMost = leftMost.left;
            }
            
            x.key = leftMost.key;
            x.value = leftMost.value;
            
            if (prevLevelRoot != null) {
                prevLevelRoot.left = leftMost.right;
            } else {
                x.right = leftMost.right;
            }
            
        }
        size--;
    }
    
    @SuppressWarnings("unchecked")
    private int compare(Object k1, Object k2) {
        int result;
        if (comparator == null) {
            result = ((Comparable<? super K>) k1).compareTo((K) k2);
        } else {
            result = comparator.compare((K) k1, (K) k2);
        }
        return result;
    }
    
    private Node<K, V> getFirstInOrderNode() {
        Node<K, V> t = root;
        if (t != null) {
            while (t.left != null) {
                t = t.left;
            }
        }
        return t;
    }
    
    public static final class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> parent;
        private Node<K, V> left;
        private Node<K, V> right;
        
        private Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
        
        public K getKey() {
            return key;
        }
        
        public V getValue() {
            return value;
        }
        
        public void setValue(V value) {
            this.value = value;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            
            Node<?, ?> node = (Node<?, ?>) o;
            
            if (key != null) {
                if (!key.equals(node.key)) {
                    return false;
                }
            } else {
                if (node.key != null) {
                    return false;
                }
            }
            return (value != null) ? value.equals(node.value) : (node.value == null);
            
        }
        
        @Override
        public int hashCode() {
            int result = (key != null) ? key.hashCode() : 0;
            result = (31 * result) + ((value != null) ? value.hashCode() : 0);
            return result;
        }
    }
    
    private class IteratorCustom implements Iterator<Node<K, V>> {
        private Node<K, V> nextToReturn;
        private int expectedModificationCount = modificationCount;
    
        IteratorCustom() {
            expectedModificationCount = modificationCount;
            nextToReturn = null;
        }
        
        IteratorCustom(Node<K, V> first) {
            expectedModificationCount = modificationCount;
            nextToReturn = first;
        }
        
        @Override
        public final boolean hasNext() {
            return nextToReturn != null;
        }
        
        @Override
        public Node<K, V> next() {
            Node<K, V> e = nextToReturn;
            if (e == null) {
                throw new NoSuchElementException();
            }
            if (modificationCount != expectedModificationCount) {
                throw new ConcurrentModificationException();
            }
            nextToReturn = getNextInOrderNode(e);
            return e;
        }
        
        private Node<K, V> getNextInOrderNode(Node<K, V> currentNode) {
            Node<K, V> nextInOrderNode = null;
            
            if (currentNode != null) {
                boolean hasRightBranch = currentNode.right != null;
                if (hasRightBranch) {
                    nextInOrderNode = findMostLeftNodeInRightBranch(currentNode);
                } else {
                    Node<K, V> superParent = currentNode.parent;
                    Node<K, V> t = currentNode;
                    while ((superParent != null) && Objects.equals(t, superParent.right)) {
                        t = superParent;
                        superParent = superParent.parent;
                    }
                    nextInOrderNode = superParent;
                }
            }
            
            return nextInOrderNode;
        }
        
        private Node<K, V> findMostLeftNodeInRightBranch(Node<K, V> currentNode) {
            Node<K, V> t = currentNode.right;
            while (t.left != null) {
                t = t.left;
            }
            return t;
        }
    }
}
