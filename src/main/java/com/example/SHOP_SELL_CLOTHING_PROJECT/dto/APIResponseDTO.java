package com.example.SHOP_SELL_CLOTHING_PROJECT.dto;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/20
 * Time: 9:00 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * @ 2025. All rights reserved
 */

@Getter
@Setter
@AllArgsConstructor
public class APIResponseDTO {
    @NotNull(message = "Code is required")
    private Integer code;

    @NotBlank(message = "Message is required")
    @Size(max = 255, message = "Message must not exceed 255 characters")
    private String message;

    private String description;

    @NotNull(message = "Response type is required")
    private ResponseType responseType;

    public APIResponseDTO() {
        this.code = 1;
        this.message = "Unexpected exception";
        this.description = "Unexpected exception";
        this.responseType = ResponseType.ERROR;
    }
}
