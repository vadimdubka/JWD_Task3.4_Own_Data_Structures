package com.dubatovka.collection;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedListCustom<E> implements Iterable<E> {
    private static final String TEMPLATE_MESSAGE = "Index: %d, Size: %d";
    private Node<E> head;
    private Node<E> last;
    private int size;
    private int modificationCount = 0;
    
    public void add(E value) {
        linkLast(value);
    }
    
    public void add(int index, E value) {
        checkPositionIndex(index);
        
        if (index == size) {
            linkLast(value);
        } else {
            Node<E> nodeBefore = getNode(index);
            linkBefore(value, nodeBefore);
        }
    }
    
    public void addFirst(E e) {
        linkFirst(e);
    }
    
    public void set(int index, E value) {
        checkElementIndex(index);
        Node<E> x = getNode(index);
        x.data = value;
    }
    
    public E get(int index) {
        checkElementIndex(index);
        Node<E> node = getNode(index);
        return node.data;
    }
    
    public E remove(int index) {
        checkElementIndex(index);
        Node<E> node = getNode(index);
        return deleteLinks(node);
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public Iterator<E> iterator() {
        return listIterator(0);
    }
    
    public ListIterator<E> listIterator(int index) {
        return new ListIteratorCustom(index);
    }
    
    private Node<E> getNode(int index) {
        Node<E> x = head;
        for (int i = 0; i < index; i++) {
            x = x.next;
        }
        return x;
    }
    
    private void linkFirst(E value) {
        Node<E> hd = head;
        Node<E> newNode = new Node<>(null, value, hd);
        head = newNode;
        if (hd == null) {
            last = newNode;
        } else {
            hd.previous = newNode;
        }
        size++;
        modificationCount++;
    }
    
    private void linkLast(E value) {
        Node<E> lst = last;
        Node<E> newNode = new Node<>(last, value, null);
        last = newNode;
        if (lst == null) {
            head = newNode;
        } else {
            lst.next = newNode;
        }
        size++;
        modificationCount++;
    }
    
    private void linkBefore(E value, Node<E> nodeBefore) {
        Node<E> prev = nodeBefore.previous;
        Node<E> newNode = new Node<>(prev, value, nodeBefore);
        nodeBefore.previous = newNode;
        if (prev == null) {
            head = newNode;
        } else {
            prev.next = newNode;
        }
        size++;
        modificationCount++;
    }
    
    private E deleteLinks(Node<E> nodeToDelete) {
        E data = nodeToDelete.data;
        Node<E> next = nodeToDelete.next;
        Node<E> previous = nodeToDelete.previous;
        
        if (previous == null) {
            head = next;
        } else {
            previous.next = next;
        }
        
        if (next == null) {
            last = previous;
        } else {
            next.previous = previous;
        }
        
        nodeToDelete.data = null;
        nodeToDelete.previous = null;
        nodeToDelete.next = null;
        
        size--;
        modificationCount++;
        return data;
    }
    
    private void checkPositionIndex(int index) {
        boolean wrong = (index < 0) || (index > size);
        if (wrong) {
            String message = String.format(TEMPLATE_MESSAGE, index, size);
            throw new IndexOutOfBoundsException(message);
        }
    }
    
    private void checkElementIndex(int index) {
        boolean wrong = (index < 0) || (index >= size);
        if (wrong) {
            String message = String.format(TEMPLATE_MESSAGE, index, size);
            throw new IndexOutOfBoundsException(message);
        }
    }
    
    private static class Node<E> {
        private E data;
        private Node<E> next;
        private Node<E> previous;
        
        Node(Node<E> previous, E element, Node<E> next) {
            this.data = element;
            this.next = next;
            this.previous = previous;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Node)) return false;
            
            Node<?> node = (Node<?>) obj;
            
            return (data != null) ? data.equals(node.data) : (node.data == null);
            
        }
        
        @Override
        public int hashCode() {
            return (data != null) ? data.hashCode() : 0;
        }
    }
    
    private final class ListIteratorCustom implements ListIterator<E> {
        private int nextIndex;
        private Node<E> nextToReturn;
        private Node<E> lastReturned;
        private int expectedModificationCount = modificationCount;
        
        private ListIteratorCustom() {
            this(0);
        }
        
        private ListIteratorCustom(int index) {
            nextIndex = index;
            boolean hasNext = index < size;
            if (hasNext) {
                nextToReturn = getNode(index);
            }
        }
        
        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }
        
        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }
        
        @Override
        public E next() {
            checkForOuterModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            E data = nextToReturn.data;
            lastReturned = nextToReturn;
            nextToReturn = nextToReturn.next;
            nextIndex++;
            return data;
        }
        
        @Override
        public E previous() {
            checkForOuterModification();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            
            if (nextToReturn == null) {
                nextToReturn = last;
                lastReturned = last;
            } else {
                nextToReturn = nextToReturn.previous;
                lastReturned = nextToReturn.previous;
            }
            E data = lastReturned.data;
            nextIndex--;
            return data;
        }
        
        @Override
        public void remove() {
            checkForOuterModification();
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            
            Node<E> lastNext = lastReturned.next;
            deleteLinks(lastReturned);
            expectedModificationCount++;
            if (Objects.equals(nextToReturn, lastReturned)) {
                nextToReturn = lastNext;
            } else {
                nextIndex--;
            }
            lastReturned = null;
        }
        
        @Override
        public int nextIndex() {
            return nextIndex;
        }
        
        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }
        
        @Override
        public void set(E value) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            checkForOuterModification();
            lastReturned.data = value;
        }
        
        @Override
        public void add(E value) {
            checkForOuterModification();
            lastReturned = null;
            if (nextToReturn == null) {
                linkLast(value);
            } else {
                linkBefore(value, nextToReturn);
            }
            nextIndex++;
            expectedModificationCount++;
        }
        
        private void checkForOuterModification() {
            if (modificationCount != expectedModificationCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
    
}
