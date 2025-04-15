package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/20
 * Time: 8:30 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.IAPIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.APIResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.APIResponseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Service
public class APIResponseServiceImpl implements IAPIResponse {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private APIResponseRepository apiResponseRepository;

    @Override
    public APIResponse<String> createAPIResponse(Integer code, String message, String description, String responseType) throws JsonProcessingException {
        Map<String, Object> result = apiResponseRepository.createAPIResponse(code, message, description, responseType);

        int statusCode = (Integer) result.get("STATUS_CODE");
        int categoryId = result.get("RESPONSE_ID") == null ? -1 : (Integer) result.get("RESPONSE_ID");

        if (statusCode == 0)
            return new APIResponse<>(statusCode, "API Response created successfully", objectMapper.writeValueAsString(categoryId), ResponseType.SUCCESS);
        else {
            APIResponse<String> apiResponse = getAPIResponseByCode(statusCode);
            APIResponseDTO apiResponseDTO = new APIResponseDTO();
            if (apiResponse != null && apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                apiResponseDTO = objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class);
            }
            return new APIResponse<>(statusCode, apiResponseDTO.getMessage(), null, ResponseType.ERROR);
        }
    }

    @Override
    public APIResponse<String> updateAPIResponse(Integer responseId, Integer code, String message, String description, String responseType) throws JsonProcessingException {
        Map<String, Object> result = apiResponseRepository.updateAPIResponse(
                responseId,
                code,
                message,
                description,
                responseType
        );

        Integer statusCode = (Integer) result.get("STATUS_CODE");

        if (statusCode == 0) {
            return new APIResponse<>(statusCode, "API Response updated successfully", null, ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = getAPIResponseByCode(statusCode);
            APIResponseDTO apiResponseDTO = new APIResponseDTO();
            if (apiResponse != null && apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                apiResponseDTO = objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class);
            }
            return new APIResponse<>(statusCode, apiResponseDTO.getMessage(), null, ResponseType.ERROR);
        }
    }

    @Override
    public APIResponse<String> deleteAPIResponse(Integer responseId) throws JsonProcessingException {
        Map<String, Object> result = apiResponseRepository.deleteAPIResponse(responseId);
        Integer statusCode = (Integer) result.get("STATUS_CODE");

        if (statusCode == 0) {
            return new APIResponse<>(statusCode, "API Response deleted successfully", null, ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = getAPIResponseByCode(statusCode);
            APIResponseDTO apiResponseDTO = new APIResponseDTO();
            if (apiResponse != null && apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                apiResponseDTO = objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class);
            }
            return new APIResponse<>(statusCode, apiResponseDTO.getMessage(), null, ResponseType.ERROR);
        }
    }

    @Override
    public APIResponse<String> getAPIResponseByCode(Integer code) throws JsonProcessingException {
        Map<String, Object> result = apiResponseRepository.getAPIResponseByCode(code);

        Integer statusCode = (Integer) result.get("STATUS_CODE");
        Object response = result.get("RESPONSE");

        if (statusCode == 0 && response != null) {
            Object[] responseArray = (Object[]) response;
            APIResponseDTO apiResponseDTO = new APIResponseDTO(
                    (Integer) responseArray[1],    // CODE
                    (String) responseArray[2],    // MESSAGE
                    (String) responseArray[3],    // DESCRIPTION
                    ResponseType.valueOf((String) responseArray[4])  // RESPONSE_TYPE
            );
            return new APIResponse<>(statusCode, "API Response retrieved successfully", objectMapper.writeValueAsString(apiResponseDTO), ResponseType.SUCCESS);
        } else {
            return new APIResponse<>(statusCode, "API Response not found", null, ResponseType.ERROR);
        }
    }
}
