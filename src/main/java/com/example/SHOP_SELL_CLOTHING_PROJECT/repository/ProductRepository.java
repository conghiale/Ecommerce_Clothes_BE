package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:05 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ProductStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ImageDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ProductDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ProductVariantDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Repository
@Slf4j
public class ProductRepository {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public Map<String, Object> createProduct(String productsName, String description,
                                             BigDecimal price, Integer categoryId, String sizes) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_PRODUCT_CREATE")
                    .registerStoredProcedureParameter("p_PRODUCTS_NAME", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_DESCRIPTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PRICE", BigDecimal.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CATEGORY_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_SIZES", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .registerStoredProcedureParameter("p_PRODUCT_ID", Integer.class, ParameterMode.OUT)
                    .setParameter("p_PRODUCTS_NAME", productsName)
                    .setParameter("p_DESCRIPTION", description)
                    .setParameter("p_PRICE", price)
                    .setParameter("p_CATEGORY_ID", categoryId)
                    .setParameter("p_SIZES", sizes);

            query.execute();
            
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");

            if (code == 0) {
                Integer productId = (Integer) query.getOutputParameterValue("p_PRODUCT_ID");
                result.put("PRODUCT_ID", productId);
            } else
                result.put("PRODUCT_ID", null);
            
            result.put("CODE", code);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK PRODUCT REPOSITORY] Create Product. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }

    public Map<String, Object> getProductById(Integer productId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_PRODUCT_GET")
                    .registerStoredProcedureParameter("p_PRODUCT_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_PRODUCT_ID", productId);

            List<?> results = query.getResultList();
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            
            ProductDTO productDTO = null;
            if (code == 0 && results != null && !results.isEmpty()) {
                Object[] row = (Object[]) results.get(0);
                productDTO = mapProductFromRow(row);
            }
            
            result.put("CODE", code);
            result.put("PRODUCT", productDTO);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK PRODUCT REPOSITORY] Get Product By ID. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }

    public Map<String, Object> searchProducts(String searchTerm, Integer categoryId,
                                            BigDecimal minPrice, BigDecimal maxPrice,
                                            Integer page, Integer pageSize) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_PRODUCT_SEARCH")
                    .registerStoredProcedureParameter("p_SEARCH_TERM", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CATEGORY_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_MIN_PRICE", BigDecimal.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_MAX_PRICE", BigDecimal.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PAGE", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PAGE_SIZE", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_SEARCH_TERM", searchTerm)
                    .setParameter("p_CATEGORY_ID", categoryId)
                    .setParameter("p_MIN_PRICE", minPrice)
                    .setParameter("p_MAX_PRICE", maxPrice)
                    .setParameter("p_PAGE", page)
                    .setParameter("p_PAGE_SIZE", pageSize);

            List<?> results = query.getResultList();
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            
            List<ProductDTO> productDTOS = null;

            if (code == 0 && results != null && !results.isEmpty()) {
                productDTOS = new ArrayList<>();
                for (Object item : results) {
                    Object[] row = (Object[]) item;
                    productDTOS.add(mapProductFromRow(row));
                }
            }
            
            result.put("CODE", code);
            result.put("PRODUCTS", productDTOS);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK PRODUCT REPOSITORY] Search Products. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }

    public Map<String, Object> getAvailableProducts(Integer page, Integer pageSize) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_PRODUCTS_AVAILABLE_GET")
                    .registerStoredProcedureParameter("p_PAGE", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PAGE_SIZE", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_PAGE", page)
                    .setParameter("p_PAGE_SIZE", pageSize);

            List<?> results = query.getResultList();
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");

            List<ProductDTO> productDTOs = null;

            if (code == 0 && results != null && !results.isEmpty()) {
                productDTOs = new ArrayList<>();
                for (Object item : results) {
                    Object[] row = (Object[]) item;
                    productDTOs.add(mapProductFromRow(row));
                }
            }

            result.put("CODE", code);
            result.put("PRODUCTS", productDTOs);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK PRODUCT REPOSITORY] Get Available Products. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

//    Pending
    public Map<String, Object> updateProductStatus(Integer productId, String status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_PRODUCT_STATUS_UPDATE")
                    .registerStoredProcedureParameter("p_PRODUCT_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_STATUS", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_PRODUCT_ID", productId)
                    .setParameter("p_STATUS", status);

            query.execute();
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            
            result.put("CODE", code);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK PRODUCT REPOSITORY] Update Product Status. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }

//    Pending
    public Map<String, Object> updateProduct(Integer productId, String productsName,
                                           String description, BigDecimal price, Integer categoryId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_PRODUCT_UPDATE")
                    .registerStoredProcedureParameter("p_PRODUCT_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PRODUCTS_NAME", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_DESCRIPTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PRICE", BigDecimal.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CATEGORY_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_PRODUCT_ID", productId)
                    .setParameter("p_PRODUCTS_NAME", productsName)
                    .setParameter("p_DESCRIPTION", description)
                    .setParameter("p_PRICE", price)
                    .setParameter("p_CATEGORY_ID", categoryId);

            query.execute();
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            
            result.put("CODE", code);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK PRODUCT REPOSITORY] Update Product. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }

    private ProductDTO mapProductFromRow(Object[] row) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ProductDTO productDTO = new ProductDTO();

        productDTO.setProductId((Integer) row[0]);
        productDTO.setProductsName((String) row[1]);
        productDTO.setDescription((String) row[2]);
        productDTO.setPrice((BigDecimal) row[3]);
        productDTO.setCategoryID((Integer) row[4]);
        productDTO.setStockQuantity((Integer) row[5]);
        productDTO.setProductStatus(ProductStatus.AVAILABLE);
        productDTO.setCategoryName((String) row[7]);

//             Parse variants JSON string
        String variantsJson = (String) row[8];
        if (variantsJson != null) {
            List<ProductVariantDTO> variants = mapper.readValue(variantsJson, new TypeReference<>() {});
            productDTO.setVariants(variants);
        }

        // Parse images JSON string
        String imagesJson = (String) row[9];
        if (imagesJson != null) {
            List<ImageDTO> images = mapper.readValue(imagesJson, new TypeReference<>() {});
            productDTO.setImages(images);
        }

        return productDTO;
    }
}
