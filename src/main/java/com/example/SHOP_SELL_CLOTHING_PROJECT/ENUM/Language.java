package com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 1:15 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.shared.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * @ 2025. All rights reserved
 */

public enum Language {

    @SerializedName("vi")
    VI(Constants.LANGUAGE_VI),

    @SerializedName("en")
    EN(Constants.LANGUAGE_EN);

    private final String value;

    Language(String value) {
        this.value = value;
    }

    public static Language findByName(String name) {
        for (Language type : values()) {
            if (type.getLanguage().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public String getLanguage() { return value; }

}
