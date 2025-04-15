package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:25 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.CartDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.CartItem;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * @ 2025. All rights reserved
 */

public interface CartService {
    APIResponse<String> addItemToCart(Integer userId, CartDTO cartDTO) throws JsonProcessingException;
    APIResponse<String> removeItemFromCart(Integer cartItemId) throws JsonProcessingException;
    APIResponse<String> getCartItems(Integer userId) throws JsonProcessingException;
    APIResponse<String> updateQuantityCartItem(Integer cartItemId, Integer quantity) throws JsonProcessingException;
    APIResponse<String> updateQuantityCartItemDecrease(Integer cartItemId, Integer decreaseBy) throws JsonProcessingException;
    APIResponse<String> getTotalCartItem(Integer cartItemId) throws JsonProcessingException;
    APIResponse<String> getTotalCartItems(Integer userId) throws JsonProcessingException;
    APIResponse<String> getQuantitiesCartItems(Integer userId) throws JsonProcessingException;
}
