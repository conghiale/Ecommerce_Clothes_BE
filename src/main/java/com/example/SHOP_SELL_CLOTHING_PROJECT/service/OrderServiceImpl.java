package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:23 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.OrderService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.APIResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.OrderDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Order;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private APIResponseServiceImpl apiResponseServiceImpl;

    @Override
    public APIResponse<String> createOrder(Integer userId, OrderDTO orderDTO) throws JsonProcessingException {
        Map<String, Object> result = orderRepository.createOrder(
                userId,
                orderDTO.getPaymentMethod().toString(),
                orderDTO.getShippingAddress(),
                orderDTO.getBillingAddress()
        );

        int code = (Integer) result.get("CODE");

        if (code == 0) {
            Integer orderId = (Integer) result.get("ORDER_ID");
            return new APIResponse<>(code, "Order created successfully", objectMapper.writeValueAsString(orderId), ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }
}
