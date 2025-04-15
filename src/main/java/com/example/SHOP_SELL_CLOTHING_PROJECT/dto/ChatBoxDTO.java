package com.example.SHOP_SELL_CLOTHING_PROJECT.dto;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 6:50 PM
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ 2025. All rights reserved
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBoxDTO {
    private Integer userId;
    private Integer conversationId;
    private Integer userMessageId;
    private Integer botMessageId;
    private String title;
    private String userMessage;
    private String botResponse;
    private Integer replyToUserMessageId;
}
