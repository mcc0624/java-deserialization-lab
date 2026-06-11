package com.ctfstu.common;

import java.io.Serializable;

/**
 * 基础 Student POJO — 所有模块共享
 * 不含自定义 readObject()，保持纯净。
 * 需要自定义 readObject 的挑战（Class04, Class07）在自己的模块中定义子类或独立版本。
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;

    public Student() {
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{name = " + name + ", age = " + age + "}";
    }
}
