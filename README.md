# Student Management System

一个基于 Spring Boot 和 Vue 3 的现代化学生管理系统，实现了学生、课程和成绩的全方位管理功能。

## 🛠 技术栈

### 后端
- **Spring Boot 3.2** - Java 应用框架
- **Spring Data JPA** - 数据持久化
- **MySQL 8.0** - 关系型数据库
- **Hibernate** - ORM 框架
- **SpringDoc OpenAPI** - API 文档生成
- **Lombok** - 代码简化工具

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **Element Plus** - Vue 3 组件库
- **Vue Router** - 路由管理
- **Axios** - HTTP 客户端
- **Vite** - 前端构建工具

### DevOps
- **Docker** - 容器化
- **Docker Compose** - 多容器编排
- **Nginx** - 前端静态资源服务

## 🚀 启动指南

### 前置要求
- Docker Desktop 已安装并启动
- 确保端口 3247、8247、3347 未被占用

### 一键启动

```bash
# 进入项目根目录
cd e:\work\label-2747

# 启动所有服务
docker compose up --build
```

### 启动说明
- 首次启动需要下载镜像和构建，大约需要 3-5 分钟
- 等待看到以下日志表示启动成功：
  - `student-management-db` - Database is ready
  - `student-management-backend` - Started StudentManagementApplication
  - `student-management-frontend` - Nginx started

## 🔐 管理员账号

| 字段 | 值 |
|------|----|
| **账号** | `admin` |
| **密码** | `admin123456` |

> 首次访问前端地址时会自动跳转到登录页面，使用以上账号密码登录后方可进入系统。

## 🔗 服务地址

| 服务 | 地址 | 说明 |
|------|------|------|
| **前端应用** | http://localhost:3247 | Vue 前端界面 |
| **后端 API** | http://localhost:8247 | Spring Boot 后端服务 |
| **API 文档** | http://localhost:8247/swagger-ui.html | Swagger 文档 |
| **数据库** | localhost:3347 | MySQL 数据库 |

### 数据库连接信息
```
Host: localhost
Port: 3347
Database: student_management
Username: root
Password: root
```

## 🧪 功能模块

### 1. 仪表盘
- **数据统计**：学生总数、课程总数、成绩记录数
- **快捷操作**：快速进入各功能模块

### 2. 学生管理
- ✅ 查看学生列表
- ✅ 添加新学生（姓名、性别、年龄、邮箱、手机、入学日期）
- ✅ 编辑学生信息
- ✅ 删除学生
- ✅ 按姓名搜索学生
- ✅ 表单验证（邮箱格式、手机号格式）

### 3. 课程管理
- ✅ 查看课程列表
- ✅ 添加新课程（名称、学分、教师、描述）
- ✅ 编辑课程信息
- ✅ 删除课程
- ✅ 按课程名搜索

### 4. 成绩管理
- ✅ 查看所有成绩记录
- ✅ 录入学生成绩
- ✅ 更新成绩
- ✅ 删除成绩记录
- ✅ 按学生/课程筛选
- ✅ 成绩等级显示（优秀、良好、中等、及格、不及格）
- ✅ 成绩颜色标识

## 📁 项目结构

```
label-2747/
├── backend/                      # Spring Boot 后端
│   ├── src/
│   │   └── main/
│   │       ├── java/com/student/management/
│   │       │   ├── entity/      # 实体类（Student, Course, Grade）
│   │       │   ├── repository/  # 数据访问层
│   │       │   ├── service/     # 业务逻辑层
│   │       │   ├── controller/  # REST API 控制器
│   │       │   └── config/      # 配置类
│   │       └── resources/
│   │           ├── application.yml
│   │           └── data.sql     # 初始数据
│   ├── pom.xml
│   ├── settings.xml             # Maven 镜像配置
│   └── Dockerfile
│
├── frontend/                    # Vue 3 前端
│   ├── src/
│   │   ├── api/                # API 接口
│   │   ├── views/              # 页面组件
│   │   ├── router/             # 路由配置
│   │   ├── styles/             # 样式文件
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   ├── vite.config.js
│   ├── nginx.conf
│   └── Dockerfile
│
├── docker-compose.yml          # Docker 编排配置
└── README.md
```

## 🎨 UI 设计亮点

### 现代化视觉
- 🎨 渐变色背景（紫色渐变主题）
- 💎 卡片式设计with阴影效果
- 🔄 流畅的过渡动画
- 📱 完全响应式布局

### 交互体验
- ⚡ 按钮悬停效果
- 🎯 表单实时验证
- 💬 友好的错误提示
- 🔍 实时搜索筛选

### 组件库
- 使用 **Element Plus** 组件库
- 统一的设计语言
- 优秀的可访问性

## 🐳 Docker 配置说明

### 多阶段构建
- **前端**：使用 Node.js 构建，Nginx 提供服务
- **后端**：使用 Maven 构建，JRE 运行

### 服务依赖
```
frontend → backend → database
```

- Frontend 等待 Backend 启动
- Backend 等待 Database 健康检查通过

### 数据持久化
- MySQL 数据存储在 Docker Volume `mysql_data`
- 数据不会因容器重启而丢失

## 📊 初始数据

系统预置了测试数据（自动导入）：
- **10 名学生**：张三、李四、王五等
- **7 门课程**：高等数学、大学英语、计算机基础等
- **26 条成绩记录**：覆盖不同学生和课程的组合

## 🔧 开发说明

### 本地开发（可选）

如需本地开发而非 Docker 环境：

#### 后端
```bash
cd backend
mvn spring-boot:run
```

#### 前端
```bash
cd frontend
npm install
npm run dev
```

**注意**：本地开发需要修改前端 API 基础URL 为 `http://localhost:8080`

### API 文档
访问 Swagger UI 查看完整 API 文档：
http://localhost:8247/swagger-ui.html

## 🛑 停止服务

```bash
# 停止所有容器
docker compose down

# 停止并删除所有数据（包括数据库）
docker compose down -v
```

## 🔄 重新启动

```bash
# 重新构建并启动
docker compose up --build

# 仅启动（不重新构建）
docker compose up
```

## ⚠️ 常见问题

### Q: 端口被占用？
A: 检查端口 3247、8247、3347 是否被其他程序占用，如需修改端口请编辑 `docker-compose.yml`

### Q: 前端无法连接后端？
A: 确保等待后端完全启动（查看日志），Docker 网络使用服务名 `backend` 自动解析

### Q: 数据库初始化失败？
A: 删除 volume 后重新启动：`docker compose down -v && docker compose up --build`

### Q: Maven/npm 依赖下载慢？
A: 已配置国内镜像源（阿里云 Maven、淘宝 npm），首次构建仍需时间

## 📄 许可证

本项目仅供学习和演示使用。

## 👨‍💻 技术支持

如有问题，请检查：
1. Docker Desktop 是否正常运行
2. 查看容器日志：`docker compose logs -f`
3. 检查端口占用情况

---

**Developed with ❤️ using Spring Boot & Vue 3**
