package com.example.aics.utils;

import com.example.aics.entity.Ticket;
import com.example.aics.vo.ticket.TicketVO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ticket 实体与 VO 转换工具类
 */
public class TicketConverter {

    private TicketConverter() {}

    /**
     * Ticket -> TicketVO
     */
    public static TicketVO toVO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        TicketVO vo = new TicketVO();
        vo.setId(ticket.getId());
        vo.setTitle(ticket.getTitle());
        vo.setDescription(ticket.getDescription());
        vo.setStatus(ticket.getStatus());
        vo.setPriority(ticket.getPriority());
        vo.setCategory(ticket.getCategory());
        vo.setSentiment(ticket.getSentiment());
        vo.setSummary(ticket.getSummary());
        vo.setUserId(ticket.getUserId());
        vo.setAgentId(ticket.getAgentId());
        return vo;
    }

    /**
     * List<Ticket> -> List<TicketVO>
     */
    public static List<TicketVO> toVOList(List<Ticket> tickets) {
        if (tickets == null) {
            return Collections.emptyList();
        }
        return tickets.stream()
                .map(TicketConverter::toVO)
                .collect(Collectors.toList());
    }
}
