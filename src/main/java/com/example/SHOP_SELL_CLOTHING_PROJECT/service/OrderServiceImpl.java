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

import java.util.List;
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

    @Override
    public APIResponse<String> getOrders(Integer userId, Integer page, Integer pageSize) throws JsonProcessingException {
        Map<String, Object> result = orderRepository.getOrders(
                userId,
                page,
                pageSize
        );

        int code = (Integer) result.get("CODE");

        if (code == 0 && result.get("ORDERS") != null) {
            List<OrderDTO> orderDTOS = (List<OrderDTO>) result.get("ORDERS");
            return new APIResponse<>(
                    code,
                    "Get all orders successfully",
                    objectMapper.writeValueAsString(orderDTOS),
                    ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> getOrderDetail(Integer userId, Integer orderId) throws JsonProcessingException {
        Map<String, Object> result = orderRepository.getOrderDetail(
                userId,
                orderId
        );

        int code = (Integer) result.get("CODE");

        if (code == 0 && result.get("ORDER") != null) {
            OrderDTO orderDTO = (OrderDTO) result.get("ORDER");
            return new APIResponse<>(
                    code,
                    "Get order detail successfully",
                    objectMapper.writeValueAsString(orderDTO),
                    ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }
}
