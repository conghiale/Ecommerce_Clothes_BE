package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/20
 * Time: 9:21 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.APIResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.service.APIResponseServiceImpl;
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
@RequestMapping("/api/api-responses")
@Validated
public class APIResponseController {
    @Autowired
    private APIResponseServiceImpl apiResponseService;

    @PostMapping("/")
    public ResponseEntity<APIResponse<String>> createAPIResponse(@Valid @RequestBody APIResponseDTO apiResponseDTO) throws JsonProcessingException {
        APIResponse<String> result = apiResponseService.createAPIResponse(
            apiResponseDTO.getCode(),
            apiResponseDTO.getMessage(),
            apiResponseDTO.getDescription(),
            apiResponseDTO.getResponseType().toString()
        );
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{responseId}")
    public ResponseEntity<APIResponse<String>> updateAPIResponse(
            @PathVariable Integer responseId,
            @Valid @RequestBody APIResponseDTO apiResponseDTO) throws JsonProcessingException {
        APIResponse<String> result = apiResponseService.updateAPIResponse(
            responseId,
            apiResponseDTO.getCode(),
            apiResponseDTO.getMessage(),
            apiResponseDTO.getDescription(),
            apiResponseDTO.getResponseType().toString()
        );
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{responseId}")
    public ResponseEntity<APIResponse<String>> deleteAPIResponse(@PathVariable Integer responseId) throws JsonProcessingException {
        APIResponse<String> result = apiResponseService.deleteAPIResponse(responseId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{code}")
    public ResponseEntity<APIResponse<String>> getAPIResponseByCode(@PathVariable Integer code) throws JsonProcessingException {
        APIResponse<String> result = apiResponseService.getAPIResponseByCode(code);
        return ResponseEntity.ok(result);
    }
}
