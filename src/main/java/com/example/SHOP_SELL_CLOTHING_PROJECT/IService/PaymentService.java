package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/22
 * Time: 11:17 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @ 2025. All rights reserved
 */

public interface PaymentService {
    APIResponse<String> paymentProcess(Integer orderId, PaymentDTO paymentDTO) throws JsonProcessingException;
}
