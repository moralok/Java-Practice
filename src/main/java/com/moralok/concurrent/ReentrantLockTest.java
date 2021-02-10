package com.moralok.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author moralok
 * @since 2021/2/10
 */
public class ReentrantLockTest {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Thread[] threads = new Thread[500];
        // 要出现覆盖真不容易
        for (int i = 0; i < 500; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 5000; j++) {
                    counter.add(1);
                }
            });
            threads[i] = thread;
        }
        for (Thread t : threads) {
            t.start();
        }
        TimeUnit.SECONDS.sleep(20);

        System.out.println(counter.getCount());
    }

    static class Counter {

        private int count;

        private ReentrantLock lock = new ReentrantLock();

        void add(int n) {
            lock.lock();
            try {
                count += n;
            } finally {
                lock.unlock();
            }
        }

        int getCount() {
            return count;
        }
    }
}
