package com.moralok.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author moralok
 * @since 2021/1/28 11:19 上午
 */
public class IterableTest {

    private List<Integer> integers;

    @BeforeEach
    void setUp() {
        integers = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            integers.add(i);
        }
    }

    @Test
    void testIterator() {
        // 经典遍历方式
        System.out.println("经典遍历方式");
        Iterator<Integer> iterator = integers.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
        System.out.println("foreach遍历");
        // foreach遍历
        for (Integer integer : integers) {
            System.out.print(integer + " ");
        }

    }

    @Test
    void testForEach() {
        integers.forEach(integer -> {
            System.out.print(integer + " ");
        });
        System.out.println();
        integers.forEach(integer -> integer *= 2);
        integers.forEach(integer -> {
            System.out.print(integer + " ");
        });
        System.out.println();
        System.out.println("自定义Consumer");
        try {
            integers.forEach(new MyConsumer());
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("模拟异常抛出"));
        }
    }

    @Test
    void testSpliterator() {
        Spliterator<Integer> spliterator = integers.spliterator();
        // 目前没用过
        spliterator.forEachRemaining(new MyConsumer());
    }

    static class MyConsumer implements Consumer<Integer> {

        @Override
        public void accept(Integer integer) {
            if (integer >= 50) {
                throw new RuntimeException("模拟异常抛出，integer = " + integer);
            }
            System.out.print(2 * integer + " ");
        }
    }
}
