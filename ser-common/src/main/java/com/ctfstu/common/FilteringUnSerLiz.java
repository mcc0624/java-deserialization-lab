package com.ctfstu.common;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 带黑名单过滤的反序列化工具类
 * 用于需要 FilteringObjectInputStream 的挑战（Class15-20）
 */
public class FilteringUnSerLiz {

    public static Object unser(String filename, String[] forbiddenPatterns)
            throws IOException, ClassNotFoundException {
        try (FilteringObjectInputStream fis =
                     new FilteringObjectInputStream(new FileInputStream(filename), forbiddenPatterns)) {
            return fis.readObject();
        }
    }
}
