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
│   ├── class05/GenPayload.java      # 反射版 readObject（Class.forName → invoke）
│   │   └── Student.java
│   ├── class06/GenPayload.java      # Javassist 字节码注入
│   │   └── Student.java
│   ├── class07/GenPayload.java      # InvokerTransformer
│   ├── class08/GenPayload.java      # ConstantTransformer
│   ├── class09/GenPayload.java      # ChainedTransformer
│   ├── class10/GenPayload.java      # LazyMap
│   ├── class11/GenPayload.java      # InstantiateTransformer
│   │   └── Student.java
│   ├── class12/GenPayload.java      # TemplatesImpl (javassist)
│   └── class13/GenPayload.java      # TransformedMap
│   └── debug/DeserDebug.java       # 🆕 本地反序列化调试工具
├── lib/                             # 本地 JAR（供 gen.sh 备用）
├── ysoserial/                       # CC1-CC7 链生成脚本（需 Docker 内运行）
│   ├── class14-cc1/gen.sh
│   ├── class15-cc2/gen.sh
│   └── ...
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
`GenPayload.java` 已通过反射方式访问这些内部类，**无需** `--add-exports` 参数，
在任何 JDK 版本上均可直接编译运行。

### 在终端

```bash
# 生成单个 payload
mvn exec:java -Dexec.mainClass="class07.GenPayload"

# 生成全部
bash gen_all.sh
```


### 🐛 本地反序列化调试

使用 DeserDebug 工具在本地直接反序列化 .ser 文件，无需上传到靶场：

[[1;34mINFO[m] Scanning for projects...
[[1;34mINFO[m] 
[[1;34mINFO[m] [1m----------------------< [0;36mcom.ctfstu:poc-generator[0;1m >----------------------[m
[[1;34mINFO[m] [1mBuilding Java反序列化靶场 - POC生成器 1.0[m
[[1;34mINFO[m]   from pom.xml
[[1;34mINFO[m] [1m--------------------------------[ jar ]---------------------------------[m
[[1;34mINFO[m] 
[[1;34mINFO[m] [1m--- [0;32mexec:3.6.3:java[m [1m(default-cli)[m @ [36mpoc-generator[0;1m ---[m
[[1;33mWARNING[m] 
[1;31mjava.lang.ClassNotFoundException[m: [1;31mdebug.DeserDebug[m
    [1mat[m org.codehaus.mojo.exec.URLClassLoaderBuilder$ExecJavaClassLoader.loadClass ([1mURLClassLoaderBuilder.java:211[m)
    [1mat[m java.lang.ClassLoader.loadClass ([1mClassLoader.java:490[m)
    [1mat[m org.codehaus.mojo.exec.AbstractExecJavaBase.doExecClassLoader ([1mAbstractExecJavaBase.java:376[m)
    [1mat[m org.codehaus.mojo.exec.AbstractExecJavaBase.lambda$execute$0 ([1mAbstractExecJavaBase.java:287[m)
    [1mat[m java.lang.Thread.run ([1mThread.java:1474[m)
[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
[[1;34mINFO[m] [1;31mBUILD FAILURE[m
[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
[[1;34mINFO[m] Total time:  0.290 s
[[1;34mINFO[m] Finished at: 2026-06-14T21:04:06-04:00
[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
[[1;31mERROR[m] Failed to execute goal [32morg.codehaus.mojo:exec-maven-plugin:3.6.3:java[m [1m(default-cli)[m on project [36mpoc-generator[m: [1;31mAn exception occurred while executing the Java class. debug.DeserDebug[m -> [1m[Help 1][m
[[1;31mERROR[m] 
[[1;31mERROR[m] To see the full stack trace of the errors, re-run Maven with the [1m-e[m switch.
[[1;31mERROR[m] Re-run Maven using the [1m-X[m switch to enable full debug logging.
[[1;31mERROR[m] 
[[1;31mERROR[m] For more information about the errors and possible solutions, please read the following articles:
[[1;31mERROR[m] [1m[Help 1][m http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException
[[1;34mINFO[m] Scanning for projects...
[[1;34mINFO[m] 
[[1;34mINFO[m] [1m----------------------< [0;36mcom.ctfstu:poc-generator[0;1m >----------------------[m
[[1;34mINFO[m] [1mBuilding Java反序列化靶场 - POC生成器 1.0[m
[[1;34mINFO[m]   from pom.xml
[[1;34mINFO[m] [1m--------------------------------[ jar ]---------------------------------[m
[[1;34mINFO[m] 
[[1;34mINFO[m] [1m--- [0;32mexec:3.6.3:java[m [1m(default-cli)[m @ [36mpoc-generator[0;1m ---[m
[[1;33mWARNING[m] 
[1;31mjava.lang.ClassNotFoundException[m: [1;31mdebug.DeserDebug[m
    [1mat[m org.codehaus.mojo.exec.URLClassLoaderBuilder$ExecJavaClassLoader.loadClass ([1mURLClassLoaderBuilder.java:211[m)
    [1mat[m java.lang.ClassLoader.loadClass ([1mClassLoader.java:490[m)
    [1mat[m org.codehaus.mojo.exec.AbstractExecJavaBase.doExecClassLoader ([1mAbstractExecJavaBase.java:376[m)
    [1mat[m org.codehaus.mojo.exec.AbstractExecJavaBase.lambda$execute$0 ([1mAbstractExecJavaBase.java:287[m)
    [1mat[m java.lang.Thread.run ([1mThread.java:1474[m)
[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
[[1;34mINFO[m] [1;31mBUILD FAILURE[m
[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
[[1;34mINFO[m] Total time:  0.169 s
[[1;34mINFO[m] Finished at: 2026-06-14T21:04:06-04:00
[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
[[1;31mERROR[m] Failed to execute goal [32morg.codehaus.mojo:exec-maven-plugin:3.6.3:java[m [1m(default-cli)[m on project [36mpoc-generator[m: [1;31mAn exception occurred while executing the Java class. debug.DeserDebug[m -> [1m[Help 1][m
[[1;31mERROR[m] 
[[1;31mERROR[m] To see the full stack trace of the errors, re-run Maven with the [1m-e[m switch.
[[1;31mERROR[m] Re-run Maven using the [1m-X[m switch to enable full debug logging.
[[1;31mERROR[m] 
[[1;31mERROR[m] For more information about the errors and possible solutions, please read the following articles:
[[1;31mERROR[m] [1m[Help 1][m http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException

也可以在 IDEA 中直接运行 `debug/DeserDebug`，在 Program arguments 中填入 `.ser` 文件路径。

> 调试工具使用与靶场完全相同的 ObjectInputStream 反序列化逻辑，
> 可以单步跟踪 readObject 执行过程，理解漏洞触发原理。

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
| class05 | 无（标准库 + 反射） |
| class06 | javassist 3.29.2-GA |
| class07-13 | commons-collections 3.2.1 |
| class12 | + javassist 3.29.2-GA |
| class14-20 | ysoserial（Docker 内运行） |
