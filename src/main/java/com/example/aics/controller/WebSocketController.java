package com.example.aics.controller;

import com.example.aics.vo.ticket.MessageVO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    /**
     * 客户端发送消息到 /app/sendMessage
     * 广播到所有订阅 /topic/messages 的客户端
     */
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public MessageVO sendMessage(MessageVO message) {
        // 直接广播消息给所有订阅者
        return message;
    }

    /**
     * 客户端发送消息到 /app/ticket/{ticketId}
     * 广播到所有订阅 /topic/ticket/{ticketId} 的客户端
     */
    @MessageMapping("/ticket/{ticketId}")
    @SendTo("/topic/ticket/{ticketId}")
    public MessageVO ticketMessage(MessageVO message) {
        return message;
    }
}
