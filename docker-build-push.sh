#!/bin/bash
# ============================================================
# 构建并推送 Docker 镜像到阿里云 ACR（成都节点）
# 用法: ./docker-build-push.sh [tag]
#
# 默认示例: ./docker-build-push.sh v1.0
# 前置条件: docker login registry.cn-chengdu.aliyuncs.com
# ============================================================
set -euo pipefail

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; NC='\033[0m'

REGISTRY="crpi-4exq63xzgbecpu58.cn-chengdu.personal.cr.aliyuncs.com"
NAMESPACE="mcc0624"
TAG="${1:-latest}"

IMAGE="${REGISTRY}/${NAMESPACE}/java-deserialization-lab:${TAG}"

echo -e "${GREEN}=============================================${NC}"
echo -e "${GREEN}  构建并推送镜像到阿里云 ACR（成都）${NC}"
echo -e "${GREEN}=============================================${NC}"
echo -e "  镜像: ${IMAGE}"

if ! docker info >/dev/null 2>&1; then
    echo -e "${RED}Docker 未运行${NC}"; exit 1
fi

echo -e "\n${YELLOW}[1/2] 构建镜像 (Maven 编译 + WAR 打包 + 首页集成)...${NC}"
docker build -t "${IMAGE}" .

echo -e "\n${YELLOW}[2/2] 推送镜像...${NC}"
docker push "${IMAGE}"

echo -e "\n${GREEN}=============================================${NC}"
echo -e "${GREEN}  ✅ 推送完成！${NC}"
echo -e "${GREEN}  ${IMAGE}${NC}"
echo -e "${GREEN}=============================================${NC}"

# 生成学员用的 docker-compose.student.yml
cat > docker-compose.student.yml << EOF
# ============================================================
# Java 反序列化靶场 — 学员部署文件
# 用法: docker compose -f docker-compose.student.yml up -d
# 访问: http://localhost:81
# ============================================================
services:
  javaser-apps:
    image: ${IMAGE}
    container_name: javaserial-lab
    ports:
      - "81:8080"
    environment:
      CATALINA_OPTS: "-Djdk.xml.enableTemplatesImplDeserialization=true"
    restart: always
EOF

echo -e "\n${GREEN}已生成学员部署文件: docker-compose.student.yml${NC}"
echo -e "发给学员后，学员只需:"
echo -e "  docker compose -f docker-compose.student.yml up -d"
echo -e "然后浏览器访问 http://localhost:81"
