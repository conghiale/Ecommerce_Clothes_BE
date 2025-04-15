package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:24 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.SupportTicketService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.APIResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.TicketResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.TicketSupportDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.SupportTicket;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.TicketResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.SupportTicketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Service
@Transactional
public class SupportTicketServiceImpl implements SupportTicketService {
    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private APIResponseServiceImpl apiResponseServiceImpl;

    @Override
    public APIResponse<String> createTicket(TicketSupportDTO ticketSupportDTO) throws JsonProcessingException {
        Map<String, Object> result = supportTicketRepository.createTicket(
                ticketSupportDTO.getUserId(),
                ticketSupportDTO.getOrderId(),
                ticketSupportDTO.getSubject(),
                ticketSupportDTO.getMessage()
        );

        int code = (Integer) result.get("CODE");

        if (code == 0) {
            Integer ticketId = (Integer) result.get("TICKET_ID");
            return new APIResponse<>(code, "Create ticket support successfully", objectMapper.writeValueAsString(ticketId), ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> createResponse(TicketResponseDTO responseDTO) throws JsonProcessingException {
        Map<String, Object> result = supportTicketRepository.createTicketResponse(
                responseDTO.getTicketId(),
                responseDTO.getUserId(),
                responseDTO.getMessage()
        );

        int code = (Integer) result.get("CODE");

        if (code == 0) {
            Integer responseId = (Integer) result.get("RESPONSE_ID");
            return new APIResponse<>(code, "Create ticket successfully", objectMapper.writeValueAsString(responseId), ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> getTicketSupports(Integer userId) throws JsonProcessingException {
        Map<String, Object> result = supportTicketRepository.getTicketSupports(userId);

        int code = (Integer) result.get("CODE");

        if (code == 0 && result.get("TICKET_SUPPORT") != null && !result.get("TICKET_SUPPORT").equals("")) {
            List<TicketSupportDTO> ticketSupportDTOS = (List<TicketSupportDTO>) result.get("TICKET_SUPPORT");
            return new APIResponse<>(code, "Get ticket support successfully", objectMapper.writeValueAsString(ticketSupportDTOS), ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> getTicketResponses(Integer ticketSupportId) throws JsonProcessingException {
        Map<String, Object> result =  supportTicketRepository.getTicketResponses(ticketSupportId);

        int code = (Integer) result.get("CODE");

        if (code == 0 && result.get("TICKET_RESPONSE") != null && !result.get("TICKET_RESPONSE").equals("")) {
            List<TicketResponseDTO> ticketResponseDTOS = (List<TicketResponseDTO>) result.get("TICKET_RESPONSE");
            return new APIResponse<>(code, "Get ticket response successfully", objectMapper.writeValueAsString(ticketResponseDTOS), ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }

    }
}
