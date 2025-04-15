package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:23 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.CartService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.APIResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.CartDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.CartItem;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.CartRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Service
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private APIResponseServiceImpl apiResponseServiceImpl;

    public APIResponse<String> addItemToCart(Integer userId, CartDTO cartDTO) throws JsonProcessingException {
//        Call repository to add item to cart
        Map<String, Object> result = cartRepository.addCartItem(
                userId,
                cartDTO.getProductId(),
                cartDTO.getVariantId(),
                cartDTO.getQuantity()
        );

        int code = (Integer) result.get("CODE");

        if (code == 0) {
            return new APIResponse<>(code, "Item added to cart successfully", null, ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> removeItemFromCart(Integer cartItemId) throws JsonProcessingException {
        // Call repository to remove item from cart
        Map<String, Object> result = cartRepository.removeCartItem(cartItemId);

        int code = (Integer) result.get("CODE");

        if (code == 0) {
            return new APIResponse<>(code, "Item removed from cart successfully", null, ResponseType.SUCCESS
            );
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType()
            );
        }
    }

    @Override
    public APIResponse<String> getCartItems(Integer userId) throws JsonProcessingException {
        Map<String, Object> result = cartRepository.getCartItems(userId);

        int code = (Integer) result.get("CODE");

        if (code == 0) {
            List<CartDTO> cartDTOS = (List<CartDTO>) result.get("CART_ITEMS");
            return new APIResponse<>(
                    code,
                    "Item added to cart successfully",
                    objectMapper.writeValueAsString(cartDTOS),
                    ResponseType.SUCCESS
            );
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> updateQuantityCartItem(Integer cartItemId, Integer quantity) throws JsonProcessingException {
        Map<String, Object> result = cartRepository.updateQuantityCartItem(cartItemId, quantity);
        int code = (Integer) result.get("CODE");

        if (code == 0) {
            String message = quantity == 0 ? "Item removed from cart successfully" : "Cart item quantity updated successfully";
            return new APIResponse<>(code, message, null, ResponseType.SUCCESS
            );
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> updateQuantityCartItemDecrease(Integer cartItemId, Integer decreaseBy) throws JsonProcessingException {
        Map<String, Object> result = cartRepository.updateQuantityCartItemDecrease(cartItemId, decreaseBy);
        int code = (Integer) result.get("CODE");

        if (code == 0) {
            return new APIResponse<>(code, "Cart item quantity decreased successfully", null, ResponseType.SUCCESS
            );
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> getTotalCartItem(Integer cartItemId) throws JsonProcessingException {
        Map<String, Object> result = cartRepository.getTotalCartItem(cartItemId);
        int code = (Integer) result.get("CODE");

        if (code == 0) {
            BigDecimal totalAmount = (BigDecimal) result.get("TOTAL_AMOUNT");
            return new APIResponse<>(code, "Cart item total retrieved successfully", objectMapper.writeValueAsString(totalAmount), ResponseType.SUCCESS
            );
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> getTotalCartItems(Integer userId) throws JsonProcessingException {
        Map<String, Object> result = cartRepository.getTotalCartItems(userId);
        int code = (Integer) result.get("CODE");

        if (code == 0) {
            BigDecimal totalAmount = (BigDecimal) result.get("TOTAL_AMOUNT");
//            log.debug("[CART SERVICE IMPL] totalAmount = {}", totalAmount);
            return new APIResponse<>(code, "Cart total retrieved successfully", objectMapper.writeValueAsString(totalAmount), ResponseType.SUCCESS
            );
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> getQuantitiesCartItems(Integer userId) throws JsonProcessingException {
        Map<String, Object> result = cartRepository.getQuantitiesCartItems(userId);
        int code = (Integer) result.get("CODE");

        if (code == 0) {
            int quantities = (Integer) result.get("TOTAL_QUANTITY");
//            log.debug("[CART SERVICE IMPL] totalAmount = {}", totalAmount);
            return new APIResponse<>(code, "Cart total retrieved successfully", objectMapper.writeValueAsString(quantities), ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }
}
