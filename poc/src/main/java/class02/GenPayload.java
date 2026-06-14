package class02;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * 生成 class02 的 payload — 基础反序列化
 * 目标: 上传任意可序列化对象，触发 toString() 显示内容
 * 靶场: /class02/upload
 *
 * 注意: 服务器只调 obj.toString()，不检查类型，所以用 HashMap 最稳
 */
public class GenPayload {
    public static void main(String[] args) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("msg", "反序列化成功！");

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("class02.ser"));
        oos.writeObject(map);
        oos.close();

        System.out.println("✅ 已生成: class02.ser");
    }
}
