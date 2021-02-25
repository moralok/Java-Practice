package com.moralok.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.stream.IntStream;

/**
 * @author moralok
 * @since 2021/2/25 11:38 上午
 */
public class MyLockBaseOnAqs {

    private Sync sync = new Sync();

    private static int count;

    private void lock() {
        sync.acquire(1);
    }

    private void unlock() {
        sync.release(1);
    }

    public static void main(String[] args) throws InterruptedException {
        MyLockBaseOnAqs myLockBaseOnAqs = new MyLockBaseOnAqs();
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        IntStream.range(0, 1000).forEach(i -> new Thread(() -> {
            myLockBaseOnAqs.lock();
            try {
                IntStream.range(0, 10000).forEach(j -> count++);
            } finally {
                myLockBaseOnAqs.unlock();
            }
            countDownLatch.countDown();
        }, "tt-" + i).start());
        countDownLatch.await();
        System.out.println(count);
    }

    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
    }
}
