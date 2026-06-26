# 🤖 AI 智能客服助手 - Python AI 服务

基于 LangChain + FastAPI 的 AI 服务层，为 Java 后端提供智能分类、情绪分析、知识库检索等功能。

## 📋 功能列表

| 接口 | 方法 | 功能 |
|------|------|------|
| `/chat` | POST | 智能对话（LangChain Agent） |
| `/classify` | POST | 工单自动分类 |
| `/sentiment` | POST | 情绪分析 |
| `/summary` | POST | 生成工单摘要 |
| `/faq` | POST | 添加知识库 |
| `/faq` | DELETE | 删除知识库 |
| `/clean` | GET | 清空对话历史 |

## 🏗️ 技术栈

- **FastAPI** — Web 框架
- **LangChain** — AI 应用框架
- **LangGraph** — Agent 工作流
- **Chroma** — 向量数据库
- **HuggingFace** — Embedding 模型
- **Redis** — 多轮对话记忆

## 📂 项目结构

```
.
├── main.py           # FastAPI 入口，定义所有接口
├── agent.py          # LangChain Agent + LangGraph 工作流
├── ai_service.py     # AI 分类/情绪/摘要服务
├── rag.py            # RAG 知识库检索（Chroma 向量数据库）
├── prompts.py        # 提示词模板
├── tools.py          # Agent 工具定义
├── config.py         # 配置文件
├── requirements.txt  # Python 依赖
└── knowledge/
    └── faq.txt       # FAQ 知识库
```

## 🚀 快速开始

### 1. 安装依赖

```bash
pip install -r requirements.txt
```

### 2. 配置

修改 `config.py` 中的配置：

```python
LLM_API_KEY = "your-api-key"
LLM_BASE_URL = "https://api.xiaomimimo.com/v1"
LLM_MODEL = "mimo-v2.5-pro"
REDIS_HOST = "127.0.0.1"
REDIS_PORT = 6379
REDIS_PASSWORD = "123456"
```

### 3. 启动服务

```bash
python main.py
```

服务启动后访问 `http://localhost:8000/docs` 查看 API 文档。

## 🔗 关联项目

- **Java 后端**：[ai-customer-service](https://github.com/hwf0719/ai-customer-service-system)
- **前端**：Vue 3 + Element Plus + ECharts

## 📝 提示词设计

### 工单分类

```
分类类别：TECHNICAL（技术问题）、BILLING（账单问题）、GENERAL（一般咨询）、OTHER（其他）
```

### 情绪分析

```
情绪倾向：POSITIVE（正面）、NEUTRAL（中性）、NEGATIVE（负面）
```

### 摘要生成

```
根据对话记录生成简洁的工单处理摘要
```

## 📄 License

MIT
