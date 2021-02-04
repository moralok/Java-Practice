package com.moralok.concurrent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author moralok
 * @since 2021/2/4 4:58 下午
 */
public class CallableDemo {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POLL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;
    private static final int JOB_COUNT = 50;

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POLL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());
        List<Future<String>> futureList = new ArrayList<>();
        Callable<String> callable = new MyCallable();
        for (int i = 0; i < JOB_COUNT; i++) {
            Future<String> future = executor.submit(callable);
            futureList.add(future);
        }
        for (Future<String> future : futureList) {
            try {
                System.out.println(LocalDateTime.now() + " : " + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }
}
