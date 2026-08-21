#!/bin/bash
set -euo pipefail

RED='\e[0;31m'
GREEN='\e[0;32m'
YELLOW='\e[1;33m'
NC='\e[0m'

check_docker() {
    if ! docker info >/dev/null 2>&1; then
        echo -e "[错误] Docker 未运行"
        exit 1
    fi
    if ! docker compose version >/dev/null 2>&1; then
        echo -e "[错误] 需要 Docker Compose"
        exit 1
    fi
}

main() {
    echo -e "============================================="
    echo -e "  Java 反序列化靶场 — 一键部署脚本"
    echo -e "============================================="

    check_docker

    echo -e "\n[1/2] 拉取镜像..."
    docker compose pull

    echo -e "\n[2/2] 启动服务..."
    docker compose down 2>/dev/null || true
    docker compose up -d

    echo -e "\n============================================="
    echo -e "  ✅ 部署完成！"
    echo -e "  访问 http://localhost:81 进入靶场"
    echo -e "============================================="
    echo -e "\n等待服务启动..."
    sleep 3
    docker compose ps
}

main