package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:46 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.CategoryDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Category;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * @ 2025. All rights reserved
 */

public interface CategoryService {
    APIResponse<String> createCategory(CategoryDTO categoryDTO) throws JsonProcessingException;
    APIResponse<String> updateCategory(Integer categoryId, Category category) throws JsonProcessingException;
    APIResponse<String> deleteCategory(Integer categoryId) throws JsonProcessingException;
    APIResponse<String> getAllCategories() throws JsonProcessingException;
    APIResponse<String> getCategoryById(Integer categoryId) throws JsonProcessingException;
    APIResponse<String> getRootCategories() throws JsonProcessingException;
    APIResponse<String> getSubCategories(Integer parentId) throws JsonProcessingException;
}
