package com.example.aics.utils;

import com.example.aics.entity.TicketMessage;
import com.example.aics.vo.ticket.MessageVO;

import java.util.List;
import java.util.stream.Collectors;

public class MessageConverter {

    /**
     * 将 TicketMessage 实体转换为 MessageVO
     */
    public static MessageVO toVO(TicketMessage message) {
        if (message == null) {
            return null;
        }
        MessageVO vo = new MessageVO();
        vo.setId(message.getId());
        vo.setTicketId(message.getTicketId());
        vo.setSenderId(message.getSenderId());
        vo.setSenderType(message.getSenderType());
        vo.setContent(message.getContent());
        vo.setCreateTime(message.getCreateTime());
        return vo;
    }

    /**
     * 将 TicketMessage 列表转换为 MessageVO 列表
     */
    public static List<MessageVO> toVOList(List<TicketMessage> messages) {
        if (messages == null) {
            return List.of();
        }
        return messages.stream()
                .map(MessageConverter::toVO)
                .collect(Collectors.toList());
    }
}
