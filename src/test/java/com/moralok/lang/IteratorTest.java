package com.moralok.lang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author moralok
 * @since 2021/1/31
 */
public class IteratorTest {

    private  List<String> names;

    @BeforeEach
    void setUp() {
        names = new ArrayList<>(8);
        names.add("red");
        names.add("blue");
        names.add("yellow");
    }

    @Test
    void iterateTest() {
        Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            iterator.remove();
        }
        System.out.println(names.size());
    }

    @Test
    void foreachRemainingTest() {
        Iterator<String> iterator = names.iterator();
        iterator.forEachRemaining(System.out::println);
    }

    @Test
    void modifyElementTest() {
        // lambda表达式中不能修改集合元素的描述也太不准确了
        List<int[]> list = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            list.add(new int[] {i});
        }
        System.out.println(list.get(0)[0]);
        Iterator<int[]> iterator = list.iterator();
        iterator.forEachRemaining(arr -> arr[0]++);
        System.out.println(list.get(0)[0]);
    }
}
