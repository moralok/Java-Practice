package com.moralok.proxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 动态代理测试
 *
 * @author moralok
 * @since 2021/1/6
 */
public class DynamicProxyTest {

    private Teacher teacher;

    private TeacherHandler teacherHandler;

    private Object proxy;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacherHandler = new TeacherHandler(teacher);
        proxy = teacherHandler.getProxy();
    }

    @Test
    void dynamicProxyTest() {
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

    @Test
    void proxyClassTest() {
        // 调用的处理程序相同
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
        Assertions.assertSame(invocationHandler, teacherHandler);
        // 相同的类加载器和指定接口数组可以生成同一个Class
        Class[] interfaces = new Class[] {Worker.class, Runner.class, NoImplement.class};
        Class<?> proxyClass = Proxy.getProxyClass(teacher.getClass().getClassLoader(), interfaces);
        Assertions.assertSame(proxy.getClass(), proxyClass);
        // 可以判断是否为代理类
        Assertions.assertTrue(Proxy.isProxyClass(proxyClass));
    }
}
