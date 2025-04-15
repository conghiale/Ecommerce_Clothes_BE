package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:10 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.ProductService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.APIResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ProductDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Product;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ 2025. All rights reserved
 */

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private APIResponseServiceImpl apiResponseServiceImpl;

    @Override
    public APIResponse<String> createProduct(ProductDTO productDTO) throws JsonProcessingException {
//         Convert variants to JSON string
        List<Map<String, Object>> variantsList = productDTO.getVariants().stream()
            .map(variant -> {
                Map<String, Object> variantMap = new HashMap<>();
                variantMap.put("size", variant.getSize());
                variantMap.put("quantity", variant.getStockQuantity());
                return variantMap;
            })
            .collect(Collectors.toList());
        
        String sizesJson = objectMapper.writeValueAsString(variantsList);

        // Gọi repository để tạo sản phẩm
        Map<String, Object> result = productRepository.createProduct(
                productDTO.getProductsName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getCategoryID(),
                sizesJson
        );

        int code = (Integer) result.get("CODE");
        int productId = result.get("PRODUCT_ID") == null ? -1 : (Integer) result.get("PRODUCT_ID");

        if (code == 0) {
            return new APIResponse<>(
                    code,
                    "Create successful products",
                    objectMapper.writeValueAsString(productId),
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
    public APIResponse<String> getProductById(Integer productId) throws JsonProcessingException {
        Map<String, Object> result = productRepository.getProductById(productId);
        int code = (Integer) result.get("CODE");

        if (code == 0 && result.get("PRODUCT") != null) {
            ProductDTO productDTO = (ProductDTO) result.get("PRODUCT");

            return new APIResponse<>(
                    code,
                    "Product retrieved successfully",
                    objectMapper.writeValueAsString(productDTO),
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
    public APIResponse<String> searchProducts(String searchTerm, Integer categoryId,
                                                          BigDecimal minPrice, BigDecimal maxPrice,
                                                          Integer page, Integer pageSize) throws JsonProcessingException {
        APIResponse<String> apiResponse = new APIResponse<>();
//         Call repository
        Map<String, Object> result = productRepository.searchProducts(
                searchTerm, categoryId, minPrice, maxPrice, page, pageSize);

        Integer code = (Integer) result.get("CODE");
        apiResponse.setCode(code);

        if (code == 0 && result.get("PRODUCTS") != null) {
            List<ProductDTO> productDTOs = (List<ProductDTO>) result.get("PRODUCTS");

            return new APIResponse<>(
                    code,
                    "Create successful products",
                    objectMapper.writeValueAsString(productDTOs),
                    ResponseType.SUCCESS
            );
        } else {
            APIResponse<String> apiResponse_v2 = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse_v2 != null ?
                    objectMapper.readValue(apiResponse_v2.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType()
            );
        }
    }

    @Override
    public APIResponse<String> getAvailableProducts(Integer page, Integer pageSize) throws JsonProcessingException {
        Map<String, Object> result = productRepository.getAvailableProducts(page, pageSize);

        int code = (Integer) result.get("CODE");

        if (code == 0 && result.get("PRODUCTS") != null) {
            List<ProductDTO> productDTOs = (List<ProductDTO>) result.get("PRODUCTS");

            return new APIResponse<>(
                    code,
                    "Create successful products",
                    objectMapper.writeValueAsString(productDTOs),
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

//    Pending
    @Override
    public APIResponse<String> updateProductStatus(Integer productId, String status) throws JsonProcessingException {
        Map<String, Object> result = productRepository.updateProductStatus(productId, status);

        int code = (Integer) result.get("CODE");

        if (code == 0) {
            return new APIResponse<>(
                    code,
                    "Create successful products",
                    objectMapper.writeValueAsString(""),
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

//    Pending
    @Override
    public APIResponse<String> updateProduct(Product product) throws JsonProcessingException {
        Map<String, Object> result = productRepository.updateProduct(
                product.getProductId(),
                product.getProductsName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getCategoryId()
        );

        int code = (Integer) result.get("CODE");

        if (code == 0) {
            return new APIResponse<>(
                    code,
                    "Create successful products",
                    objectMapper.writeValueAsString(""),
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
