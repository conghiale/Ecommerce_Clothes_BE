package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:09 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ProductDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ 2025. All rights reserved
 */

public interface ProductService {
    APIResponse<String> createProduct(ProductDTO productDTO) throws JsonProcessingException;
    APIResponse<String> getProductById(Integer id) throws JsonProcessingException;
    APIResponse<String> searchProducts(String searchTerm, Integer categoryId,
                                 BigDecimal minPrice, BigDecimal maxPrice,
                                 Integer page, Integer pageSize) throws JsonProcessingException;
    APIResponse<String> getAvailableProducts(Integer page, Integer pageSize) throws JsonProcessingException;
    APIResponse<String> updateProductStatus(Integer productId, String status) throws JsonProcessingException;
    APIResponse<String> updateProduct(ProductDTO productDTO) throws JsonProcessingException;
    APIResponse<String> deleteProducts(ProductDTO productDTO) throws JsonProcessingException;
}
