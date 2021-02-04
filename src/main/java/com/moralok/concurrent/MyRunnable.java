package com.moralok.concurrent;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author moralok
 * @since 2021/2/4 2:23 下午
 */
public class MyRunnable implements Runnable {

    private String command;

    public MyRunnable(String s) {
        command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start time = " + LocalDateTime.now());
        processCommand();
        System.out.println(Thread.currentThread().getName() + " end time = " + LocalDateTime.now());
    }

    private void processCommand() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MyRunnable{" +
                "command='" + command + '\'' +
                '}';
    }
}
