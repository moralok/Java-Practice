package com.moralok.atomic;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author moralok
 * @since 2021/2/4 10:50 上午
 */
class AtomicIntegerFieldUpdaterTest {

    @Test
    void testUpdate() {
        Person person = new Person("person", 20, 1);
        AtomicIntegerFieldUpdater<Person> age = AtomicIntegerFieldUpdater.newUpdater(Person.class, "id");
        boolean ret = age.compareAndSet(person, 1, 2);
        System.out.println("ret: " + ret + " person: " + person);
    }
}
