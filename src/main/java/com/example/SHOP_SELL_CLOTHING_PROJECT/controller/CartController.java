package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 11:47 AM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.CartService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.CartDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.CartItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ 2025. All rights reserved
 */

@RestController
@RequestMapping("/api/cart")
@Validated
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/cart-item/{userId}")
    public ResponseEntity<APIResponse<String>> addToCart(@PathVariable Integer userId, @Valid @RequestBody CartDTO cartDTO) throws JsonProcessingException {
        APIResponse<String> resultData = cartService.addItemToCart(userId, cartDTO);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @DeleteMapping("/cart-item/{itemId}")
    public ResponseEntity<APIResponse<String>> removeFromCart(@PathVariable Integer itemId) throws JsonProcessingException {
        APIResponse<String> resultData = cartService.removeItemFromCart(itemId);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @GetMapping("/cart-items/{userId}")
    public ResponseEntity<APIResponse<String>> getCartItems(@PathVariable Integer userId) throws JsonProcessingException {
        APIResponse<String> resultData = cartService.getCartItems(userId);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @PutMapping("/cart-items/{itemId}")
    public ResponseEntity<APIResponse<String>> updateQuantityCartItem(@PathVariable Integer itemId, @Valid @RequestBody CartDTO cartDTO) throws JsonProcessingException {
        APIResponse<String> resultData = cartService.updateQuantityCartItem(itemId, cartDTO.getQuantity());
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @PutMapping("/cart-items/decrease/{itemId}")
    public ResponseEntity<APIResponse<String>> updateQuantityCartItemDecrease(@PathVariable Integer itemId, @Valid @RequestBody CartDTO cartDTO) throws JsonProcessingException {
        APIResponse<String> resultData = cartService.updateQuantityCartItemDecrease(itemId, cartDTO.getQuantity());
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @GetMapping("/cart-items/total-item/{itemId}")
    public ResponseEntity<APIResponse<String>> getTotalCartItem(@PathVariable Integer itemId) throws JsonProcessingException {
        APIResponse<String> resultData = cartService.getTotalCartItem(itemId);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @GetMapping("/total/{userId}")
    public ResponseEntity<APIResponse<String>> getTotalCartItems(@PathVariable Integer userId) throws JsonProcessingException {
        APIResponse<String> resultData = cartService.getTotalCartItems(userId);
        return ResponseEntity.ok(resultData);
    }

    @GetMapping("/cart-items/{userId}/quantities")
    public ResponseEntity<APIResponse<String>> getQuantitiesCartItems(@PathVariable Integer userId) throws JsonProcessingException {
        APIResponse<String> resultData = cartService.getQuantitiesCartItems(userId);
        return ResponseEntity.ok(resultData);
    }
}
