package com.example.aics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.aics.dto.page.TicketPageQueryDTO;
import com.example.aics.entity.Ticket;
import com.example.aics.vo.page.PageResultVO;
import com.example.aics.vo.ticket.TicketVO;

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

    PageResultVO<TicketVO> queryPage(TicketPageQueryDTO dto);
}