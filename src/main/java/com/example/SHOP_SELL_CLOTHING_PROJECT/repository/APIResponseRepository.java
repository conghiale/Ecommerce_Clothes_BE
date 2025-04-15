package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/20
 * Time: 5:58 PM
 */

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Repository
public class APIResponseRepository {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public Map<String, Object> createAPIResponse(Integer code, String message, String description, String responseType) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_API_RESPONSE_CREATE")
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_MESSAGE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_DESCRIPTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_RESPONSE_TYPE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_RESPONSE_ID", Integer.class, ParameterMode.OUT)
                    .registerStoredProcedureParameter("p_STATUS_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_CODE", code)
                    .setParameter("p_MESSAGE", message)
                    .setParameter("p_DESCRIPTION", description)
                    .setParameter("p_RESPONSE_TYPE", responseType);

            query.execute();

            Integer statusCode = (Integer) query.getOutputParameterValue("p_STATUS_CODE");
            Integer responseId = (Integer) query.getOutputParameterValue("p_RESPONSE_ID");

            result.put("STATUS_CODE", statusCode);
            result.put("RESPONSE_ID", responseId);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("STATUS_CODE", -1);
            result.put("ERROR", e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    public Map<String, Object> updateAPIResponse(Integer responseId, Integer code, String message,
                                                 String description, String responseType) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_API_RESPONSE_UPDATE")
                    .registerStoredProcedureParameter("p_RESPONSE_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_MESSAGE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_DESCRIPTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_RESPONSE_TYPE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_STATUS_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_RESPONSE_ID", responseId)
                    .setParameter("p_CODE", code)
                    .setParameter("p_MESSAGE", message)
                    .setParameter("p_DESCRIPTION", description)
                    .setParameter("p_RESPONSE_TYPE", responseType);

            query.execute();

            Integer statusCode = (Integer) query.getOutputParameterValue("p_STATUS_CODE");
            result.put("STATUS_CODE", statusCode);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("STATUS_CODE", -1);
            result.put("ERROR", e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    public Map<String, Object> deleteAPIResponse(Integer responseId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_API_RESPONSE_DELETE")
                    .registerStoredProcedureParameter("p_RESPONSE_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_STATUS_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_RESPONSE_ID", responseId);

            query.execute();

            Integer statusCode = (Integer) query.getOutputParameterValue("p_STATUS_CODE");
            result.put("STATUS_CODE", statusCode);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("STATUS_CODE", -1);
            result.put("ERROR", e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    public Map<String, Object> getAPIResponseByCode(Integer code) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_API_RESPONSE_GET_BY_CODE")
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_STATUS_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_CODE", code);

            query.execute();

            Integer statusCode = (Integer) query.getOutputParameterValue("p_STATUS_CODE");
            result.put("STATUS_CODE", statusCode);

            if (statusCode == 0) {
                List<?> responses = query.getResultList();
                if (!responses.isEmpty()) {
                    result.put("RESPONSE", responses.get(0));
                }
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("STATUS_CODE", -1);
            result.put("ERROR", e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }
}
