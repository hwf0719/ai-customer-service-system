import operator
import redis
from typing import TypedDict, Annotated


from langchain_core.messages import SystemMessage, HumanMessage, AIMessage
from langchain_openai import ChatOpenAI
from langgraph.graph import StateGraph,END
from langgraph.prebuilt import ToolNode

from config import LLM_API_KEY, LLM_BASE_URL, LLM_MODEL, REDIS_HOST, REDIS_PORT, REDIS_PASSWORD
from prompts import SYSTEM_PROMPT
from tools import tools

llm=ChatOpenAI(
    api_key=LLM_API_KEY,
    base_url=LLM_BASE_URL,
    model=LLM_MODEL
)
r=redis.Redis(
    host=REDIS_HOST,
    port=REDIS_PORT,
    password=REDIS_PASSWORD,
    decode_responses=True,
    protocol=2
)

class AgentState(TypedDict):
    messages:Annotated[list,operator.add]

def call_model(state:AgentState):
    messages=state["messages"]
    response=llm.bind_tools(tools=tools).invoke(messages)
    return {"messages":[response]}

def should_continue(state:AgentState):
    last_messages=state["messages"][-1]
    if last_messages.tool_calls:
        return "tools"
    else:
        return "end"

graph=StateGraph(AgentState)
graph.add_node("agent",call_model)
graph.add_node("tools",ToolNode(tools))
graph.set_entry_point("agent")
graph.add_edge("tools","agent")
graph.add_conditional_edges(
    "agent",
    should_continue,{
        "tools":"tools",
        "end":END
    }
)
app=graph.compile()
def chat(question:str,session_id:str="default")->str:
    key=f"chat:{session_id}"
    r.rpush(key,f"user:{question}")
    history_data=r.lrange(key,0,-1)
    conversation_history=[]
    for msg in history_data:
        if msg.startswith("user:"):
            conversation_history.append(HumanMessage(content=msg[5:]))
        elif msg.startswith("ai:"):
            conversation_history.append(AIMessage(content=msg[3:]))

    messages=[SystemMessage(content=SYSTEM_PROMPT)]+conversation_history
    result=app.invoke({"messages":messages})

    ai_message=result["messages"][-1]
    r.rpush(key,f"ai:{ai_message.content}")
    return ai_message.content

def clean(session_id:str="default"):
    key=f"chat:{session_id}"
    r.delete(key)
    return "对话历史已清空"