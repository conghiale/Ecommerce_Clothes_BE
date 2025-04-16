package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:22 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.OrderStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.PaymentMethod;
import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.PaymentStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ProductStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ImageDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.OrderDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ProductDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ProductVariantDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * @ 2025. All rights reserved
 */

@Repository
@Slf4j
public class OrderRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Map<String, Object> createOrder(Integer userId, String paymentMethod,
                                           String shippingAddress, String billingAddress) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_ORDER_CREATE")
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PAYMENT_METHOD", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_SHIPPING_ADDRESS", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_BILLING_ADDRESS", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .registerStoredProcedureParameter("p_ORDER_ID", Integer.class, ParameterMode.OUT)
                    .setParameter("p_USER_ID", userId)
                    .setParameter("p_PAYMENT_METHOD", paymentMethod)
                    .setParameter("p_SHIPPING_ADDRESS", shippingAddress)
                    .setParameter("p_BILLING_ADDRESS", billingAddress);

            query.execute();

            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            int orderId = -1;
            if (code == 0) {
                orderId = query.getOutputParameterValue("p_ORDER_ID") != null ?
                        (Integer) query.getOutputParameterValue("p_ORDER_ID") : -1;
            }

            result.put("CODE", code);
            result.put("ORDER_ID", orderId);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[ORDER REPOSITORY] Create Order. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    public Map<String, Object> getOrders(Integer userId, Integer page, Integer pageSize) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

