package com.ctfstu.class05;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * class05 服务端使用的 Student — 与 server 完全一致
 * 反射版 readObject()，通过 Class.forName + getMethod + invoke 执行命令
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

    /** 反射版 readObject — 通过反射调用链执行命令 */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        try {
            Class<?> runtimeClass = Class.forName("java.lang.Runtime");
            Method getRuntimeMethod = runtimeClass.getMethod("getRuntime");
            Object runtimeObj = getRuntimeMethod.invoke(null);
            Method execMethod = runtimeClass.getMethod("exec", String.class);
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
