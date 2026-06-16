# ============================================================
# Docker 多阶段构建 — Java 反序列化靶场 Tomcat 镜像
# 第一阶段：编译所有 WAR 包
# 第二阶段：打包进 Tomcat
# ============================================================

# ---------- 第一阶段：编译 ----------
FROM maven:3.6.3-jdk-8 AS builder

# 使用阿里云 Maven 镜像加速国内构建
COPY .m2/settings.xml /usr/share/maven/conf/settings.xml

WORKDIR /build

# 先拷贝 pom 文件，利用 Docker 缓存加速依赖下载
COPY pom.xml .
COPY ser-common/pom.xml ser-common/
COPY class01-runtime/pom.xml class01-runtime/
COPY class02-unser/pom.xml class02-unser/
COPY class03-unser-cmd/pom.xml class03-unser-cmd/
COPY class04-unser-readobj/pom.xml class04-unser-readobj/
COPY class05-reflection/pom.xml class05-reflection/
COPY class06-javassist/pom.xml class06-javassist/
COPY class07-invoker/pom.xml class07-invoker/
COPY class08-constant/pom.xml class08-constant/
COPY class09-chained/pom.xml class09-chained/
COPY class10-lazymap/pom.xml class10-lazymap/
COPY class11-instantiate/pom.xml class11-instantiate/
COPY class12-templates/pom.xml class12-templates/
COPY class13-transformedmap/pom.xml class13-transformedmap/
COPY class14-cc1/pom.xml class14-cc1/
COPY class15-cc2/pom.xml class15-cc2/
COPY class16-cc3/pom.xml class16-cc3/
COPY class17-cc4/pom.xml class17-cc4/
COPY class18-cc5/pom.xml class18-cc5/
COPY class19-cc6/pom.xml class19-cc6/
COPY class20-cc7/pom.xml class20-cc7/

# 下载依赖（离线缓存）
RUN mvn dependency:go-offline -q || true

# 拷贝全部源码
COPY ser-common/ ser-common/
COPY class01-runtime/ class01-runtime/
COPY class02-unser/ class02-unser/
COPY class03-unser-cmd/ class03-unser-cmd/
COPY class04-unser-readobj/ class04-unser-readobj/
COPY class05-reflection/ class05-reflection/
COPY class06-javassist/ class06-javassist/
COPY class07-invoker/ class07-invoker/
COPY class08-constant/ class08-constant/
COPY class09-chained/ class09-chained/
COPY class10-lazymap/ class10-lazymap/
COPY class11-instantiate/ class11-instantiate/
COPY class12-templates/ class12-templates/
COPY class13-transformedmap/ class13-transformedmap/
COPY class14-cc1/ class14-cc1/
COPY class15-cc2/ class15-cc2/
COPY class16-cc3/ class16-cc3/
COPY class17-cc4/ class17-cc4/
COPY class18-cc5/ class18-cc5/
COPY class19-cc6/ class19-cc6/
COPY class20-cc7/ class20-cc7/

# 编译打包
RUN mvn clean package -DskipTests -q

# ---------- 第二阶段：Tomcat 运行镜像 ----------
FROM tomcat:8.5-jdk8

# 安全提示
RUN echo "==============================================" && \
    echo "  警告：此镜像包含安全漏洞环境" && \
    echo "  仅限授权安全测试与学习用途" && \
    echo "  请勿在生产环境部署" && \
    echo "=============================================="

# 覆写 JDK 为 8u65（CC1 链需要，AnnotationInvocationHandler 在 8u71 被修复）
# 注意：jdk-8u65-linux-x64.tar.gz 需与 Dockerfile 同目录
COPY jdk-8u65-linux-x64.tar.gz /tmp/
RUN tar xzf /tmp/jdk-8u65-linux-x64.tar.gz -C /usr/local/ && \
    rm -f /tmp/jdk-8u65-linux-x64.tar.gz
ENV JAVA_HOME=/usr/local/jdk1.8.0_65
ENV PATH=$JAVA_HOME/bin:$PATH

# 删除 Tomcat 默认应用
RUN rm -rf /usr/local/tomcat/webapps/ROOT \
           /usr/local/tomcat/webapps/docs \
           /usr/local/tomcat/webapps/examples \
           /usr/local/tomcat/webapps/host-manager \
           /usr/local/tomcat/webapps/manager

# 从编译阶段拷贝 WAR 文件
COPY --from=builder /build/class01-runtime/target/class01.war /usr/local/tomcat/webapps/class01.war
COPY --from=builder /build/class02-unser/target/class02.war /usr/local/tomcat/webapps/class02.war
COPY --from=builder /build/class03-unser-cmd/target/class03.war /usr/local/tomcat/webapps/class03.war
COPY --from=builder /build/class04-unser-readobj/target/class04.war /usr/local/tomcat/webapps/class04.war
COPY --from=builder /build/class05-reflection/target/class05.war /usr/local/tomcat/webapps/class05.war
COPY --from=builder /build/class06-javassist/target/class06.war /usr/local/tomcat/webapps/class06.war
COPY --from=builder /build/class07-invoker/target/class07.war /usr/local/tomcat/webapps/class07.war
COPY --from=builder /build/class08-constant/target/class08.war /usr/local/tomcat/webapps/class08.war
COPY --from=builder /build/class09-chained/target/class09.war /usr/local/tomcat/webapps/class09.war
COPY --from=builder /build/class10-lazymap/target/class10.war /usr/local/tomcat/webapps/class10.war
COPY --from=builder /build/class11-instantiate/target/class11.war /usr/local/tomcat/webapps/class11.war
COPY --from=builder /build/class12-templates/target/class12.war /usr/local/tomcat/webapps/class12.war
COPY --from=builder /build/class13-transformedmap/target/class13.war /usr/local/tomcat/webapps/class13.war
COPY --from=builder /build/class14-cc1/target/class14.war /usr/local/tomcat/webapps/class14.war
COPY --from=builder /build/class15-cc2/target/class15.war /usr/local/tomcat/webapps/class15.war
COPY --from=builder /build/class16-cc3/target/class16.war /usr/local/tomcat/webapps/class16.war
COPY --from=builder /build/class17-cc4/target/class17.war /usr/local/tomcat/webapps/class17.war
COPY --from=builder /build/class18-cc5/target/class18.war /usr/local/tomcat/webapps/class18.war
COPY --from=builder /build/class19-cc6/target/class19.war /usr/local/tomcat/webapps/class19.war
COPY --from=builder /build/class20-cc7/target/class20.war /usr/local/tomcat/webapps/class20.war

# 创建 ROOT webapp 作为首页（无需 Apache 反向代理）
RUN mkdir /usr/local/tomcat/webapps/ROOT

# 拷贝 ysoserial 到容器（供 CC 链 payload 生成）
COPY poc/lib/ysoserial.jar /tmp/ysoserial.jar
COPY landing/index.html /usr/local/tomcat/webapps/ROOT/index.html

# 启用 TemplatesImpl 反序列化
ENV CATALINA_OPTS="-Djdk.xml.enableTemplatesImplDeserialization=true"

EXPOSE 8080

CMD ["catalina.sh", "run"]
