package com.example.aics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.aics.dto.page.TicketPageQueryDTO;
import com.example.aics.entity.Ticket;
import com.example.aics.entity.TicketMessage;
import com.example.aics.vo.page.PageResultVO;
import com.example.aics.vo.ticket.TicketVO;

import java.util.List;

public interface TicketService extends IService<Ticket> {

    /**
     * 创建工单（AI自动分类+情绪分析）
     */
    Ticket createTicket(Long userId, String title, String description);

    /**
     * 分配客服
     */
    void assignAgent(Long ticketId, Long agentId);

    /**
     * 更新工单状态
     */
    void updateStatus(Long ticketId, String status);

    /**
     * 生成处理摘要（AI）
     */
    String generateSummary(Long ticketId);

    /**
     * 发送消息
     */
    TicketMessage sendMessage(Long ticketId, Long senderId, String senderType, String content);

    /**
     * 获取工单消息列表
     */
    List<TicketMessage> getMessages(Long ticketId);

    /**
     * 分页查询工单（管理员查看所有，用户只看自己的）
     */
    PageResultVO<TicketVO> queryPage(TicketPageQueryDTO dto, Long userId, String role);
}