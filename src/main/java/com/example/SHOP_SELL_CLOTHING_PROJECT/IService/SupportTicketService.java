package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:25 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.TicketResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.TicketSupportDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.SupportTicket;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.TicketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * @ 2025. All rights reserved
 */

public interface SupportTicketService {
    APIResponse<String> createTicket(TicketSupportDTO ticketSupportDTO) throws JsonProcessingException;
    APIResponse<String> createResponse(TicketResponseDTO responseDTO) throws JsonProcessingException;
    APIResponse<String> getTicketSupports(Integer userId) throws JsonProcessingException;
    APIResponse<String> getTicketResponses(Integer ticketSupportId) throws JsonProcessingException;
}
