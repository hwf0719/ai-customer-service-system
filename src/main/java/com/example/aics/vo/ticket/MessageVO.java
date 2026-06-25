package com.example.aics.vo.ticket;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVO {

    private Long id;

    private Long ticketId;

    private Long senderId;

    /**
     * 发送者类型：USER-用户, AGENT-客服, AI-AI助手
     */
    private String senderType;

    private String content;

    private LocalDateTime createTime;
}
