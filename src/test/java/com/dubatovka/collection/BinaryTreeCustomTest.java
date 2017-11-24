package com.dubatovka.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class BinaryTreeCustomTest {
    
    @Test
    public void add() {
        BinaryTreeCustom<Integer, String> tree = new BinaryTreeCustom<>();
        tree.add(0, "zero");
        tree.add(1, "one");
        tree.add(2, "two");
        
        Assert.assertEquals(3, tree.size());
        
        tree.add(3, "three");
        tree.add(2, "two");
        Assert.assertEquals(4, tree.size());
    }
    
    @Test
    public void get() {
        BinaryTreeCustom<Integer, String> tree = new BinaryTreeCustom<>();
        tree.add(0, "zero");
        tree.add(1, "one");
        tree.add(2, "two");
        tree.add(3, "three");
        
        String value1 = tree.get(1);
        String value2 = tree.get(2);
        Assert.assertEquals("one", value1);
        Assert.assertEquals("two", value2);
    }
    
    @Test
    public void remove() {
        BinaryTreeCustom<Integer, String> tree = new BinaryTreeCustom<>();
        tree.add(0, "zero");
        tree.add(1, "one");
        tree.add(2, "two");
        tree.add(3, "three");
        
        tree.remove(2);
        Assert.assertEquals(3, tree.size());
        String value2 = tree.get(2);
        String value3 = tree.get(3);
        Assert.assertEquals(null, value2);
        Assert.assertEquals("three", value3);
    }
    
    @Test
    public void iterator() {
        BinaryTreeCustom<Integer, String> tree = new BinaryTreeCustom<>();
        tree.add(0, "zero");
        tree.add(1, "one");
        tree.add(2, "two");
        tree.add(3, "three");
        
        Iterator<BinaryTreeCustom.Node<Integer, String>> iterator = tree.iterator();
        Assert.assertNotEquals(null, iterator);
        
        BinaryTreeCustom<Integer, String> tree2 = new BinaryTreeCustom<>();
        
        while (iterator.hasNext()) {
            BinaryTreeCustom.Node<Integer, String> node = iterator.next();
            tree2.add(node.getKey(), node.getValue());
        }
        
        Assert.assertEquals(tree.size(), tree2.size());
        
        for (int i = 0; i < tree.size(); i++) {
            Assert.assertEquals(tree.get(i), tree2.get(i));
        }
    }
    
    @Test(expected = ConcurrentModificationException.class)
    public void removeConcurrentModificationExceptionTest() {
        BinaryTreeCustom<Integer, String> tree = new BinaryTreeCustom<>();
        tree.add(0, "zero");
        tree.add(1, "one");
        tree.add(2, "two");
        tree.add(3, "three");
        
        Iterator<BinaryTreeCustom.Node<Integer, String>> iterator = tree.iterator();
        tree.remove(1);
        while (iterator.hasNext()) {
            iterator.next();
        }
    }
    
    //TODO сделать невидимыми внутренник класс итератора и в ноде не додоступны поля
    
    @Test
    public void order() {
        BinaryTreeCustom<Integer, String> tree = new BinaryTreeCustom<>();
        tree.add(1, "one");
        tree.add(3, "three");
        tree.add(0, "zero");
        tree.add(2, "two");
    
        for (BinaryTreeCustom.Node<Integer, String> node : tree) {
            System.out.println(node.getKey() + " - " + node.getValue());
        }
    
        String[] arr = {"zero", "one", "two", "three"};
        
       /* for (int i = 0; i < tree.size(); i++) {
            System.out.println(t);
        }*/
    }
    
    
    @Test
    public void isEmpty() {
        
    }
    
    
}