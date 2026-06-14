# Java 反序列化漏洞靶场

基于 Commons Collections 链学习的 Java 反序列化漏洞靶场，包含 18 个渐进式挑战模块。

## 快速部署

### 前置条件

- Docker Engine 20.10+
- Docker Compose v2

### 部署命令

```bash
# 一键部署
chmod +x deploy.sh && ./deploy.sh

# 或手动执行
docker compose build
docker compose up -d
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
| class14 | CC1 完整链 | AnnotationInvocationHandler + LazyMap |
| class15 | CC2 链 + 黑名单绕过 | 禁 cc3 不禁 cc4 |
| class16 | CC3 链 + 黑名单绕过 | TemplatesImpl 替代方案 |
| class17 | CC4 链 + 过滤演示 | InvokerTransformer 过滤 |
| class18 | CC5 链 + 类型检查 | 仅允许 BadAttributeValueExpException |
| class19 | CC6 链 + 严格过滤 | 最严格黑名单绕过 |
| class20 | CC7 链 + 过滤绕过 | 替代链结构 |

## 测试

使用 ysoserial 生成 payload 并上传到各模块测试反序列化漏洞。

### 注意事项

- 靶场仅限授权的安全测试与学习用途
- 默认使用 81 端口，如有冲突可修改 docker-compose.yml
- JDK 版本为 8，部分 CC 链在高版本 JDK 上可能无法触发
