package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 10:21 AM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.MomoService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentRequest;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ 2025. All rights reserved
 */

@RequestMapping("/api/momo")
@RestController
public class MomoController {
    @Autowired
    private MomoService momoServiceImpl;

    @PostMapping("/{orderId}")
    public ResponseEntity<APIResponse<String>> testPayment(@PathVariable Integer orderId, @RequestBody PaymentRequest paymentRequest) {
        paymentRequest.setOrderId(String.valueOf(orderId));
        APIResponse<String> resultData = momoServiceImpl.createPaymentRequest(paymentRequest);
        return ResponseEntity.ok(resultData);
    }

    @GetMapping("/order-status/{orderId}") // OrderID của sử dụng cho MOMO là String
    public ResponseEntity<APIResponse<String>> checkPaymentStatus(@PathVariable String orderId) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setOrderId(orderId);
        APIResponse<String> resultData = momoServiceImpl.checkPaymentStatus(paymentResponse);
        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/IPN")
    public ResponseEntity<APIResponse<String>> instantPaymentNotification(@RequestParam PaymentResponse paymentResponse) {
        APIResponse<String> resultData = momoServiceImpl.checkPaymentStatus(paymentResponse);
        return ResponseEntity.ok(resultData);
    }
}
