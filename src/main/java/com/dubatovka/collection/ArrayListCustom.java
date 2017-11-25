package com.dubatovka.collection;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayListCustom<E> implements Iterable<E> {
    private static final int INITIAL_CAPACITY = 10;
    private static final int INCREASE_CAPACITY_COEFFICIENT = 2;
    private int size = 0;
    private Object[] elementData;
    private int modificationCount = 0;
    
    public ArrayListCustom() {
        this.elementData = new Object[INITIAL_CAPACITY];
    }
    
    public ArrayListCustom(int initCapacity) {
        if (initCapacity > 0) {
            elementData = new Object[initCapacity];
        } else if (initCapacity == 0) {
            elementData = new Object[INITIAL_CAPACITY];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initCapacity);
        }
        
    }
    
    public void add(E value) {
        modificationCount++;
        ensureCapacity();
        elementData[size] = value;
        size++;
    }
    
    public void add(int index, E value) {
        modificationCount++;
        ensureCapacity();
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = value;
        size++;
    }
    
    public void set(int index, E value) {
        elementData[index] = value;
    }
    
    @SuppressWarnings("unchecked")
    public E get(int index) {
        rangeCheck(index);
        return (E) elementData[index];
    }
    
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        rangeCheck(index);
        modificationCount++;
        
        Object removedElement = elementData[index];
        
        int numMoved = size - 1 - index;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        --size;
        elementData[size] = null;
        return (E) removedElement;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new IteratorCustom();
    }
    
    private void rangeCheck(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Array size " + index);
        }
    }
    
    private void ensureCapacity() {
        if (size == elementData.length) {
            increaseCapacity();
        }
    }
    
    private void increaseCapacity() {
        int newIncreasedCapacity = elementData.length * INCREASE_CAPACITY_COEFFICIENT;
        elementData = Arrays.copyOf(elementData, newIncreasedCapacity);
    }
    
    private final class IteratorCustom implements Iterator<E> {
        private int nextToReturn = 0;
        private int lastReturned = -1;
        private int expectedModificationCount = modificationCount;
        
        @Override
        public boolean hasNext() {
            return nextToReturn != size();
        }
        
        @Override
        public E next() {
            checkForOuterModification();
            try {
                int i = nextToReturn;
                E next = get(i);
                lastReturned = i;
                nextToReturn = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForOuterModification();
                String message = e.getMessage();
                throw new NoSuchElementException(message);
            }
        }
        
        @Override
        public void remove() {
            if (lastReturned < 0) {
                throw new IllegalStateException("There is no element to remove.");
            }
            checkForOuterModification();
            
            try {
                ArrayListCustom.this.remove(lastReturned);
                if (lastReturned < nextToReturn) {
                    nextToReturn -= 1;
                }
                lastReturned = -1;
                expectedModificationCount = modificationCount;
            } catch (IndexOutOfBoundsException e) {
                String message = e.getMessage();
                throw new ConcurrentModificationException(message);
            }
        }
        
        void checkForOuterModification() {
            if (modificationCount != expectedModificationCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
