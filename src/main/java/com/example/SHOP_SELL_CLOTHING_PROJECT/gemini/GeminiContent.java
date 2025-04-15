package com.example.SHOP_SELL_CLOTHING_PROJECT.gemini;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 8:05 PM
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ 2025. All rights reserved
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeminiContent {
    private List<GeminiPart> parts;
}
