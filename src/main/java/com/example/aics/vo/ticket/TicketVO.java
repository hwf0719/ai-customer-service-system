package com.example.aics.vo.ticket;

import lombok.Data;

@Data
public class TicketVO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String category;
    private String sentiment;
    private String summary;
    private Long userId;
    private Long agentId;
}
