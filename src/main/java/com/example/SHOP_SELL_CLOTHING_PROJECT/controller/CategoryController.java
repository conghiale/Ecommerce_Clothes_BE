package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 12:54 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.CategoryService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.CategoryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ 2025. All rights reserved
 */

@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/")
    public ResponseEntity<APIResponse<String>> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws JsonProcessingException {
        APIResponse<String> resultData = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<String>> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryDTO categoryDTO) throws JsonProcessingException {
        Category category = convertToCategory(categoryDTO);
        APIResponse<String> resultData = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteCategory(@PathVariable Integer id) throws JsonProcessingException {
        APIResponse<String> resultData = categoryService.deleteCategory(id);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @GetMapping("/")
    public ResponseEntity<APIResponse<String>> getAllCategories() throws JsonProcessingException {
        APIResponse<String> resultData = categoryService.getAllCategories();
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<String>> getCategoryById(@PathVariable Integer id) throws JsonProcessingException {
        APIResponse<String> resultData = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

//    Pending
    @GetMapping("/root")
    public ResponseEntity<APIResponse<String>> getRootCategories() throws JsonProcessingException {
        APIResponse<String> resultData = categoryService.getRootCategories();
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

//    Pending
    @GetMapping("/{id}/children")
    public ResponseEntity<APIResponse<String>> getChildCategories(@PathVariable Integer id) throws JsonProcessingException {
        APIResponse<String> resultData = categoryService.getSubCategories(id);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    private Category convertToCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setCategoryId(dto.getCategoryId());
        category.setCategoriesName(dto.getCategoriesName());
        category.setDescription(dto.getDescription());
        if (dto.getParentId() != null) {
            Category parent = new Category();
            parent.setCategoryId(dto.getParentId());
            category.setParent(parent);
        }
        return category;
    }
}
