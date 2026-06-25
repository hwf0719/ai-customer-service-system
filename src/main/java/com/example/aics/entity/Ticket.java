package com.example.aics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("ticket")
public class Ticket {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    /**
     * 状态：PENDING-待处理, PROCESSING-处理中, RESOLVED-已解决, CLOSED-已关闭
     */
    private String status;

    /**
     * 优先级：LOW-低, MEDIUM-中, HIGH-高, URGENT-紧急
     */
    private String priority;

    /**
     * AI自动分类结果
     */
    private String category;

    /**
     * AI情绪分析结果
     */
    private String sentiment;

    /**
     * 提交用户ID
     */
    private Long userId;

    /**
     * 处理客服ID
     */
    private Long agentId;

    /**
     * AI生成的处理摘要
     */
    private String summary;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}