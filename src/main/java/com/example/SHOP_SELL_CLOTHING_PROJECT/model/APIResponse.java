package com.example.SHOP_SELL_CLOTHING_PROJECT.model;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 12:14 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ 2025. All rights reserved
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T> {
    private int code;
    private String message;
    private T data;
    private ResponseType responseType;
    private LocalDateTime timestamp;

    public APIResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.responseType = ResponseType.ERROR;
        this.timestamp = LocalDateTime.now();
    }

    public APIResponse(int code, String message, T data, ResponseType responseType) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.responseType = responseType;
        this.timestamp = LocalDateTime.now();
    }
}
