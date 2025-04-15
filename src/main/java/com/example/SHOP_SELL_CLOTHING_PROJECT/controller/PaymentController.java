package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 11:50 AM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.OrderService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.PaymentService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.ProductService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.OrderDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentDTO;
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
@RequestMapping("/api/payments")
@Validated
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<APIResponse<String>> processPayment(@PathVariable Integer orderId, @Valid @RequestBody PaymentDTO paymentDTO) throws JsonProcessingException {
        APIResponse<String> resultData =paymentService.paymentProcess(orderId, paymentDTO);
        return ResponseEntity.ok(resultData);
    }
}
