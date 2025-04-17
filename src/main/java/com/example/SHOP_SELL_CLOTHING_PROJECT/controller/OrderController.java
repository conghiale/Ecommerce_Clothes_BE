package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 11:50 AM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.OrderService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.*;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse<String>> getOrdersByUser(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) throws JsonProcessingException {
        APIResponse<String> resultData = orderService.getOrdersByUser(userId, page, pageSize);
        return ResponseEntity.ok(resultData);
    }

    @GetMapping("/")
    public ResponseEntity<APIResponse<String>> getOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String paymentStatus) throws JsonProcessingException {
        // Convert "null" string to null
        String finalOrderStatus = "null".equalsIgnoreCase(orderStatus) ? null : orderStatus;
        String finalPaymentStatus = "null".equalsIgnoreCase(paymentStatus) ? null : paymentStatus;
        APIResponse<String> resultData = orderService.getOrders(page, pageSize, finalOrderStatus, finalPaymentStatus);
        return ResponseEntity.ok(resultData);
    }

    @GetMapping("/{userId}/order-detail")
    public ResponseEntity<APIResponse<String>> getOrderDetail(
            @PathVariable Integer userId,
            @RequestParam Integer orderId) throws JsonProcessingException {
        APIResponse<String> resultData = orderService.getOrderDetail(userId, orderId);
        return ResponseEntity.ok(resultData);
    }
}
