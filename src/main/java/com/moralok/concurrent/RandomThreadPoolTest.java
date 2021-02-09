package com.moralok.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author moralok
 * @since 2021/2/9
 */
public class RandomThreadPoolTest {

    public static void main(String[] args) {
        // 线程池执行的随机性测试
        // 不能通过预估线程开始执行的顺序
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 3, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.execute(new MyRunnable(i));
        }
    }

    static class MyRunnable implements Runnable {

        private int i;

        MyRunnable(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            System.out.println("执行当前任务的线程是 " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Runnable " + i + " is processing");
        }
    }
}
