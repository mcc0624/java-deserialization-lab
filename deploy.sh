#!/bin/bash
set -euo pipefail

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

check_docker() {
    if ! docker info >/dev/null 2>&1; then
        echo -e "${RED}[错误] Docker 未运行${NC}"
        exit 1
    fi
    if ! docker compose version >/dev/null 2>&1; then
        echo -e "${RED}[错误] 需要 Docker Compose${NC}"
        exit 1
    fi
}

main() {
    echo -e "${GREEN}=============================================${NC}"
    echo -e "${GREEN}  Java 反序列化靶场 — 一键部署脚本${NC}"
    echo -e "${GREEN}=============================================${NC}"

    check_docker

    echo -e "\n${YELLOW}[1/3] 构建镜像...${NC}"
    docker compose build --no-cache 2>&1 | tail -3

    echo -e "\n${YELLOW}[2/3] 停止旧容器...${NC}"
    docker compose down 2>/dev/null || true

    echo -e "\n${YELLOW}[3/3] 启动服务...${NC}"
    docker compose up -d

    echo -e "\n${GREEN}=============================================${NC}"
    echo -e "${GREEN}  ✅ 部署完成！${NC}"
    echo -e "${GREEN}  访问 http://localhost:81 进入靶场${NC}"
    echo -e "${GREEN}=============================================${NC}"
    echo -e "\n${YELLOW}等待服务启动...${NC}"
    sleep 3
    docker compose ps
}

main
