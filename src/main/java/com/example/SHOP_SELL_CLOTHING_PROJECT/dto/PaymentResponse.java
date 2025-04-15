package com.example.SHOP_SELL_CLOTHING_PROJECT.dto;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 1:22 PM
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
public class PaymentResponse extends Response {
    private String requestId;

    private Long amount;

    private String payUrl;

    private String shortLink;

    private String deeplink;

    private String qrCodeUrl;

    private String deeplinkWebInApp;

    private Long transId;

    private String applink;

    private String partnerClientId;

    private String bindingUrl;

    private String deeplinkMiniApp;

    public PaymentResponse(Integer resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }
}
