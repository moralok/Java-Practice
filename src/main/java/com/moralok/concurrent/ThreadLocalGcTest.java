package com.moralok.concurrent;

import java.lang.reflect.Field;

/**
 * @author moralok
 * @since 2021/2/24 3:30 下午
 */
public class ThreadLocalGcTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Test Before GC");
        Thread t = new Thread(() -> print("abc", false));
        t.start();
        t.join();
        System.out.println("Test After GC");
        Thread t2 = new Thread(() -> print("cde", true));
        t2.start();
        t2.join();
    }

    private static void print(String value, boolean isGc) {
        // getField public 包括继承
        // getDeclaredField 所有 不包括继承
        try {
            new ThreadLocal<String>().set(value);
            if (isGc) {
                System.gc();
            }
            Thread currentThread = Thread.currentThread();
            Class<? extends Thread> threadClazz = currentThread.getClass();
            Field threadLocalsField = threadClazz.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            Object threadLocals = threadLocalsField.get(currentThread);
            Class threadLocalMapClz = threadLocals.getClass();
            Field tableField = threadLocalMapClz.getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] table = (Object[]) tableField.get(threadLocals);
            for (Object e : table) {
                if (e != null) {
                    Class entryClz = e.getClass();
                    Field valueField = entryClz.getDeclaredField("value");
                    Field keyField = entryClz.getSuperclass().getSuperclass().getDeclaredField("referent");
                    valueField.setAccessible(true);
                    keyField.setAccessible(true);
                    System.out.println("key: " + keyField.get(e) + " value: " + valueField.get(e));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
