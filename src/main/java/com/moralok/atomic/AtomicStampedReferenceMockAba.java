package com.moralok.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 模拟ABA问题
 * AtomicMarkableReference只关心是否修改过，不关心修改过几次
 *
 * @author moralok
 * @since 2021/2/4 10:19 上午
 */
public class AtomicStampedReferenceMockAba {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("t1 start");
            atomicInteger.compareAndSet(0, 1);
            atomicInteger.compareAndSet(1, 0);
            System.out.println("t1 end, atomicInteger: " + atomicInteger);
        });
        Thread t2 = new Thread(() -> {
            System.out.println("t2 start");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2 sleep end");
            boolean ret = atomicInteger.compareAndSet(0, 2);
            System.out.println("AtomicInteger t2 update: " + ret);
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Thread t3 = new Thread(() -> {
            System.out.println("t3 start");
            int stamp = atomicStampedReference.getStamp();
            atomicStampedReference.compareAndSet(100, 1, stamp, stamp + 1);
            System.out.println("t3 process, atomicStampedReference: " + atomicStampedReference.getReference() + " " + atomicStampedReference.getStamp());
            stamp = atomicStampedReference.getStamp();
            atomicStampedReference.compareAndSet(1, 100, stamp, stamp + 1);
            System.out.println("t3 end, atomicStampedReference: " + atomicStampedReference.getReference() + " " + atomicStampedReference.getStamp());
        });
        Thread t4 = new Thread(() -> {
            System.out.println("t4 start");
            int stamp = atomicStampedReference.getStamp();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t4 sleep end");
            boolean ret = atomicStampedReference.compareAndSet(100, 1, stamp, stamp + 1);
            System.out.println("AtomicStampedReference t4 update: " + ret + " atomicStampedReference: " +  + atomicStampedReference.getReference() + " " + atomicStampedReference.getStamp());
        });
        t3.start();
        t4.start();
    }
}
