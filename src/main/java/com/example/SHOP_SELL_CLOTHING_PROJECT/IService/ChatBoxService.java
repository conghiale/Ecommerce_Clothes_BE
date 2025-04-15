package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 6:54 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ChatBoxDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @ 2025. All rights reserved
 */

public interface ChatBoxService {
    APIResponse<String> chatConversationGetOrCreate(ChatBoxDTO chatBoxDTO) throws JsonProcessingException;
    APIResponse<String> chatMessageProcess(ChatBoxDTO chatBoxDTO) throws JsonProcessingException;
    APIResponse<String> chatBotResponseSave(ChatBoxDTO chatBoxDTO) throws JsonProcessingException;
    APIResponse<String> processUserMessage(ChatBoxDTO chatBoxDTO) throws JsonProcessingException;
}
