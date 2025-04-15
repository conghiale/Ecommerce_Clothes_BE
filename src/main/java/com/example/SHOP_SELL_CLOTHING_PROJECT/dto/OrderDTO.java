package com.example.SHOP_SELL_CLOTHING_PROJECT.dto;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 12:18 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.OrderStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.PaymentMethod;
import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.PaymentStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDTO {
    private Integer orderId;

//    @NotNull(message = "Product ID is required")
    private Integer productId;

//    @NotNull(message = "Variant ID is required")
    private Integer variantId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;

    private String shippingAddress;

    private String billingAddress;
}
