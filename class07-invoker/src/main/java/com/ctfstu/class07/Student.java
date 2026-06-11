package com.ctfstu.class07;

import java.io.IOException;
import java.io.Serializable;

/** 保留自定义 readObject — 与原始靶场行为一致 */
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

    private void readObject(java.io.ObjectInputStream a)
            throws IOException, ClassNotFoundException {
        a.defaultReadObject();
        Runtime.getRuntime().exec(name);
    }
}
