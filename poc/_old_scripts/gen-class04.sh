#!/bin/bash
# class04 — readObject → 命令执行
cd "$(dirname "$0")"
javac src/com/ctfstu/class04/Student.java src/GenPayload.java -d out
java -cp out GenPayload
mv class04.ser .. 2>/dev/null
rm -rf out
echo "🎯 上传 class04.ser 到 http://localhost:81/class04/"
