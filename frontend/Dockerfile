# 构建阶段
FROM node:20-alpine AS builder
WORKDIR /app

# 拷贝 package.json 和 package-lock.json
COPY package*.json ./

# 配置 npm 镜像源并安装依赖
RUN npm config set registry https://registry.npmmirror.com && \
    npm ci

# 拷贝源代码
COPY . .

# 构建生产版本
RUN npm run build

# 生产阶段
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# 从构建阶段拷贝构建产物
COPY --from=builder /app/dist .

# 拷贝 nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]
