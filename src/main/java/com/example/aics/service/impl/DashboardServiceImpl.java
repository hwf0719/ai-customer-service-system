package com.example.aics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.aics.entity.Ticket;
import com.example.aics.entity.TicketMessage;
import com.example.aics.mapper.TicketMapper;
import com.example.aics.mapper.TicketMessageMapper;
import com.example.aics.service.DashboardService;
import com.example.aics.vo.dashboard.StatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private TicketMessageMapper ticketMessageMapper;
    @Override
    public StatsVO getStats() {
        StatsVO stats = new StatsVO();
        //总工单
        stats.setTotalTickets(ticketMapper.selectCount(null));
        //待处理工单
        LambdaQueryWrapper<Ticket> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(Ticket::getStatus,"PENDING");
        stats.setPendingTickets(ticketMapper.selectCount(pendingWrapper));
        //已处理工单
        LambdaQueryWrapper<Ticket> resolvedWrapper = new LambdaQueryWrapper<>();
        resolvedWrapper.eq(Ticket::getStatus,"RESOLVED");
        stats.setResolvedTickets(ticketMapper.selectCount(resolvedWrapper));
        //平均处理时长
        stats.setAvgResponseTime(calculateAvgRespondTime());
        return stats;
    }

    private String calculateAvgRespondTime() {
        List<Ticket> tickets = ticketMapper.selectList(null);
        if (tickets.isEmpty()) {
            return "0h";
        }
        long totalTime = 0;
        long count = 0;
        for (Ticket ticket : tickets) {
            // 检查创建时间是否为空
            if (ticket.getCreateTime() == null) {
                continue;
            }

            TicketMessage firstReply = ticketMessageMapper.selectOne(
                    new LambdaQueryWrapper<TicketMessage>()
                            .eq(TicketMessage::getTicketId, ticket.getId())
                            .eq(TicketMessage::getSenderType, "AGENT")
                            .orderByAsc(TicketMessage::getCreateTime)
                            .last("LIMIT 1")
            );
            if (firstReply != null && firstReply.getCreateTime() != null) {
                Long responseTime = firstReply.getCreateTime()
                        .atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()
                        - ticket.getCreateTime()
                        .atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli();
                totalTime += responseTime;
                count++;
            }
        }
        if (count == 0) {
            return "0h";
        }
        Long avgTime = totalTime / count;
        Long hours = avgTime / 1000 / 60 / 60;
        return hours + "h";
    }


}
