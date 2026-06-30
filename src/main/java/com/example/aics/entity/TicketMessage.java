package com.example.aics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ticket_message")
public class TicketMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long ticketId;

    private Long senderId;

    /**
     * 发送者类型：USER-用户, AGENT-客服, AI-AI助手
     */
    private String senderType;

    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}