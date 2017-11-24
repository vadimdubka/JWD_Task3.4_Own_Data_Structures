package com.dubatovka.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class ArrayListCustomTest {
    @Test
    public void addTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(1);
        list.add(2);
        Assert.assertEquals(6, list.size());
        
        int actual1 = list.get(1);
        int actual0 = list.get(0);
        int actual2 = list.get(2);
        int actual3 = list.get(3);
        int actual4 = list.get(4);
        
        Assert.assertEquals(2, actual1);
        Assert.assertEquals(1, actual0);
        Assert.assertEquals(3, actual2);
        Assert.assertEquals(4, actual3);
        Assert.assertEquals(1, actual4);
    }
    
    @Test
    public void addPositionTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
        list.add(1, 3);
    
        int actual0 = list.get(0);
        int actual1 = list.get(1);
        int actual2 = list.get(2);
    
        Assert.assertEquals(1, actual0);
        Assert.assertEquals(3, actual1);
        Assert.assertEquals(2, actual2);
        Assert.assertEquals(3, list.size());
    }
    
    @Test
    public void removeTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(1);
        list.add(2);
        
        Assert.assertEquals(6, list.size());
        int removedElem = list.remove(1);
        Assert.assertEquals(2, removedElem);
        int elem1 = list.get(1);
        Assert.assertEquals(3, elem1);
        Assert.assertEquals(5, list.size());
    }
    
    @Test
    public void forEachTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(1);
        list.add(2);
        
        ArrayListCustom<Integer> list2 = new ArrayListCustom<>();
        for (Integer integer : list) {
            list2.add(integer);
        }

        Assert.assertEquals(list.size(), list2.size());
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(list.get(i), list2.get(i));
        }
    }
    
    @Test
    public void iteratorTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(1);
        list.add(2);
        
        ArrayListCustom<Integer> list2 = new ArrayListCustom<>();
        
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            list2.add(iterator.next());
        }
    
        Assert.assertEquals(list.size(), list2.size());
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(list.get(i), list2.get(i));
        }
    }
    
    @Test
    public void iteratorRemoveTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        
        Assert.assertEquals(0, list.size());
    }
    
    @Test(expected = ConcurrentModificationException.class)
    public void removeConcurrentModificationExceptionTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
    
        Iterator<Integer> iterator = list.iterator();
        list.remove(1);
        while (iterator.hasNext()) {
            iterator.next();
        }
    }
    
    @Test(expected = ConcurrentModificationException.class)
    public void addConcurrentModificationExceptionTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
        
        Iterator<Integer> iterator = list.iterator();
        list.add(3);
        while (iterator.hasNext()) {
            iterator.next();
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void getException() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
        list.get(11);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void removeException() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
        list.remove(11);
    }
    
    @Test
    public void isEmptyTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        boolean empty = list.isEmpty();
        Assert.assertEquals(true, empty);
    }
    
    @Test
    public void setTest(){
        ArrayListCustom<Integer> list = new ArrayListCustom<>();
        list.add(1);
        list.add(2);
    
        list.set(0, 10);
        int actualInt = list.get(0);
        Assert.assertEquals(10, actualInt);
    }
    
    @Test
    public void constructorTest() {
        ArrayListCustom<Integer> list = new ArrayListCustom<>(2);
        list.add(1);
        list.add(2);
        int el0 = list.get(0);
        int el1 = list.get(1);
        Assert.assertEquals(1, el0);
        Assert.assertEquals(2, el1);
    }
}
