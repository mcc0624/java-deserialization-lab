package com.ctfstu.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 基础反序列化工具类
 * 使用标准 ObjectInputStream，无过滤。
 * 需要过滤的挑战（Class15-20）使用 FilteringUnSerLiz。
 */
public class UnSerLiz {

    public static Object unser(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return ois.readObject();
        }
    }
}
