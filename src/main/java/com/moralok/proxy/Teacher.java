package com.moralok.proxy;

/**
 * @author moralok
 * @since 2021/1/6
 */
public class Teacher implements Worker, Runner, NoProxy {
    @Override
    public void work() {
        System.out.println("Teacher works...");
    }

    @Override
    public void run() {
        System.out.println("Teacher runs...");
    }

    @Override
    public void doWithoutProxy() {
        System.out.println("Teacher does without proxy...");
    }
}
