package com.moralok.atomic;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author moralok
 * @since 2021/2/3 6:15 下午
 */
class AtomicIntegerArrayTest {

    @Test
    void testMethods() {
        int value = 0;
        int[] nums = new int[] {1, 2, 3, 4, 5, 6};
        AtomicIntegerArray array = new AtomicIntegerArray(nums);
        for (int i = 0; i < array.length(); i++) {
            System.out.print(array.get(i) + " ");
        }
        System.out.println();
        value = array.getAndSet(0, 5);
        System.out.println("value: " + value + "; array = " + array);
        value = array.getAndIncrement(0);
        System.out.println("value: " + value + "; array = " + array);
        value = array.getAndAdd(0, 5);
        System.out.println("value: " + value + "; array = " + array);
    }
}
