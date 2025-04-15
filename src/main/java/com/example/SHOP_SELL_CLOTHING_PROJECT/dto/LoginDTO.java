package com.example.SHOP_SELL_CLOTHING_PROJECT.dto;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 12:15 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.AuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ 2025. All rights reserved
 */

@Data
public class LoginDTO {
    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Authentication provider is required")
    private AuthProvider authProvider;
}
