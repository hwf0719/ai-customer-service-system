from fastapi import FastAPI
from pydantic import BaseModel

from ai_service import classify, sentiment, summary
from config import APP_TITLE, APP_VERSION, APP_HOST, APP_PORT
from agent import chat,clean
from rag import add_faq, delete_faq


class ChatRequest(BaseModel):
    question: str

class ChatResponse(BaseModel):
    answer: str
class FaqRequest(BaseModel):
    question: str
    answer: str
class ClassifyRequest(BaseModel):
    title: str
    description: str
class SentimentRequest(BaseModel):
    content: str
class SummaryRequest(BaseModel):
    messages: list
app=FastAPI(title=APP_TITLE,version=APP_VERSION)

@app.post("/chat")
def chat_endpoint(request:ChatRequest,session_id:str="default"):
    try:
        result=chat(request.question,session_id)
        return ChatResponse(answer=result)
    except Exception as e:
        return ChatResponse(answer=f"抱歉，处理您的问题时出现错误：{str(e)}。请稍后重试或联系人工客服。")
@app.get("/clean")
def clean_endpoint(session_id:str="default"):
    return {"message":clean(session_id)}
@app.post("/faq")
def add_faq_endpoint(request:FaqRequest):
    return {"message":add_faq(request.question,request.answer)}
@app.delete("/faq")
def delete_faq_endpoint(request:FaqRequest):
    return {"message":delete_faq(request.question)}
@app.post("/classify")
def classify_endpoint(classify_request:ClassifyRequest):
    try:
        return {"category": classify(classify_request.title, classify_request.description)}
    except Exception as e:
        return {"category": "OTHER", "error": str(e)}

@app.post("/sentiment")
def sentiment_endpoint(sentiment_request:SentimentRequest):
    try:
        return {"sentiment": sentiment(sentiment_request.content)}
    except Exception as e:
        return {"sentiment": "NEUTRAL", "error": str(e)}

@app.post("/summary")
def summary_endpoint(summary_request:SummaryRequest):
    try:
        return {"summary": summary(summary_request.messages)}
    except Exception as e:
        return {"summary": "摘要生成失败", "error": str(e)}
if __name__=="__main__":
    import uvicorn
    uvicorn.run(app,host=APP_HOST,port=APP_PORT)