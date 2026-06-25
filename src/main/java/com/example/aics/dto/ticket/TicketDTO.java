package com.example.aics.dto.ticket;

import lombok.Data;

@Data
public class TicketDTO {
    private Long userId;
    private String title;
    private String description;
    private String status;
    private String priority;
}
