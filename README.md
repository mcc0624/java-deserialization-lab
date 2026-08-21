# Java 反序列化漏洞靶场

基于 Commons Collections 链学习的 Java 反序列化漏洞靶场，包含 18 个渐进式挑战模块。

## 一键部署（推荐）

无需安装 JDK、Maven 或 ysoserial，仅需 Docker 环境。

### 前置条件

- Docker Engine 20.10+
- Docker Compose v2

### 部署命令

```bash
# 方式一：使用部署脚本
chmod +x deploy.sh && ./deploy.sh

# 方式二：使用 Docker Compose
docker compose pull
docker compose up -d

# 方式三：直接运行容器
docker run -d -p 81:8080 --name javaserial-lab \
  crpi-4exq63xzgbecpu58.cn-chengdu.personal.cr.aliyuncs.com/mcc0624/java-deserialization-lab:latest
```

### 访问靶场

打开浏览器：**http://localhost:81**

## 靶场模块

| 模块 | 学习主题 | 说明 |
|------|---------|------|
| class01 | 命令执行 | Runtime.exec() 基础命令执行 |
| class02 | 基础反序列化 | readObject() 与 toString() 行为 |
| class03 | 反序列化命令执行 | getter 方法触发命令执行 |
| class04 | 自定义 readObject | 反序列化自动执行代码 |
| class05 | 反射版 readObject | Class.forName + getMethod + invoke 替代直接 exec |
| class06 | Javassist 字节码注入 | 运行时动态生成字节码，加载类触发静态块 |
| class07 | InvokerTransformer | 反射调用任意方法 |
| class08 | ConstantTransformer | 固定常量返回 |
| class09 | ChainedTransformer | Transformer 链式调用 |
| class10 | LazyMap | 懒加载 Factory 模式 |
| class11 | InstantiateTransformer | 反射创建对象 |
| class12 | TemplatesImpl | 字节码加载执行 |
| class13 | TransformedMap | Map 条目自动变换 |
| class14 | CC1 完整链 | AnnotationInvocationHandler + LazyMap，黑名单拦截其他 6 条链 |
| class15 | CC2 链 + 黑名单绕过 | 禁 collections3 整包 + InstantiateTransformer，仅允许 CC2 |
| class16 | CC3 链 + 黑名单绕过 | 禁 InvokerTransformer + collections4，仅允许 CC3 |
| class17 | CC4 链 + 黑名单绕过 | 禁 LazyMap + InvokerTransformer(v3+v4)，仅允许 CC4 |
| class18 | CC5 链 + 类型检查 | 仅允许 BadAttributeValueExpException 顶层类型 |
| class19 | CC6 链 + 严格过滤 | 禁 AnnotationInvocationHandler + BadAttrVal + TemplatesImpl + collections4 + Hashtable |
| class20 | CC7 链 + 过滤绕过 | 禁 AnnotationInvocationHandler + BadAttrVal + TiedMapEntry + InstantiateTransformer + TemplatesImpl + collections4 |

### 过滤策略说明

每个 CC 链靶场通过黑名单机制阻止其他 CC 链的 payload 通过。过滤在 `resolveClass` 阶段执行，
针对反序列化对象图中出现的每个类进行匹配：

- 以 `.` 结尾的条目 → 包名前缀匹配（拦截该包下所有类）
- 不以 `.` 结尾 → 精确类名匹配

| 靶场 | 允许的链 | 拦截原理 |
|------|---------|---------|
| class14 | CC1 | blocks `collections4.*`、`InstantiateTransformer`、`TemplatesImpl`、`BadAttrVal`、`TiedMapEntry`、`Hashtable` |
| class15 | CC2 | blocks `org.apache.commons.collections.`（collections3）、`InstantiateTransformer(col4)`、`TrAXFilter` |
| class16 | CC3 | blocks `InvokerTransformer`、`collections4.*`、`BadAttrVal`、`TiedMapEntry`、`Hashtable` |
| class17 | CC4 | blocks `LazyMap(col3)`、`InvokerTransformer(v3+v4)` |
| class18 | CC5 | blocks `collections4.*`、`TemplatesImpl`、`TrAXFilter`、`Hashtable` + 顶层类型仅允许 `BadAttributeValueExpException` |
| class19 | CC6 | blocks `AnnotationInvocationHandler`、`BadAttrVal`、`TemplatesImpl`、`TrAXFilter`、`collections4.*`、`Hashtable` |
| class20 | CC7 | blocks `AnnotationInvocationHandler`、`BadAttrVal`、`TiedMapEntry`、`InstantiateTransformer`、`TemplatesImpl`、`TrAXFilter`、`collections4.*` |

## 测试

使用 ysoserial 生成 payload 并上传到各模块测试反序列化漏洞。ysoserial.jar 已内置到 Docker 容器 `/tmp/ysoserial.jar`：

```bash
# 生成 CC1 payload
docker exec javaserial-lab java -jar /tmp/ysoserial.jar CommonsCollections1 "id" > cc1.ser

# 上传到靶场
curl -F "uploadFile=@cc1.ser" http://localhost:81/class14/upload
```

各 CC 链 payload 需上传到对应的靶场模块：

| Payload | 目标模块 | 命令 |
|---------|---------|------|
| CommonsCollections1 | /class14/upload | CC1 |
| CommonsCollections2 | /class15/upload | CC2 |
| CommonsCollections3 | /class16/upload | CC3 |
| CommonsCollections4 | /class17/upload | CC4 |
| CommonsCollections5 | /class18/upload | CC5 |
| CommonsCollections6 | /class19/upload | CC6 |
| CommonsCollections7 | /class20/upload | CC7 |

### 注意事项

- 靶场仅限授权的安全测试与学习用途
- 默认使用 81 端口，如有冲突可修改 docker-compose.yml
- **JDK 版本为 8u65**（CC1 链需要，`AnnotationInvocationHandler` 在 8u71 被修复）
- TemplatesImpl 在 JDK 8u65 中默认可反序列化，无需额外参数
---

## 本地构建（开发者）

如需从源码构建：

```bash
# 1. 确保已准备以下件（与Dockerfile同同目）:
#    jdk-8u65-linux-x64.tar.gz（CC1链需要JDK 8u65）
#    poc/lib/ysoserial.jar（payload生成工具）

# 2. 构建镜像
docker compose build

# 3. 启动服务
docker compose up -d
```
