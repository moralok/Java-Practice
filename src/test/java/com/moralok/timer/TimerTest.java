package com.moralok.timer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author moralok
 * @since 2021/1/7 11:26 上午
 */
public class TimerTest {

    @Test
    void sleepTest() throws InterruptedException {
        final long timeInternal = 3000;
        final int[] i = {0};
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + "...每隔3秒打印一次 " + i[0]);
                i[0]++;
                if (i[0] == 10) {
                    break;
                }
                try {
                    Thread.sleep(timeInternal);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
        System.out.println("主线程结束");

    }

    @Test
    void timerTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("A打印 count " + countDownLatch.getCount() + " 当前时间 " + LocalDateTime.now());
                countDownLatch.countDown();
            }
        }, 0, 2000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("B每次耗时3秒打印 count " + countDownLatch.getCount() + " 当前时间 " + LocalDateTime.now());
                countDownLatch.countDown();
            }
        }, 0, 6000);
        countDownLatch.await();
        System.out.println("主线程结束");
    }

    @Test
    void scheduledThreadPollExecutorTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(3);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            System.out.println("每隔1秒打印一次");
            countDownLatch.countDown();
        },1 ,1, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            System.out.println("每隔3秒打印一次");
            countDownLatch.countDown();
        }, 1, 3, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.schedule(() -> {
            System.out.println("延迟5秒打印");
            countDownLatch.countDown();
        }, 5, TimeUnit.SECONDS);
        countDownLatch.await();
        System.out.println("主线程结束");
    }
}
