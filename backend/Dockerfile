# 构建阶段 - 使用本地已有的镜像
FROM node:20-alpine AS build
WORKDIR /app

# 安装 Maven 和 JDK
RUN apk add --no-cache maven openjdk17

# 拷贝 Maven 配置文件
COPY pom.xml .

# 拷贝源代码
COPY src ./src

# 打包应用（包含 Lombok 处理）
RUN mvn clean compile package -DskipTests

# 运行阶段
FROM golang:1.25-bookworm
WORKDIR /app

# 从构建阶段拷贝 JAR 文件
COPY --from=build /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]
