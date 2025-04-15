package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:46 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.CategoryService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.APIResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.CategoryDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.CategoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.transaction.Transactional;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ 2025. All rights reserved
 */

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private APIResponseServiceImpl apiResponseServiceImpl;

    @Override
    public APIResponse<String> createCategory(CategoryDTO categoryDTO) throws JsonProcessingException {
        Map<String, Object> result = categoryRepository.createCategory(
                categoryDTO.getCategoriesName(),
                categoryDTO.getDescription(),
                categoryDTO.getParentId()
        );

        int code = (Integer) result.get("CODE");
        int categoryId = result.get("CATEGORY_ID") == null ? -1 : (Integer) result.get("CATEGORY_ID");

        APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
        APIResponseDTO apiResponseDTO = apiResponse != null ? objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) : new APIResponseDTO();

        return new APIResponse<>(code, apiResponseDTO.getMessage(), objectMapper.writeValueAsString(categoryId), apiResponseDTO.getResponseType());
    }

    @Override
    public APIResponse<String> updateCategory(Integer categoryId, Category category) throws JsonProcessingException {
        Map<String, Object> result = categoryRepository.updateCategory(
                categoryId,
                category.getCategoriesName(),
                category.getDescription(),
                category.getParent() != null ? category.getParent().getCategoryId() : null
        );

        int code = (Integer) result.get("CODE");

        APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
        APIResponseDTO apiResponseDTO = apiResponse != null ? 
                objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) : 
                new APIResponseDTO();

        return new APIResponse<>(code, apiResponseDTO.getMessage(),null, apiResponseDTO.getResponseType()
        );
    }

    @Override
    public APIResponse<String> deleteCategory(Integer categoryId) throws JsonProcessingException {
        Map<String, Object> result = categoryRepository.deleteCategory(categoryId);
        
        int code = (Integer) result.get("CODE");
    
        APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
        APIResponseDTO apiResponseDTO = apiResponse != null ? 
                objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) : 
                new APIResponseDTO();
    
        return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType()
        );
    }

    @Override
    public APIResponse<String> getAllCategories() throws JsonProcessingException {
        Map<String, Object> result = categoryRepository.getAllCategories();
    
        int code = (Integer) result.get("CODE");
        List<Category> categories = (List<Category>) result.get("CATEGORIES");
    
        if (code == 0 && categories != null) {
            // Configure ObjectMapper to handle bidirectional relationships
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            
            // Create a custom view of categories for serialization
            List<Map<String, Object>> categoryDTOs = categories.stream()
                .map(category -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("categoryId", category.getCategoryId());
                    dto.put("categoriesName", category.getCategoriesName());
                    dto.put("description", category.getDescription());
                    dto.put("parentId", category.getParent() != null ? category.getParent().getCategoryId() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    
            return new APIResponse<>(
                code,
                "Categories retrieved successfully",
                objectMapper.writeValueAsString(categoryDTOs),
                ResponseType.SUCCESS
            );
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ? 
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) : 
                    new APIResponseDTO();
    
            return new APIResponse<>(
                code,
                apiResponseDTO.getMessage(),
                null,
                apiResponseDTO.getResponseType()
            );
        }
    }

    @Override
    public APIResponse<String> getCategoryById(Integer categoryId) throws JsonProcessingException {
        Map<String, Object> result = categoryRepository.getCategoryById(categoryId);
        
        int code = (Integer) result.get("CODE");
        Category category = (Category) result.get("CATEGORY");
    
        if (code == 0 && category != null) {
            return new APIResponse<>(
                code,
                "Category retrieved successfully",
                objectMapper.writeValueAsString(category),
                ResponseType.SUCCESS
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
    public APIResponse<String> getRootCategories() throws JsonProcessingException {
        Map<String, Object> result = categoryRepository.getRootCategories();
        
        int code = (Integer) result.get("CODE");

        List<CategoryDTO> rootCategories = null;
        if (result.get("ROOT_CATEGORIES") != null)
            rootCategories = (List<CategoryDTO>) result.get("ROOT_CATEGORIES");
    
        if (code == 0 && rootCategories != null) {
            return new APIResponse<>(
                code,
                "Root categories retrieved successfully",
                objectMapper.writeValueAsString(rootCategories),
                ResponseType.SUCCESS
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
    public APIResponse<String> getSubCategories(Integer parentId) throws JsonProcessingException {
        Map<String, Object> result = categoryRepository.getSubCategories(parentId);
        
        int code = (Integer) result.get("CODE");
        
        List<CategoryDTO> subCategories = null;
        if (result.get("SUB_CATEGORIES") != null) {
            subCategories = (List<CategoryDTO>) result.get("SUB_CATEGORIES");
        }
    
        if (code == 0 && subCategories != null) {
            return new APIResponse<>(
                code,
                "Sub categories retrieved successfully",
                objectMapper.writeValueAsString(subCategories),
                ResponseType.SUCCESS
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
}
