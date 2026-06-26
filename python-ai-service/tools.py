from langchain_core.tools import tool

from rag import query
tickets_db = {
    "TK001": {"title": "无法登录", "status": "处理中", "priority": "高"},
    "TK002": {"title": "退款申请", "status": "已解决", "priority": "中"},
    "TK003": {"title": "商品损坏", "status": "待处理", "priority": "高"},
}
ticket_counter = 100
@tool
def search_knowledge(question: str)->str:
    """搜索知识库，查找与问题相关的信息"""
    result=query(question)
    return "\n".join(result)

@tool
def get_ticket_status(ticket_id: str)->str:
    """根据工单号查询工单状态"""
    ticket=tickets_db.get(ticket_id)
    if ticket:
        return f"工单：{ticket_id}:{ticket['title']},状态：{ticket['status']},优先级：{ticket['priority']}"
    else:
        return f"未找到工单：{ticket_id}"

@tool
def create_ticket(title :str,description :str,priority :str)->str:
    """创建新工单，返回工单号"""
    global ticket_counter
    ticket_counter+=1
    ticket_id=f"TK{ticket_counter}"
    tickets_db[ticket_id]={"title":title,"description":description,"status":"待处理","priority":priority}
    return f"工单创建成功，工单号：{ticket_id}"
@tool
def list_tickets()->str:
    """列出所有工单"""
    if not tickets_db:
        return "暂无工单"
    result="工单列表：\n"
    for ticket_id,ticket in tickets_db.items():
        result+=f"- {ticket_id}: {ticket['title']} | 状态:{ticket['status']} | 优先级:{ticket['priority']}\n"
    return  result

tools=[search_knowledge,get_ticket_status,create_ticket,list_tickets]