package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 6:54 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.ChatBoxService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.CategoryDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ChatBoxDTO;
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
@RequestMapping("/api/chat-box")
@Validated
public class ChatBoxController {
    @Autowired
    private ChatBoxService chatBoxService;

    @PostMapping("/{userId}/process")
    public ResponseEntity<APIResponse<String>> chatBoxProcess(@Valid @PathVariable Integer userId, @Valid @RequestBody ChatBoxDTO chatBoxDTO) throws JsonProcessingException {
        chatBoxDTO.setUserId(userId);
        APIResponse<String> resultData = chatBoxService.processUserMessage(chatBoxDTO);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }
}
