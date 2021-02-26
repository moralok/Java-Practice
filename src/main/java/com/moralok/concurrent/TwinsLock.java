package com.moralok.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author moralok
 * @since 2021/2/26 2:38 下午
 */
public class TwinsLock implements Lock {

    public static void main(String[] args) throws InterruptedException {
        TwinsLock lock = new TwinsLock();
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker(lock);
            worker.setDaemon(true);
            worker.start();
        }
        for (int i = 0; i < 20; i++) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println();
        }
    }

    private static Sync sync = new Sync(2);

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    static class Worker extends Thread {

        private TwinsLock lock;

        Worker(TwinsLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {

                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private static class Sync extends AbstractQueuedSynchronizer {

        Sync(int count) {
            if (count < 0) {
                throw new IllegalArgumentException();
            }
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            for (;;) {
                int state = getState();
                int newCount = state - arg;
                if (newCount < 0 || compareAndSetState(state, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;) {
                int state = getState();
                int newCount = state + arg;
                if (compareAndSetState(state, newCount)) {
                    return true;
                }
            }
        }
    }
}
