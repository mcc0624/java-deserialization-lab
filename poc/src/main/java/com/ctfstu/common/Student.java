package com.ctfstu.common;

import java.io.Serializable;

/**
 * 基础 Student POJO — 与 class03 服务端一致
 * 靶场 handler 调用 getName() 获取命令字符串后执行 Runtime.exec()
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

    @Override
    public String toString() {
        return "Student{name = " + name + ", age = " + age + "}";
    }
}
