package com.moralok.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author moralok
 * @since 2021/2/4 2:26 下午
 */
public class ThreadPoolExecutorDemo {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POLL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 16;
    private static final Long KEEP_ALIVE_TIME = 1L;
    private static final int JOB_COUNT = 50;

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POLL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < JOB_COUNT; i++) {
            MyRunnable myRunnable = new MyRunnable("run " + i);
            executor.execute(myRunnable);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {

        }
        System.out.println("Finished all threads");

    }
}
