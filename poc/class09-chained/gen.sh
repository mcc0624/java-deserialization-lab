#!/bin/bash
# class09 — ChainedTransformer
cd "$(dirname "$0")"
javac -cp "../lib/commons-collections-3.2.1.jar" GenPayload.java -d .
java -cp ".:../lib/commons-collections-3.2.1.jar" class09.GenPayload
rm -rf class09
mv *.ser .. 2>/dev/null
echo "🎯 上传 class09.ser 到 http://localhost:81/class09/"
