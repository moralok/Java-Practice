package com.moralok.atomic;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author moralok
 * @since 2021/2/3 5:59 下午
 */
class AtomicIntegerTest {

    /**
     * 常见方法使用
     */
    @Test
    void testMethods() {
        AtomicInteger i = new AtomicInteger(0);
        int value = 0;
        value = i.getAndSet(5);
        System.out.println("value: " + value + "; i = " + i);
        value = i.getAndIncrement();
        System.out.println("value: " + value + "; i = " + i);
        value = i.getAndAdd(5);
        System.out.println("value: " + value + "; i = " + i);
        boolean ret = i.compareAndSet(10, 12);
        System.out.println("compare failed: " + ret + " i = " + i);
        ret = i.compareAndSet(11, 12);
        System.out.println("compare success: " + ret + " i = " + i);
    }
}
