import os

# HuggingFace 镜像（国内加速）
HF_ENDPOINT = "https://hf-mirror.com"

# LLM 配置
LLM_BASE_URL = "https://api.xiaomimimo.com/v1"
LLM_API_KEY = os.getenv("LLM_API_KEY", "sk-czhdaro06yzfaizekdmif6ie4awvaf9t7dffx21yy5j92bmr")
LLM_MODEL = "mimo-v2.5-pro"

# Embedding 模型
EMBEDDING_MODEL_NAME = "BAAI/bge-small-zh-v1.5"

# 向量数据库配置
CHROMA_PERSIST_DIR = "./chroma_db"
KNOWLEDGE_DIR = "./knowledge"
FAQ_FILE = os.path.join(KNOWLEDGE_DIR, "faq.txt")

# 文本分割配置
CHUNK_SIZE = 500
CHUNK_OVERLAP = 50

# 检索配置
RETRIEVER_TOP_K = 3

# 服务配置
APP_HOST = "127.0.0.1"
APP_PORT = 8000
APP_TITLE = "智能客服助手"
APP_VERSION = "0.1.0"

# Redis 配置
REDIS_HOST = "127.0.0.1"
REDIS_PORT = 6379
REDIS_PASSWORD = os.getenv("REDIS_PASSWORD", "123456")
