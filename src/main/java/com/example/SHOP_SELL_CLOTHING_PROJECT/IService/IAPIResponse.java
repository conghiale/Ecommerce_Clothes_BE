package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:25 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @ 2025. All rights reserved
 */

public interface IAPIResponse {
    APIResponse<String> createAPIResponse(Integer code, String message, String description, String responseType) throws JsonProcessingException;
    APIResponse<String> updateAPIResponse(Integer responseId, Integer code, String message,
                                          String description, String responseType) throws JsonProcessingException;
    APIResponse<String> deleteAPIResponse(Integer responseId) throws JsonProcessingException;
    APIResponse<String> getAPIResponseByCode(Integer code) throws JsonProcessingException;
}
