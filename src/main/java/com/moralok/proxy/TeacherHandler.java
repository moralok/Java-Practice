package com.moralok.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Teacher的代理对象对应的调用处理程序
 *
 * @author moralok
 * @since 2021/1/6
 */
public class TeacherHandler implements InvocationHandler {

    /**
     * 代理的真实对象
     */
    private Object obj;

    /**
     * 传入代理的真实对象
     * @param obj 真实对象
     */
    public TeacherHandler(Object obj) {
        this.obj = obj;
    }

    public Object getProxy() {
        Class clazz = this.obj.getClass();
        // 指定定义代理类的类加载器，使用真实对象的类加载器
        ClassLoader classLoader = clazz.getClassLoader();
        // 指定代理类需要实现的接口，这样就可以像调用真实对象一样调用代理对象的方法
        Class[] interfaces = new Class[] {Worker.class, Runner.class, NoImplement.class};
        // 第3个参数是对应的调用处理程序
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 可以在方法前后添加自己的操作，比如进行AOP
        System.out.println("Before invoke method..." + method.getName());
        Object invoke = method.invoke(obj, args);
        System.out.println("After invoke method..." + method.getName());
        return invoke;
    }
}
