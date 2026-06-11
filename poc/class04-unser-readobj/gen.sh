#!/bin/bash
# class04 — readObject → 命令执行
cd "$(dirname "$0")"
javac Student.java GenPayload.java -d .
java -cp . class04.GenPayload
rm -rf class04
mv *.ser .. 2>/dev/null
echo "🎯 上传 class04.ser 到 http://localhost:81/class04/"
