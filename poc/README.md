# Java 反序列化靶场 — POC 生成工具

Maven 项目，导入 IntelliJ IDEA 即可使用。

## 目录结构

```
poc/
├── pom.xml                          # Maven 项目配置
├── src/main/java/
│   ├── class02/GenPayload.java      # HashMap 反序列化（无需命令）
│   │   └── Student.java             # 演示 toString 触发
│   ├── class03/GenPayload.java      # getName() → 命令执行
│   │   └── Student.java
│   ├── class04/GenPayload.java      # readObject() → 命令执行
│   │   └── Student.java
│   ├── class07/GenPayload.java      # InvokerTransformer
│   ├── class08/GenPayload.java      # ConstantTransformer
│   ├── class09/GenPayload.java      # ChainedTransformer
│   ├── class10/GenPayload.java      # LazyMap
│   ├── class11/GenPayload.java      # InstantiateTransformer
│   │   └── Student.java
│   ├── class12/GenPayload.java      # TemplatesImpl (javassist)
│   └── class13/GenPayload.java      # TransformedMap
├── lib/                             # 本地 JAR（供 gen.sh 备用）
├── ysoserial/                       # CC1-CC7 链（Docker 内生成）
├── gen_all.sh                       # 一键生成所有 payload
└── README.md
```

## 使用方法

### 在 IDEA 中

1. **File → Open** → 选择 `poc/pom.xml` → 以项目打开
2. IDEA 自动下载 Maven 依赖（commons-collections, javassist）
3. 打开任意 `GenPayload.java` → 点击类名旁的 ▶ 绿色三角 → **Run**
4. 生成的 `.ser` 文件在项目根目录 `poc/`

### class12 特殊说明

class12 (TemplatesImpl) 使用了 `com.sun.org.apache.xalan...` 内部类。
如果在你的 JDK 上编译报错（`package com.sun.org.apache.xalan... is not visible`），在 IDEA 中：

1. 打开 `class12/GenPayload.java`
2. **File → Settings → Build, Execution, Deployment → Compiler → Java Compiler**
3. 在 `Override compiler parameters per-module` 中为当前模块添加：
   ```
   --add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
   --add-exports java.xml/com.sun.org.apache.xalan.internal.xsltc.runtime=ALL-UNNAMED
   --add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
   ```
4. 或者在 **Run → Edit Configurations** → `VM options` 添加上述参数

> 运行 `class12.GenPayload` 时也需要上述 `--add-exports` 和 `--add-opens` JVM 参数。

### 在终端

```bash
# 生成单个 payload
mvn exec:java -Dexec.mainClass="class07.GenPayload"

# 生成全部
bash gen_all.sh
```

### 上传到靶场

```bash
curl -X POST -F "uploadFile=@class07.ser" http://localhost:81/class07/upload
```

### CC 链 (class14-20)

CC1-CC7 利用 ysoserial 在 Docker 内生成（JDK 8 环境）：
```bash
bash ysoserial/class14-cc1/gen.sh id
```
或通过 `gen_all.sh` 一键生成。

## 依赖说明

| 模块 | 依赖 |
|------|------|
| class02-04 | 无（标准库） |
| class07-13 | commons-collections 3.2.1 |
| class12 | + javassist 3.29.2-GA |
| class14-20 | ysoserial（Docker 内运行） |
