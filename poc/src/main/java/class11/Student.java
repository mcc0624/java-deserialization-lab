package class11;

import java.io.Serializable;

/**
 * class11 服务端使用的 Student
 * 构造器中执行 Runtime.exec(name)，InstantiateTransformer 反射创建实例时触发
 */
public class Student implements Serializable {
    private String name;
    private int age;

    public Student() {}
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        try {
            // 构造器中执行命令
            Runtime.getRuntime().exec(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
