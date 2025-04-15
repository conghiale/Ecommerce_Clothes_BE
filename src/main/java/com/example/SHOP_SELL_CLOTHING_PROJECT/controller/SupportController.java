package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 1:04 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.TicketStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.SupportTicketService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.TicketSupportDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.SupportTicket;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.TicketResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.TicketResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Order;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ 2025. All rights reserved
 */

@RestController
@RequestMapping("/api/support")
@Validated
public class SupportController {
    @Autowired
    private SupportTicketService supportTicketService;

    @PostMapping("/{userId}/ticket-support")
    public ResponseEntity<APIResponse<String>> createTicket(
            @PathVariable Integer userId,
            @Valid @RequestBody TicketSupportDTO ticketSupportDTO) throws JsonProcessingException {
//        SupportTicket ticket = convertToTicketSupport(ticketSupportDTO);
        ticketSupportDTO.setUserId(userId);
        APIResponse<String> response = supportTicketService.createTicket(ticketSupportDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{ticketId}/ticket-response")
    public ResponseEntity<APIResponse<String>> createResponse(
            @PathVariable Integer ticketId,
            @Valid @RequestBody TicketResponseDTO responseDTO) throws JsonProcessingException {
//        TicketResponse response = convertToTicketResponse(responseDTO, ticketId);
        responseDTO.setTicketId(ticketId);
        APIResponse<String> response = supportTicketService.createResponse(responseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/ticket-support")
    public ResponseEntity<APIResponse<String>> getUserTickets(@PathVariable Integer userId) throws JsonProcessingException {
        APIResponse<String> response = supportTicketService.getTicketSupports(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tickets/{ticketId}/responses")
    public ResponseEntity<APIResponse<String>> getTicketResponses(@PathVariable Integer ticketId) throws JsonProcessingException {
        APIResponse<String> response = supportTicketService.getTicketResponses(ticketId);
        return ResponseEntity.ok(response);
    }

    private SupportTicket convertToTicketSupport(TicketSupportDTO ticketDTO) {
        SupportTicket ticket = new SupportTicket();

        // Set basic ticket info
        ticket.setSubject(ticketDTO.getSubject());
        ticket.setMessage(ticketDTO.getMessage());
        ticket.setStatus(TicketStatus.OPEN);

        // Set user
        User user = new User();
        user.setUserId(Integer.parseInt("1"));
        ticket.setUser(user);

        // Set order if provided
        if (ticketDTO.getOrderId() != null) {
            Order order = new Order();
            order.setOrderId(ticketDTO.getOrderId());
            ticket.setOrder(order);
        }

        return ticket;
    }

    private TicketResponse convertToTicketResponse(TicketResponseDTO ticketDTO, Integer ticketId) {
        TicketResponse response = new TicketResponse();

        // Set response message
        response.setMessage(ticketDTO.getMessage());

        // Set ticket reference
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketId(ticketId);
        response.setTicket(ticket);

        // Set user
        User user = new User();
        user.setUserId(Integer.parseInt("1"));
        response.setUser(user);

        // Set creation time
        response.setCreatedAt(LocalDateTime.now());

        return response;
    }

}
