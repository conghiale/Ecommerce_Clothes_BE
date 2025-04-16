package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 11:38 AM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ProductStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.ProductService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ProductDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Product;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ProductVariantDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ 2025. All rights reserved
 */

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<APIResponse<String>> createProduct(@Valid @RequestBody ProductDTO productDTO) throws JsonProcessingException {
//        Product product = convertToProduct(productDTO);
//        String sizes = convertVariantsToSizeString(productDTO.getVariants());
        APIResponse<String> resultData = productService.createProduct(productDTO);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<String>> getProduct(@PathVariable Integer id) throws JsonProcessingException {
        APIResponse<String> resultData = productService.getProductById(id);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<String>> searchProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) throws JsonProcessingException {
        APIResponse<String> resultData = productService.searchProducts(search, categoryId, minPrice, maxPrice, page, pageSize);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @GetMapping("/available")
    public ResponseEntity<APIResponse<String>> getAvailableProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) throws JsonProcessingException {
        APIResponse<String> resultData = productService.getAvailableProducts(page, pageSize);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<String>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductDTO productDTO) throws JsonProcessingException {
//        Product product = convertToProduct(productDTO);
        productDTO.setProductId(id);
        APIResponse<String> resultData = productService.updateProduct(productDTO);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    @DeleteMapping("/delete-products")
    public ResponseEntity<APIResponse<String>> deleteProducts(@Valid @RequestBody ProductDTO productDTO) throws JsonProcessingException {
        APIResponse<String> resultData = productService.deleteProducts(productDTO);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

//    Pending
    @PutMapping("/{id}/status")
    public ResponseEntity<APIResponse<String>> updateProductStatus(
            @PathVariable Integer id,
            @RequestParam String status) throws JsonProcessingException {
        APIResponse<String> resultData = productService.updateProductStatus(id, status);
        return ResponseEntity.ok(new APIResponse<>(resultData.getCode(), resultData.getMessage(), resultData.getData(), resultData.getResponseType()));
    }

    private Product convertToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductsName(productDTO.getProductsName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setProductStatus(ProductStatus.AVAILABLE);

        Category category = new Category();
        category.setCategoryId(productDTO.getCategoryID());
        product.setCategory(category);

        // Calculate total stock from variants
        Integer totalStock = productDTO.getVariants() != null ?
                productDTO.getVariants().stream()
                        .mapToInt(ProductVariantDTO::getStockQuantity)
                        .sum() : 0;
        product.setStockQuantity(totalStock);

        return product;
    }

    private String convertVariantsToSizeString(List<ProductVariantDTO> variants) {
        if (variants == null || variants.isEmpty()) {
            return "";
        }
        return variants.stream()
                .map(ProductVariantDTO::getSize)
                .distinct()
                .sorted()
                .collect(Collectors.joining(","));
    }
}
