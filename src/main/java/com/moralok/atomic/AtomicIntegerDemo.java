package com.moralok.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author moralok
 * @since 2021/2/3 6:08 下午
 */
public class AtomicIntegerDemo {

    /**
     * 保证count1的值是最新值
     */
    private volatile int count1 = 0;

    private AtomicInteger count2 = new AtomicInteger(0);

    public synchronized void increment1() {
        // 多线程环境不使用原子类保证线程安全（基本数据类型）
        count1++;
    }

    public int getCount1() {
        return count1;
    }

    public void increment2() {
        // 多线程环境使用原子类保证线程安全（基本数据类型）
        count2.getAndIncrement();
    }

    public int getCount2() {
        return count2.get();
    }
}
