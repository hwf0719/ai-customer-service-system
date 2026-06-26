package com.example.aics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aics.constant.MessageConstant;
import com.example.aics.dto.page.TicketPageQueryDTO;
import com.example.aics.entity.Ticket;
import com.example.aics.enums.Priority;
import com.example.aics.enums.TicketStatus;
import com.example.aics.exception.BusinessException;
import com.example.aics.entity.TicketMessage;
import com.example.aics.mapper.TicketMapper;
import com.example.aics.mapper.TicketMessageMapper;
import com.example.aics.service.AiService;
import com.example.aics.service.TicketService;
import com.example.aics.utils.TicketConverter;
import com.example.aics.vo.page.PageResultVO;
import com.example.aics.vo.ticket.TicketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    @Autowired
    private TicketMessageMapper ticketMessageMapper;
    @Autowired
    private AiService aiService;

    @Override
    public Ticket createTicket(Long userId, String title, String description) {
        String category = aiService.classify(title, description);
        String sentiment = aiService.sentiment(description);
        Ticket ticket=Ticket.builder()
                .userId(userId)
                .title(title)
                .description(description)
                .status(TicketStatus.PENDING.name())
                .priority(Priority.MEDIUM.name())
                .category(category)
                .sentiment(sentiment)
                .build();
        if ("NEGATIVE".equals(sentiment)) {
            ticket.setPriority(Priority.HIGH.name());
        }
        save(ticket);
        return ticket;
    }

    @Override
    public void assignAgent(Long ticketId, Long agentId) {
        Ticket ticket=getById(ticketId);
        if (ticket == null){
            throw new BusinessException(MessageConstant.TICKET_NOT_FOUND);
        }
        ticket.setAgentId(agentId);
        ticket.setStatus(TicketStatus.PROCESSING.name());
        updateById(ticket);
    }

    @Override
    public void updateStatus(Long ticketId, String status) {
        Ticket ticket=getById(ticketId);
        if (ticket == null){
            throw new BusinessException(MessageConstant.TICKET_NOT_FOUND);
        }
        ticket.setStatus(status);
        updateById(ticket);
    }

    @Override
    public String generateSummary(Long ticketId) {
        List<TicketMessage> ticketMessages=getMessages(ticketId);

        List<Map<String,String>> messages=new ArrayList<>();
        for (TicketMessage tm:ticketMessages){
            Map<String,String> msg=new HashMap<>();
            msg.put("role", tm.getSenderType().toLowerCase());
            msg.put("content", tm.getContent());
            messages.add(msg);
        }
        String summary=aiService.summary(messages);
        Ticket ticket=getById(ticketId);
        ticket.setSummary(summary);
        updateById(ticket);
        return summary;

    }

    @Override
    public TicketMessage sendMessage(Long ticketId, Long senderId, String senderType, String content) {
        // 验证工单是否存在
        Ticket ticket = getById(ticketId);
        if (ticket == null) {
            throw new BusinessException(MessageConstant.TICKET_NOT_FOUND);
        }

        // 创建消息
        TicketMessage message = TicketMessage.builder()
                .ticketId(ticketId)
                .senderId(senderId)
                .senderType(senderType)
                .content(content)
                .build();
        ticketMessageMapper.insert(message);
        return message;
    }

    @Override
    public List<TicketMessage> getMessages(Long ticketId) {
        LambdaQueryWrapper<TicketMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TicketMessage::getTicketId, ticketId)
                .orderByAsc(TicketMessage::getCreateTime);
        return ticketMessageMapper.selectList(wrapper);
    }

    @Override
    public PageResultVO<TicketVO> queryPage(TicketPageQueryDTO dto, Long userId, String role) {
        // 1. 创建分页对象
        Page<Ticket> page = new Page<>(dto.getPage(), dto.getPageSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<Ticket> wrapper = new LambdaQueryWrapper<>();

        // 权限控制：普通用户只能查看自己的工单
        if (!"ADMIN".equals(role) && !"AGENT".equals(role)) {
            wrapper.eq(Ticket::getUserId, userId);
        }

        wrapper.eq(StringUtils.isNotBlank(dto.getStatus()),
                Ticket::getStatus, dto.getStatus());
        wrapper.like(StringUtils.isNotBlank(dto.getKeyword()),
                Ticket::getTitle, dto.getKeyword());
        wrapper.orderByDesc(Ticket::getCreateTime);

        // 3. 执行分页查询
        Page<Ticket> result = page(page, wrapper);

        // 4. 转换为 VO
        List<TicketVO> voList = TicketConverter.toVOList(result.getRecords());

        // 5. 封装返回
        PageResultVO<TicketVO> pageResult = new PageResultVO<>();
        pageResult.setRecords(voList);
        pageResult.setTotal(result.getTotal());
        pageResult.setPage(dto.getPage());
        pageResult.setPageSize(dto.getPageSize());
        return pageResult;
    }

}
