package com.moralok.concurrent;

/**
 * @author moralok
 * @since 2021/2/24 3:52 下午
 */
public class ThreadLocalHashCodeTest {

    public static void main(String[] args) {
        // 测试该特定斐波那契数的n倍在哈希函数运算后的均匀性，牛！
        int len = 16;
        final int HASH_INCREMENT = 0x61c88647;
        for (int i = 0; i < 16; i++) {
            int threadLocalHashCode = i * HASH_INCREMENT + HASH_INCREMENT;
            int idx = threadLocalHashCode & (len - 1);
            System.out.println(idx);
        }
    }
}
