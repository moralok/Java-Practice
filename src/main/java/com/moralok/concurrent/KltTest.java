package com.moralok.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author moralok
 * @since 2021/2/9
 */
public class KltTest {

    public static void main(String[] args) {
        // 通过任务管理器查看内核是否对Java线程有感知
        // 即Java线程是用户级线程还是内核级线程
        // 目前市面上的JVM基本都是使用KLT
        for (int i = 0; i < 300; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
