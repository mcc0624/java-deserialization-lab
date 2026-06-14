package com.ctfstu.class06;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * class06 服务端使用的 Student — 与 server 完全一致
 * Javassist 版 readObject，运行时动态生成字节码，加载类触发静态块执行命令
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

    /** Javassist 版 readObject — 动态生成字节码并加载执行 */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.makeClass("Evil" + System.nanoTime());
            String execCmd = name.replace("\"", "\\\"").replace("\\", "\\\\");
            String cmd = "java.lang.Runtime.getRuntime().exec(\"" + execCmd + "\");";
            cc.makeClassInitializer().insertBefore(cmd);
            Class<?> evilClass = cc.toClass();
            // newInstance() 触发类初始化 → 执行静态代码块
            evilClass.newInstance();
        } catch (Exception e) {
            String detail = e.getClass().getName() + ": " + e.getMessage();
            if (e.getCause() != null) {
                detail += " => " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage();
            }
            throw new RuntimeException("Javassist 字节码注入失败: " + detail, e);
        }
    }

    @Override
    public String toString() {
        return "Student{name = " + name + ", age = " + age + "}";
    }
}
