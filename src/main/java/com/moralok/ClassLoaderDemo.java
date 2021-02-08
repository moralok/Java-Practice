package com.moralok;

/**
 * @author moralok
 * @since 2021/2/8
 */
public class ClassLoaderDemo {

    public static void main(String[] args) {
        System.out.println("ClassLoaderDemo's ClassLoader is " + ClassLoaderDemo.class.getClassLoader());
        System.out.println("The Parent of ClassLoaderDemo's ClassLoader is " + ClassLoaderDemo.class.getClassLoader().getParent());
        // null并不代表ExtClassLoader没有父类加载器，而是 BootstrapClassLoader
        System.out.println("The GrandParent of ClassLoaderDemo's ClassLoader is " + ClassLoaderDemo.class.getClassLoader().getParent().getParent());
    }
}
