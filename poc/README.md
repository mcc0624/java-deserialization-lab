# Java 反序列化靶场 — POC 生成工具

## 目录结构

```
poc/
├── README.md
├── lib/                          # 依赖 JAR
│   ├── commons-collections-3.2.1.jar
│   ├── commons-collections4-4.0.jar
│   ├── javassist-3.29.2-GA.jar
│   └── ysoserial.jar             # CC 链 payload 生成
├── class02-unser/gen.sh          # 基础反序列化（Student.toString）
├── class03-unser-cmd/gen.sh      # getName() → 命令执行
├── class04-unser-readobj/gen.sh  # readObject() → 命令执行
├── class07-invoker/gen.sh        # InvokerTransformer
├── class08-constant/gen.sh       # ConstantTransformer
├── class09-chained/gen.sh        # ChainedTransformer
├── class10-lazymap/gen.sh        # LazyMap
├── class11-instantiate/gen.sh    # InstantiateTransformer
├── class12-templates/gen.sh      # TemplatesImpl (javassist)
├── class13-transformedmap/gen.sh # TransformedMap
├── class14-cc1/gen.sh ~ class20-cc7/gen.sh  # CC1~CC7 (ysoserial)
├── gen_all.sh                    # 一键生成所有 payload
```

## 使用方法

### 1. 生成单个模块的 payload

```bash
cd poc
bash class02-unser/gen.sh    # 生成 class02 的 payload
bash class12-templates/gen.sh # 生成 class12 的 payload
```

### 2. 生成所有模块的 payload

```bash
cd poc
bash gen_all.sh
```

### 3. 上传 payload 测试

每个 `gen.sh` 会在当前目录生成对应的 `.ser` 文件，通过靶场页面上传到对应模块即可。

或者用 curl：
```bash
curl -X POST -F "uploadFile=@class02.ser" http://localhost:81/class02/upload
```

### 4. class01 直接 curl 测试

```bash
# 命令执行
curl -X POST http://localhost:81/class01/execute -d "command=id"
```
