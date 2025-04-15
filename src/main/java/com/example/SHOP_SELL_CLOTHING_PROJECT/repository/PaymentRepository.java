package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/22
 * Time: 11:16 PM
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
public class PaymentRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Map<String, Object> paymentProcess(Integer orderId, String paymentMethod, String transactionCode) {
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
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_ORDER_ID", orderId)
                    .setParameter("p_PAYMENT_METHOD", paymentMethod)
                    .setParameter("p_TRANSACTION_CODE", transactionCode);

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
            log.error("[PAYMENT REPOSITORY] Process Payment. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }
}
