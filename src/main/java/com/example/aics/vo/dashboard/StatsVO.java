package com.example.aics.vo.dashboard;

import lombok.Data;

@Data
public class StatsVO {
    private Long totalTickets;
    private Long pendingTickets;
    private Long resolvedTickets;
    private String avgResponseTime;
}
