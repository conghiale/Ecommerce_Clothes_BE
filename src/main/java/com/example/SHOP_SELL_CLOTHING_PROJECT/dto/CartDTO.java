package com.example.SHOP_SELL_CLOTHING_PROJECT.dto;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 12:16 PM
 */

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ 2025. All rights reserved
 */

@Data
public class CartDTO {
    private Integer cartItemID;

//    @NotNull(message = "Product ID is required")
    private Integer productId;

    private Integer cartID;
    private Integer userID;

//    @NotNull(message = "Variant ID is required")
    private Integer variantId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String productName;
    private BigDecimal price;
    private String size;
    private String imageURL;
}
