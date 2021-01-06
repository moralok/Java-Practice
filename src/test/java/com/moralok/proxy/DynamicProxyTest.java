package com.moralok.proxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 动态代理测试
 *
 * @author moralok
 * @since 2021/1/6
 */
public class DynamicProxyTest {

    @Test
    void dynamicProxyTest() {
        Teacher teacher = new Teacher();
        TeacherHandler teacherHandler = new TeacherHandler(teacher);
        Object proxy = teacherHandler.getProxy();
        Worker worker = (Worker) proxy;
        worker.work();
        Runner runner = (Runner) proxy;
        runner.run();
        try {
            // 不能转换为未代理的接口
            NoProxy noProxy = (NoProxy) proxy;
            noProxy.doWithoutProxy();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof ClassCastException);
        }
        try {
            // 不能调用虽然指定要代理但是真实对象未实现的接口的方法
            NoImplement noImplement = (NoImplement) proxy;
            noImplement.noImplement();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
        }
    }
}
