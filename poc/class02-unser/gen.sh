#!/bin/bash
# class02 — 基础反序列化（Student.toString）
cd "$(dirname "$0")"

# 编译
javac -cp "../lib/commons-collections-3.2.1.jar" \
  Student.java GenPayload.java -d .

# 运行
java -cp ".:../lib/commons-collections-3.2.1.jar" class02.GenPayload

# 清理 class
mv class02.ser .. 2>/dev/null
rm -rf class02
echo "🎯 上传 class02.ser 到 http://localhost:81/class02/"
