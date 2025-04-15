package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:22 PM
 */

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

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
