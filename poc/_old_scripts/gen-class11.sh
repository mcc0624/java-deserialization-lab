#!/bin/bash
# class11 — InstantiateTransformer
cd "$(dirname "$0")"
javac -cp "../lib/commons-collections-3.2.1.jar" Student.java GenPayload.java -d .
java -cp ".:../lib/commons-collections-3.2.1.jar" class11.GenPayload
rm -rf class11
mv *.ser .. 2>/dev/null
echo "🎯 上传 class11.ser 到 http://localhost:81/class11/"
