package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:25 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.OrderDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @ 2025. All rights reserved
 */

public interface OrderService {
    APIResponse<String> createOrder(Integer userId, OrderDTO orderDTO) throws JsonProcessingException;
    APIResponse<String> getOrders(Integer userId, Integer page, Integer pageSize) throws JsonProcessingException;
    APIResponse<String> getOrderDetail(Integer userId, Integer orderId) throws JsonProcessingException;
}
