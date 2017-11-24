package com.dubatovka.collection;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryTreeCustom<K extends Comparable<K>, V> implements Iterable<BinaryTreeCustom.Node<K, V>> {
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
    
    static final class Node<K, V> {
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
        
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        public boolean equals(Object o) {
            if (!(o instanceof BinaryTreeCustom.Node))
                return false;
            Node<?, ?> e = (Node<?, ?>) o;
            
            return valEquals(key, e.getKey()) && valEquals(value, e.getValue());
        }
        
        public int hashCode() {
            int keyHash = (key == null ? 0 : key.hashCode());
            int valueHash = (value == null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }
    }
    
    public V get(K key) {
        Node<K, V> x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                return x.value;
            }
            if (cmp < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        return null;
    }
    
    public void add(K key, V value) {
        modificationCount++;
        Node<K, V> x = root;
        Node<K, V> y = null;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                x.value = value;
                return;
            } else {
                y = x;
                if (cmp < 0) {
                    x = x.left;
                } else {
                    x = x.right;
                }
            }
        }
        
        Node<K, V> newNode = new Node<K, V>(key, value, y);
        if (y == null) {
            root = newNode;
        } else {
            if (key.compareTo(y.key) < 0) {
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
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                break;
            } else {
                prevLevelRoot = x;
                if (cmp < 0) {
                    x = x.left;
                } else {
                    x = x.right;
                }
            }
        }
        if (x == null) {
            return;
        }
        if (x.right == null) {
            if (prevLevelRoot == null) {
                root = x.left;
            } else {
                if (x == prevLevelRoot.left) {
                    prevLevelRoot.left = x.left;
                } else {
                    prevLevelRoot.right = x.left;
                }
            }
        } else {
            Node<K, V> leftMost = x.right;
            prevLevelRoot = null;
            while (leftMost.left != null) {
                prevLevelRoot = leftMost;
                leftMost = leftMost.left;
            }
            if (prevLevelRoot != null) {
                prevLevelRoot.left = leftMost.right;
            } else {
                x.right = leftMost.right;
            }
            x.key = leftMost.key;
            x.value = leftMost.value;
        }
        size--;
    }
    
    public Node<K, V> next() {
        return null;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public Iterator<Node<K, V>> iterator() {
        return new IteratorCustom(getFirstInOrderNode());
    }
    
    @SuppressWarnings("unchecked")
    private int compare(Object k1, Object k2) {
        if (comparator == null) {
            return ((Comparable<? super K>) k1).compareTo((K) k2);
        } else {
            return comparator.compare((K) k1, (K) k2);
        }
    }
    
    private static boolean valEquals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        } else {
            return o1.equals(o2);
        }
    }
    
    
    private class IteratorCustom implements Iterator<Node<K, V>> {
        Node<K, V> nextToReturn;
        Node<K, V> lastReturned;
        int expectedModificationCount;
        
        IteratorCustom(Node<K, V> first) {
            expectedModificationCount = modificationCount;
            lastReturned = null;
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
            nextToReturn = successor(e);
            lastReturned = e;
            return e;
        }
    }
    
    private Node<K, V> getFirstInOrderNode() {
        Node<K, V> p = root;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
        }
        return p;
    }
    
    private Node<K, V> successor(Node<K, V> t) {
        if (t == null) {
            return null;
        } else if (t.right != null) {
            Node<K, V> p = t.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        } else {
            Node<K, V> p = t.parent;
            Node<K, V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }
    
}
