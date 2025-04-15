package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 11:18 AM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentRequest;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;

/**
 * @ 2025. All rights reserved
 */

public interface MomoService {
    APIResponse<String> createPaymentRequest(PaymentRequest paymentRequest);
    APIResponse<String> checkPaymentStatus(PaymentResponse paymentResponse);
}
