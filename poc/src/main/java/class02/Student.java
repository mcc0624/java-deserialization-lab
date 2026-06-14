package class02;

import java.io.Serializable;

/**
 * class02 服务端使用的 Student — 演示 toString() 在反序列化中的触发
 */
public class Student implements Serializable {
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
