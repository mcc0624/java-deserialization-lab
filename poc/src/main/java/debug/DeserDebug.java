package debug;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * 本地反序列化调试工具
 *
 * 功能：使用与靶场完全相同的 ObjectInputStream 反序列化 .ser 文件，
 * 不经过 Web 上传，直接本地触发 readObject / 反序列化逻辑。
 *
 * 用于：
 * - 调试 payload 是否正确
 * - 理解反序列化过程中每一步的触发机制
 * - 对比不同 payload 的行为差异
 *
 * 用法：
 *   mvn exec:java -Dexec.mainClass="debug.DeserDebug" -Dexec.args="class08.ser"
 *
 * 或在 IDEA 中 Run → Edit Configurations → Program arguments 填入 class08.ser
 */
public class DeserDebug {

    public static void main(String[] args) throws Exception {
        // ---- JDK 版本提示 ----
        String jdkVer = System.getProperty("java.version");
        boolean isJdk8 = jdkVer.startsWith("1.8");

        // ---- 参数解析 ----
        if (args.length < 1) {
            System.err.println("用法: java DeserDebug <序列化文件路径>");
            System.err.println("示例: java DeserDebug class05.ser");
            System.err.println("      mvn exec:java -Dexec.mainClass=\"debug.DeserDebug\" -Dexec.args=\"class05.ser\"");
            System.exit(1);
        }

        String filename = args[0];
        System.out.println("╔═══════════════════════════════════════════════");
        System.out.println("║  🔍 反序列化调试工具 DeserDebug");
        System.out.println("║  📄 文件: " + filename);
        System.out.println("║  ☕ JDK 版本: " + jdkVer + (isJdk8 ? " (靶场环境)" : ""));
        System.out.println("╚═══════════════════════════════════════════════");
        System.out.println();

        if (!isJdk8) {
            System.out.println("⚠️  当前 JDK 版本高于 1.8，部分 payload（class06 Javassist、");
            System.out.println("    class12 TemplatesImpl）可能因模块系统限制无法本地触发。");
            System.out.println("    这些 payload 在 Docker 靶场（JDK 1.8）中正常运行。");
            System.out.println();
        }

        // ---- 反序列化（与靶场逻辑完全一致） ----
        long start = System.currentTimeMillis();

        Object obj = null;
        try {
            obj = unser(filename);
        } catch (Exception e) {
            System.out.println("❌ 反序列化失败: " + e.getClass().getSimpleName());
            System.out.println("   原因: " + e.getMessage());
            System.out.println();
            System.out.println("📌 常见原因排查:");
            System.out.println("   • 文件路径不正确 → 检查文件是否存在");
            System.out.println("   • serialVersionUID 不匹配 → 确认 POC 类版本与靶场一致");
            if (!isJdk8) {
                System.out.println("   • JDK 模块限制 → Javassist/TemplatesImpl 需 JDK 1.8");
            }
            System.out.println("   • 依赖缺失 → 需要对应 jar 包（如 commons-collections）");
            System.exit(1);
        }

        long elapsed = System.currentTimeMillis() - start;

        // ---- 输出结果 ----
        System.out.println("✅ 反序列化成功 (耗时: " + elapsed + "ms)");
        System.out.println();
        System.out.println("┌─ 反序列化结果 ──────────────────────────────");
        System.out.println("│ 类名: " + obj.getClass().getName());
        System.out.println("│ 对象: " + obj.toString());
        System.out.println("└────────────────────────────────────────────");

        // ---- 额外调试信息 ----
        String className = obj.getClass().getName();
        if (className.contains("Transformer") || className.contains("LazyMap")
            || className.contains("AnnotationInvocationHandler")
            || className.contains("BadAttributeValueExpException")) {
            System.out.println();
            System.out.println("💡 CC 链组件 payload，仅包含单个 Transformer 对象。");
            System.out.println("   完整的 CC 链利用（class14-20）需要组合多个组件。");
        } else if (className.contains("Student")) {
            System.out.println();
            System.out.println("💡 Student 对象包含自定义 readObject() 方法，");
            System.out.println("   反序列化时已自动触发其中逻辑。");
        }

        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════");
        System.out.println("║  ✅ 调试完成");
        System.out.println("╚═══════════════════════════════════════════════");
    }

    /**
     * 标准反序列化 —— 与靶场 UnSerLiz.unser() 逻辑完全一致
     */
    public static Object unser(String filename) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return ois.readObject();
        }
    }
}
