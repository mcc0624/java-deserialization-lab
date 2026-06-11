package com.ctfstu.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * 可配置黑名单的过滤 ObjectInputStream 基类。
 * 子类通过构造函数传入禁止的类名/包名前缀数组，resolveClass 时会逐个匹配。
 *
 * 匹配规则：
 *   - 如果禁止条目以 "." 结尾 → 按包名前缀匹配 (startsWith)
 *   - 否则 → 精确类名匹配 (equals)
 */
public class FilteringObjectInputStream extends ObjectInputStream {

    private final String[] forbiddenPatterns;

    public FilteringObjectInputStream(InputStream in, String[] forbiddenPatterns) throws IOException {
        super(in);
        this.forbiddenPatterns = forbiddenPatterns != null ? forbiddenPatterns : new String[0];
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();

        for (String pattern : forbiddenPatterns) {
            if (pattern.endsWith(".")) {
                // 包名前缀匹配 — 会匹配该包下的所有类
                if (className.startsWith(pattern)) {
                    throw new InvalidClassException("拒绝反序列化: 禁止的包路径 -> " + className);
                }
            } else {
                // 精确类名匹配
                if (className.equals(pattern)) {
                    throw new InvalidClassException("拒绝反序列化: 禁止的类 -> " + className);
                }
            }
        }

        return super.resolveClass(desc);
    }
}
