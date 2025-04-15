package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 11:50 AM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.OrderStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.PaymentMethod;
import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.PaymentStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.OrderService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.ProductService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.*;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Order;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @ 2025. All rights reserved
 */

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}/create-order")
    public ResponseEntity<APIResponse<String>> createOrder(@PathVariable Integer userId, @Valid @RequestBody OrderDTO orderDTO) throws JsonProcessingException {
        APIResponse<String> resultData = orderService.createOrder(userId, orderDTO);
        return ResponseEntity.ok(resultData);
    }
}
