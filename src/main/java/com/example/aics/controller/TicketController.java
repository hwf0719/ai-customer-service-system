package com.example.aics.controller;

import com.example.aics.common.Result;
import com.example.aics.context.BaseContext;
import com.example.aics.dto.page.TicketPageQueryDTO;
import com.example.aics.dto.ticket.TicketDTO;
import com.example.aics.dto.ticket.MessageDTO;
import com.example.aics.entity.Ticket;
import com.example.aics.entity.TicketMessage;
import com.example.aics.service.TicketService;
import com.example.aics.utils.MessageConverter;
import com.example.aics.utils.TicketConverter;
import com.example.aics.vo.page.PageResultVO;
import com.example.aics.vo.ticket.MessageVO;
import com.example.aics.vo.ticket.TicketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("")
    public Result<TicketVO> createTicket(@RequestBody @Valid TicketDTO ticketDTO) {
        Long userId = BaseContext.getCurrentId();
        Ticket ticket = ticketService.createTicket(userId, ticketDTO.getTitle(), ticketDTO.getDescription());
        return Result.success(TicketConverter.toVO(ticket));
    }

    @GetMapping("/{id}")
    public Result<TicketVO> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getById(id);
        return Result.success(TicketConverter.toVO(ticket));
    }

    @GetMapping("/list")
    public Result<PageResultVO<TicketVO>> listTickets(TicketPageQueryDTO dto){
        Long userId = BaseContext.getCurrentId();
        String role = BaseContext.getCurrentRole();
        return Result.success(ticketService.queryPage(dto, userId, role));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        ticketService.updateStatus(id, status);
        return Result.success();
    }

    @GetMapping("/{id}/summary")
    public Result<String> generateSummary(@PathVariable Long id) {
        String summary = ticketService.generateSummary(id);
        return Result.success(summary);
    }

    @PostMapping("/{id}/messages")
    public Result<MessageVO> sendMessage(@PathVariable Long id, @RequestBody @Valid MessageDTO dto) {
        Long userId = BaseContext.getCurrentId();
        TicketMessage message = ticketService.sendMessage(id, userId, dto.getSenderType(), dto.getContent());
        return Result.success(MessageConverter.toVO(message));
    }

    @GetMapping("/{id}/messages")
    public Result<List<MessageVO>> getMessages(@PathVariable Long id) {
        List<TicketMessage> messages = ticketService.getMessages(id);
        return Result.success(MessageConverter.toVOList(messages));
    }
}