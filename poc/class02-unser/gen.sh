#!/bin/bash
# class02 — 基础反序列化（无需自定义类，用 HashMap）
cd "$(dirname "$0")"
javac GenPayload.java -d .
java GenPayload
rm -rf GenPayload*.class
mv class02.ser .. 2>/dev/null
echo "🎯 上传 class02.ser 到 http://localhost:81/class02/"
