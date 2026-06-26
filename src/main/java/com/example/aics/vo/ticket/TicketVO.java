package com.example.aics.vo.ticket;

import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
