package com.dubatovka.collection;

import java.util.*;

public class ArrayListCustom<E> implements Iterable<E> {
    private static final int INITIAL_CAPACITY = 10;
    private static final int INCREASE_CAPACITY_COEFFICIENT = 2;
    private int size = 0;
    private Object elementData[] = {};
    private int modificationCount = 0;
    
    public ArrayListCustom() {
        elementData = new Object[INITIAL_CAPACITY];
    }
    
    public ArrayListCustom(int initCapacity) {
        if (initCapacity > 0) {
            elementData = new Object[initCapacity];
        } else if (initCapacity == 0) {
            elementData = new Object[INITIAL_CAPACITY];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initCapacity);
        }
        
    }
    
    public void add(E value) {
        modificationCount++;
        ensureCapacity();
        elementData[size++] = value;
    }
    
    public void add(int index, E value) {
        modificationCount++;
        ensureCapacity();
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = value;
        size++;
    }
    
    public void set(int index, E value) {
        E oldValue = get(index);
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
        elementData[--size] = null;
        return (E) removedElement;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
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
    
    @Override
    public Iterator<E> iterator() {
        return new IteratorCustom();
    }
    
    private class IteratorCustom implements Iterator<E> {
        int nextToReturn = 0;
        int lastReturned = -1;
        int expectedModificationCount = modificationCount;
        
        public boolean hasNext() {
            return nextToReturn != size();
        }
        
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
                throw new NoSuchElementException();
            }
        }
        
        public void remove() {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }
            checkForOuterModification();
            
            try {
                ArrayListCustom.this.remove(lastReturned);
                if (lastReturned < nextToReturn) {
                    nextToReturn--;
                }
                lastReturned = -1;
                expectedModificationCount = modificationCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
        
        final void checkForOuterModification() {
            if (modificationCount != expectedModificationCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
