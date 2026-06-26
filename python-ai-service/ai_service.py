from langchain_core.output_parsers import JsonOutputParser
from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import ChatOpenAI

from config import LLM_API_KEY, LLM_BASE_URL, LLM_MODEL
from prompts import CLASSIFY_PROMPT, SENTIMENT_PROMPT, SUMMARY_PROMPT

llm=ChatOpenAI(
    api_key=LLM_API_KEY,
    base_url=LLM_BASE_URL,
    model=LLM_MODEL
)
def classify(title, description):
    try:
        prompt = ChatPromptTemplate.from_template(CLASSIFY_PROMPT)
        chain = prompt | llm | JsonOutputParser()
        result = chain.invoke({"title": title, "description": description})
        return result.get("category", "OTHER")
    except Exception as e:
        print(f"分类失败：{e}")
        return "OTHER"

def sentiment(content):
    try:
        prompt = ChatPromptTemplate.from_template(SENTIMENT_PROMPT)
        chain = prompt | llm | JsonOutputParser()
        result = chain.invoke({"content": content})
        return result.get("sentiment", "NEUTRAL")
    except Exception as e:
        print(f"情绪分析失败：{e}")
        return "NEUTRAL"

def summary(messages):
    try:
        prompt = ChatPromptTemplate.from_template(SUMMARY_PROMPT)
        chain = prompt | llm | JsonOutputParser()
        result = chain.invoke({"messages": messages})
        return result.get("summary", "摘要生成失败")
    except Exception as e:
        print(f"摘要生成失败：{e}")
        return "摘要生成失败"