//        System.out.println("[CHECK GET ORDERS] userId: " + userId + " | page: " + page + " | pageSize: " + pageSize);

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_ORDER_GET_ALL")
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PAGE", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PAGE_SIZE", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_USER_ID", userId)
                    .setParameter("p_PAGE", page)
                    .setParameter("p_PAGE_SIZE", pageSize);

            List<?> results = query.getResultList();
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");

            List<OrderDTO> orderDTOS = null;

            if (code == 0 && results != null && !results.isEmpty()) {
                orderDTOS = new ArrayList<>();
                for (Object item : results) {
                    Object[] row = (Object[]) item;
                    orderDTOS.add(mapOrderFromRow(row));
                }
            }

            result.put("CODE", code);
            result.put("ORDERS", orderDTOS);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[ORDER REPOSITORY] Get Orders. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    public Map<String, Object> getOrderDetail(Integer userId, Integer orderId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_ORDER_GET_DETAIL")
                    .registerStoredProcedureParameter("p_ORDER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_ORDER_ID", orderId)
                    .setParameter("p_USER_ID", userId);

            boolean hasResultSet = query.execute(); // Dùng execute() thay vì getResultList()

            Integer code = (Integer) query.getOutputParameterValue("p_CODE");

            OrderDTO orderDTO = null;
            List<ProductDTO> products;

            if (code == 0 && hasResultSet) {
                @SuppressWarnings("unchecked")
                List<Object[]> results = query.getResultList();
                // Map the first row to get order details
                Object[] firstRow = results.get(0);
                orderDTO = mapOrderDetailFromRow(firstRow);

                // Map all rows to get products
                Map<Integer, ProductDTO> productMap = new HashMap<>();

                for (Object[] row : results) {
                    ProductDTO productDTO = mapProductFromRow(row);

                    // Use product ID as key to avoid duplicates
                    if (!productMap.containsKey(productDTO.getProductId())) {
                        productMap.put(productDTO.getProductId(), productDTO);
                    }

                    // Add variant information
                    ProductVariantDTO variant = mapVariantFromRow(row);
                    productMap.get(productDTO.getProductId()).getVariants().add(variant);
                }

                // Convert map to list
                products = new ArrayList<>(productMap.values());
                orderDTO.setProducts(products);
            }

            result.put("CODE", code);
            result.put("ORDER", orderDTO);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[ORDER REPOSITORY] Get Order Detail. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    private OrderDTO mapOrderFromRow(Object[] row) throws JsonProcessingException {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderId((Integer) row[0]);
        orderDTO.setUserId((Integer) row[1]);
        orderDTO.setOrderStatus(OrderStatus.valueOf((String) row[2]));
        orderDTO.setPaymentMethod(PaymentMethod.valueOf((String) row[3]));
        orderDTO.setPaymentStatus(PaymentStatus.valueOf((String) row[4]));
        orderDTO.setTotalAmount((BigDecimal) row[5]);
        orderDTO.setShippingAddress((String) row[6]);
        orderDTO.setBillingAddress((String) row[7]);
        orderDTO.setCreateAt(((Timestamp) row[8]).toLocalDateTime());
        orderDTO.setUpdateAt(((Timestamp) row[9]).toLocalDateTime());
        orderDTO.setTotalItems((Long) row[10]);

        return orderDTO;
    }

    private OrderDTO mapOrderDetailFromRow(Object[] row) throws JsonProcessingException {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderId((Integer) row[0]);
        orderDTO.setUserId((Integer) row[1]);
        orderDTO.setPaymentMethod(PaymentMethod.valueOf((String) row[2]));
        orderDTO.setTotalAmount((BigDecimal) row[3]);
        orderDTO.setShippingAddress((String) row[4]);
        orderDTO.setBillingAddress((String) row[5]);
        orderDTO.setPaymentStatus(PaymentStatus.valueOf((String) row[6]));
        orderDTO.setOrderStatus(OrderStatus.valueOf((String) row[7]));
        orderDTO.setCreateAt(((Timestamp) row[8]).toLocalDateTime());
        orderDTO.setUpdateAt(((Timestamp) row[9]).toLocalDateTime());
        orderDTO.setTransactionCode((row[10] != null && !row[10].equals("")) ? (String) row[10] : null);
        orderDTO.setPaymentDate((row[11] != null && !row[11].equals("")) ? ((Timestamp) row[11]).toLocalDateTime() : null);

        return orderDTO;
    }

    private ProductDTO mapProductFromRow(Object[] row) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setOrderItemId((Integer) row[12]);
        productDTO.setStockQuantity((Integer) row[13]);
        productDTO.setPrice((BigDecimal) row[14]);
        productDTO.setProductId((Integer) row[15]);
        productDTO.setProductsName((String) row[16]);
        productDTO.setDescription((String) row[17]);
        productDTO.setCategoryID((Integer) row[18]);
        productDTO.setCategoryName((String) row[19]);
        productDTO.setVariants(new ArrayList<>());

        // Create and set image if exists
        if (row[23] != null && row[24] != null) {
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setImageId((Integer) row[23]);
            imageDTO.setImageUrl((String) row[24]);
            imageDTO.setIsPrimary(true);

            productDTO.setImages(Collections.singletonList(imageDTO));
        }

        return productDTO;
    }

    private ProductVariantDTO mapVariantFromRow(Object[] row) {
        ProductVariantDTO variantDTO = new ProductVariantDTO();

        variantDTO.setVariantId((Integer) row[20]);
        variantDTO.setSize((String) row[21]);
        variantDTO.setStockQuantity((Integer) row[22]);
//        variantDTO.setPriceAtTime((BigDecimal) row[14]);

        return variantDTO;
    }

    public Map<String, Object> processPayment(Integer orderId, String paymentMethod, String transactionCode) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_PAYMENT_PROCESS")
                    .registerStoredProcedureParameter("p_ORDER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PAYMENT_METHOD", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_TRANSACTION_CODE", String.class, ParameterMode.IN)
                    .setParameter("p_ORDER_ID", orderId)
                    .setParameter("p_PAYMENT_METHOD", paymentMethod)
                    .setParameter("p_TRANSACTION_CODE", transactionCode);

            query.execute();
            result.put("SUCCESS", true);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("SUCCESS", false);
            result.put("ERROR", e.getMessage());
            log.error("[ORDER REPOSITORY] Process Payment. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }
}
