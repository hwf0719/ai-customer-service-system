# 🤖 AI 智能客服工单系统

基于 Spring Boot + Vue 3 + Python AI 的智能客服工单系统。

## 🏗️ 系统架构

```
┌─────────────────┐    HTTP     ┌─────────────────┐    HTTP     ┌─────────────────┐
│   Vue 3 前端     │ ──────────▶ │  Java 后端       │ ──────────▶ │  Python AI 服务  │
│   :5173         │            │  :8080          │            │  :8000          │
└─────────────────┘            └─────────────────┘            └─────────────────┘
```

- **前端**：Vue 3 + Element Plus + ECharts
- **后端**：Spring Boot 3 + MyBatis-Plus + MySQL + Redis + JWT
- **AI 服务**：FastAPI + LangChain + Chroma 向量数据库

## 📂 项目结构

```
ai-customer-service/
├── frontend/                 # Vue 3 前端
├── src/                      # Java 后端
│   └── main/java/com/example/aics/
│       ├── config/           # 配置类
│       ├── controller/       # 控制器
│       ├── service/          # 业务逻辑
│       ├── entity/           # 实体类
│       ├── mapper/           # 数据访问
│       └── ...
├── python-ai-service/        # Python AI 服务
│   ├── main.py               # FastAPI 入口
│   ├── agent.py              # LangChain Agent
│   ├── ai_service.py         # 分类/情绪/摘要
│   ├── rag.py                # RAG 知识库检索
│   ├── prompts.py            # 提示词模板
│   ├── tools.py              # Agent 工具
│   ├── config.py             # 配置文件
│   └── knowledge/faq.txt     # 知识库
└── pom.xml                   # Maven 配置
```

## ✨ 核心功能

### 用户端
- 📝 提交工单（AI 自动分类 + 情绪分析）
- 📋 查看我的工单
- 💬 工单对话
- 👤 个人中心

### 管理端
- 📊 控制台（统计图表）
- 📋 工单管理（查看、更新状态）
- 🤖 AI 摘要生成
- 📚 知识库管理
- 👥 用户管理

### AI 能力
- 🏷️ **自动分类**：技术问题 / 账单问题 / 一般咨询
- 😊 **情绪分析**：正面 / 中性 / 负面（负面自动升级优先级）
- 📝 **智能摘要**：自动生成工单处理摘要
- 💬 **智能对话**：基于知识库的 RAG 检索

## 🚀 快速开始

### 环境要求

- Java 17+
- Node.js 16+
- Python 3.10+
- MySQL 8.0+
- Redis

### 1. 启动 Python AI 服务

```bash
cd python-ai-service
pip install -r requirements.txt
python main.py
```

### 2. 启动 Java 后端

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE ai_customer_service;"

# 修改 src/main/resources/application.yml 中的数据库配置

# 启动
mvn spring-boot:run
```

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

### 4. 访问

- 前端：http://localhost:5173
- 后端 API：http://localhost:8080
- Python AI 文档：http://localhost:8000/docs

## 📊 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 客服 | agent01 | agent123 |
| 普通用户 | user01 | user123 |

## 🛠️ 技术栈

| 层 | 技术 |
|---|------|
| 前端 | Vue 3, Vite, Element Plus, ECharts, Pinia, Axios |
| 后端 | Spring Boot 3.2, MyBatis-Plus, Redis, JWT |
| AI 服务 | FastAPI, LangChain, LangGraph, Chroma, HuggingFace |
| 数据库 | MySQL 8.0 |

## 📄 License

MIT
