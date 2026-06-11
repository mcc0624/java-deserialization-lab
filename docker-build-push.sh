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

APPS_IMAGE="${REGISTRY}/${NAMESPACE}/java-deser-apps:${TAG}"
WEB_IMAGE="${REGISTRY}/${NAMESPACE}/java-deser-web:${TAG}"

echo -e "${GREEN}=============================================${NC}"
echo -e "${GREEN}  构建并推送镜像到阿里云 ACR（成都）${NC}"
echo -e "${GREEN}=============================================${NC}"
echo -e "  Tomcat: ${APPS_IMAGE}"
echo -e "  Apache: ${WEB_IMAGE}"

if ! docker info >/dev/null 2>&1; then
    echo -e "${RED}Docker 未运行${NC}"; exit 1
fi

echo -e "\n${YELLOW}[1/4] 构建 Tomcat 镜像 (Maven 编译 + WAR 打包)...${NC}"
docker build -t "${APPS_IMAGE}" .

echo -e "\n${YELLOW}[2/4] 构建 Apache 镜像...${NC}"
docker build -t "${WEB_IMAGE}" -f Dockerfile.apache .

echo -e "\n${YELLOW}[3/4] 推送 Tomcat 镜像...${NC}"
docker push "${APPS_IMAGE}"

echo -e "\n${YELLOW}[4/4] 推送 Apache 镜像...${NC}"
docker push "${WEB_IMAGE}"

echo -e "\n${GREEN}=============================================${NC}"
echo -e "${GREEN}  ✅ 推送完成！${NC}"
echo -e "${GREEN}  ${APPS_IMAGE}${NC}"
echo -e "${GREEN}  ${WEB_IMAGE}${NC}"
echo -e "${GREEN}=============================================${NC}"

# 生成学员用的 docker-compose.student.yml
cat > docker-compose.student.yml << EOF
# ============================================================
# Java 反序列化靶场 — 学员部署文件
# 用法: docker compose -f docker-compose.student.yml up -d
# 访问: http://localhost:81
# ============================================================
networks:
  my_custom_network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.20.0.0/24
          gateway: 172.20.0.1

services:
  javaser-apps:
    image: ${APPS_IMAGE}
    container_name: app-multi
    environment:
      CATALINA_OPTS: "-Djdk.xml.enableTemplatesImplDeserialization=true"
    restart: always
    networks:
      my_custom_network:
        ipv4_address: 172.20.0.10
    ulimits:
      nofile:
        soft: 65536
        hard: 65536

  apache:
    image: ${WEB_IMAGE}
    container_name: web
    restart: always
    ports:
      - "81:80"
    depends_on:
      - javaser-apps
    networks:
      my_custom_network:
        ipv4_address: 172.20.0.5
EOF

echo -e "\n${GREEN}已生成学员部署文件: docker-compose.student.yml${NC}"
echo -e "发给学员后，学员只需:"
echo -e "  docker compose -f docker-compose.student.yml up -d"
echo -e "然后浏览器访问 http://localhost:81"
