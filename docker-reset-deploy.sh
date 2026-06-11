#!/bin/bash
set -euo pipefail

# ============================================================
# Docker 容器清理与重新部署脚本
# 功能：停止并删除所有运行中的 Docker 容器，
#       然后通过当前目录的 docker-compose.yml 部署服务
# ============================================================

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 获取宿主机在局域网中的出口 IP
get_host_ip() {
    if command -v ip &> /dev/null; then
        ip route get 8.8.8.8 2>/dev/null | awk '{print $7; exit}'
    elif command -v route &> /dev/null; then
        route -n get default 2>/dev/null | grep 'interface:' -A1 | tail -1 | awk '{print $2}'
    else
        echo "无法自动获取宿主机IP，请手动设置 RMIIP 环境变量" >&2
        exit 1
    fi
}

# 检查 Docker 是否正在运行
check_docker_running() {
    if ! docker info >/dev/null 2>&1; then
        echo -e "${RED}[错误] Docker 服务未运行，请先启动 Docker！${NC}"
        exit 1
    fi
}

# 展示当前运行的 Docker 容器
list_running_containers() {
    echo -e "\n${YELLOW}[步骤1/6] 正在列出当前运行的 Docker 容器...${NC}"
    running_containers=$(docker ps --format "{{.Names}} (ID: {{.ID}})")
    if [ -z "$running_containers" ]; then
        echo -e "${GREEN}  没有正在运行的 Docker 容器${NC}"
    else
        echo -e "  运行中的容器："
        echo "$running_containers"
    fi
}

# 停止所有运行的 Docker 容器
stop_all_containers() {
    echo -e "\n${YELLOW}[步骤2/6] 正在停止所有运行的 Docker 容器...${NC}"
    container_ids=$(docker ps -q)
    if [ -n "$container_ids" ]; then
        docker stop $container_ids
        echo -e "${GREEN}  所有运行的容器已成功停止${NC}"
    else
        echo -e "${GREEN}  没有需要停止的容器${NC}"
    fi
}

# 删除所有已停止的 Docker 容器
remove_all_containers() {
    echo -e "\n${YELLOW}[步骤3/6] 正在删除所有已停止的 Docker 容器...${NC}"
    stopped_container_ids=$(docker ps -a -q)
    if [ -n "$stopped_container_ids" ]; then
        docker rm $stopped_container_ids
        echo -e "${GREEN}  所有已停止的容器已成功删除${NC}"
    else
        echo -e "${GREEN}  没有需要删除的容器${NC}"
    fi
}

# 清理冲突的 Docker 网络
cleanup_network() {
    echo -e "\n${YELLOW}[步骤4/6] 正在清理冲突的 Docker 网络...${NC}"

    # 方案1：直接删除 compose 生成的固定网络名
    compose_network="java-deserialization-lab_my_custom_network"
    if docker network ls --format "{{.Name}}" | grep -q "^${compose_network}$"; then
        echo -e "  正在删除网络: ${compose_network}"
        containers=$(docker network inspect "$compose_network" --format '{{range .Containers}} {{.Name}} {{end}}' 2>/dev/null || true)
        for container in $containers; do
            [ -n "$container" ] && docker network disconnect -f "$compose_network" "$container" 2>/dev/null
        done
        docker network rm "$compose_network" 2>/dev/null \
            && echo -e "  ${GREEN}网络 $compose_network 已删除${NC}" \
            || echo -e "  ${YELLOW}网络 $compose_network 删除失败（可能不存在或已被清理）${NC}"
    else
        echo -e "  ${GREEN}网络 $compose_network 不存在，无需清理${NC}"
    fi

    # 方案2：清理当前目录名开头的所有相关网络（兜底）
    dir_name=$(basename "$(pwd)")
    all_networks=$(docker network ls --format "{{.Name}}" | grep "${dir_name}" || true)
    if [ -n "$all_networks" ]; then
        echo -e "  正在检查残留网络..."
        for net in $all_networks; do
            if docker network ls --format "{{.Name}}" | grep -q "^${net}$"; then
                echo -e "  清理残留网络: ${net}"
                containers=$(docker network inspect "$net" --format '{{range .Containers}} {{.Name}} {{end}}' 2>/dev/null || true)
                for container in $containers; do
                    [ -n "$container" ] && docker network disconnect -f "$net" "$container" 2>/dev/null
                done
                docker network rm "$net" 2>/dev/null || echo -e "  ${YELLOW}残留网络 $net 清理失败${NC}"
            fi
        done
    fi

    echo -e "${GREEN}  网络清理完成${NC}"
}

# 部署前检查 WAR 文件是否存在
check_war_files() {
    echo -e "\n${YELLOW}[步骤5/6] 正在检查 WAR 文件...${NC}"
    missing=0
    for war in \
        class01-runtime/target/class01.war \
        class02-unser/target/class02.war \
        class03-unser-cmd/target/class03.war \
        class04-unser-readobj/target/class04.war \
        class07-invoker/target/class07.war \
        class08-constant/target/class08.war \
        class09-chained/target/class09.war \
        class10-lazymap/target/class10.war \
        class11-instantiate/target/class11.war \
        class12-templates/target/class12.war \
        class13-transformedmap/target/class13.war \
        class14-cc1/target/class14.war \
        class15-cc2/target/class15.war \
        class16-cc3/target/class16.war \
        class17-cc4/target/class17.war \
        class18-cc5/target/class18.war \
        class19-cc6/target/class19.war \
        class20-cc7/target/class20.war; do
        if [ ! -f "$war" ]; then
            echo -e "  ${RED}缺失: $war${NC}"
            missing=1
        fi
    done
    if [ "$missing" -eq 1 ]; then
        echo -e "${RED}  错误: 存在缺失的 WAR 文件，请先执行 mvn clean package${NC}"
        exit 1
    fi
    echo -e "${GREEN}  所有 WAR 文件已就绪${NC}"
}

# 通过 docker-compose 部署服务
deploy_with_compose() {
    echo -e "\n${YELLOW}[步骤6/6] 正在通过 docker-compose.yml 部署服务...${NC}"
    if [ ! -f "docker-compose.yml" ]; then
        echo -e "${RED}[错误] 当前目录不存在 docker-compose.yml 文件！${NC}"
        exit 1
    fi

    if docker-compose up -d; then
        echo -e "${GREEN}  Docker Compose 服务部署成功！${NC}"
        echo -e "\n${YELLOW}当前运行的容器状态：${NC}"
        docker ps --format "{{.Names}} (状态: {{.Status}})"
    else
        echo -e "${RED}[错误] Docker Compose 服务部署失败！${NC}"
        exit 1
    fi
}

# 主流程
main() {
    echo -e "${GREEN}=============================================${NC}"
    echo -e "${GREEN}  Docker 容器清理与重新部署脚本 ${NC}"
    echo -e "${GREEN}=============================================${NC}"

    check_docker_running
    list_running_containers
    stop_all_containers
    remove_all_containers
    cleanup_network
    check_war_files
    deploy_with_compose

    echo -e "\n${GREEN}=============================================${NC}"
    echo -e "${GREEN}  操作完成！靶场已部署 ${NC}"
    echo -e "${GREEN}  访问 http://localhost:81 进入靶场 ${NC}"
    echo -e "${GREEN}=============================================${NC}"
}

main
