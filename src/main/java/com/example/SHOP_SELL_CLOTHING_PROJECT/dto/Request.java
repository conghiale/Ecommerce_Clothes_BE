package com.example.SHOP_SELL_CLOTHING_PROJECT.dto;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 1:20 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.Language;
import lombok.Data;

/**
 * @ 2025. All rights reserved
 */

@Data
public class Request {
    private String partnerCode;
    private String requestId;
    private String orderId;
    private Language lang = Language.EN;
    private long startTime;

    public Request() {
        this.startTime = System.currentTimeMillis();
    }

    public Request(String partnerCode, String orderId, String requestId, Language lang) {
        this.partnerCode = partnerCode;
        this.orderId = orderId;
        this.requestId = requestId;
        this.lang = lang;
        this.startTime = System.currentTimeMillis();
    }
}
