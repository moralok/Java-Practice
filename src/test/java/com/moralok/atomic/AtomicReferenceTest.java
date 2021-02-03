package com.moralok.atomic;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author moralok
 * @since 2021/2/3 6:25 下午
 */
class AtomicReferenceTest {

    @Test
    void testMethods() {
        Person old = new Person("old", 1);
        Person young = new Person("young", 2);
        AtomicReference<Person> personAtomicReference = new AtomicReference<>(old);
        old.setAge(2);
        boolean ret = personAtomicReference.compareAndSet(old, young);
        // 比较的是引用地址
        System.out.println("compare success: " + ret + "person: " + personAtomicReference);
        ret = personAtomicReference.compareAndSet(old, young);
        System.out.println("compare failed: " + ret + "person: " + personAtomicReference);
    }
}
