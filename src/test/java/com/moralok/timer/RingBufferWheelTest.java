package com.moralok.timer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author moralok
 * @since 2021/1/7 2:15 下午
 */
public class RingBufferWheelTest {

    @Test
    void ringBufferWheelTest() throws InterruptedException {
        // 额。。数量大时会有任务被吃掉
        // 最后一个要等一会，不知道为啥
        CountDownLatch countDownLatch = new CountDownLatch(20);
        RingBufferWheel ringBufferWheel = new RingBufferWheel( Executors.newFixedThreadPool(2));
        for (int i = 0; i < 20; i++) {
            RingBufferWheel.Task job = new Job(countDownLatch);
            job.setKey(i);
            ringBufferWheel.addTask(job);
        }
        countDownLatch.await();
        System.out.println("主方法结束");
    }

    public static class Job extends RingBufferWheel.Task{

        private CountDownLatch countDownLatch;

        public Job(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " print count=" + countDownLatch.getCount() + " taskId=" + getTaskId() + " key=" + getKey() + " circleNum=" + getCycleNum() + " index=" + getIndex());
            countDownLatch.countDown();
        }
    }
}
