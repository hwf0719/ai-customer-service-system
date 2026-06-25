package com.example.aics.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MessageDTO {

    @NotBlank(message = "消息内容不能为空")
    private String content;

    @NotBlank(message = "发送者类型不能为空")
    @Pattern(regexp = "^(USER|AGENT|AI)$", message = "发送者类型只能是 USER、AGENT 或 AI")
    private String senderType;
}
