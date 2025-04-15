package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/22
 * Time: 11:17 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.PaymentService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.APIResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.OrderRepository;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Service
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private APIResponseServiceImpl apiResponseServiceImpl;

    @Override
    public APIResponse<String> paymentProcess(Integer orderId, PaymentDTO paymentDTO) throws JsonProcessingException {
        Map<String, Object> result = paymentRepository.paymentProcess(
                orderId,
                paymentDTO.getPaymentMethod(),
                paymentDTO.getTransactionCode()
        );

        int code = (Integer) result.get("CODE");

        if (code == 0) {
            return new APIResponse<>(code, "Payment processed successfully", null, ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }
}
