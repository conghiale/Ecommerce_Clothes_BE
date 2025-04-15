package com.example.SHOP_SELL_CLOTHING_PROJECT.dto;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 1:21 PM
 */

import lombok.Data;

/**
 * @ 2025. All rights reserved
 */

@Data
public class Response {
    protected long responseTime;
    protected String message;
    private String partnerCode;
    private String orderId;
    protected Integer resultCode;

    public Response() {
        this.responseTime = System.currentTimeMillis();
    }

    public long getResponseTime() {
        return  System.currentTimeMillis();
    }
}
