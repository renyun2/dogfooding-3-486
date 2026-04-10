# 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 拷贝 Maven 配置文件和 pom.xml
COPY settings.xml /root/.m2/settings.xml
COPY pom.xml .

# 预下载依赖
RUN mvn dependency:go-offline -DskipTests || true

# 拷贝源代码
COPY src ./src

# 打包应用
RUN mvn package -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre
WORKDIR /app

# 从构建阶段拷贝 JAR 文件
COPY --from=build /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]
