package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 6:55 PM
 */

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Repository
public class ChatBoxRepository {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Map<String, Object> chatConversationGetOrCreate(Integer userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CHAT_CONVERSATION_GET_OR_CREATE")
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .registerStoredProcedureParameter("p_CONVERSATION_ID", Integer.class, ParameterMode.OUT)
                    .setParameter("p_USER_ID", userId);

            query.execute();

            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            if (code == 0 && query.getOutputParameterValue("p_CONVERSATION_ID") != null) {
                Integer conversationId = (Integer) query.getOutputParameterValue("p_CONVERSATION_ID");
                result.put("CONVERSATION_ID", conversationId);
            }

            result.put("CODE", code);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", -1);
            result.put("ERROR", e.getMessage());
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    public Map<String, Object> chatMessageProcess(Integer userId, Integer conversationId, String messageContent) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CHAT_MESSAGE_PROCESS")
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CONVERSATION_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_MESSAGE_CONTENT", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .registerStoredProcedureParameter("p_USER_MESSAGE_ID", Integer.class, ParameterMode.OUT)
                    .setParameter("p_USER_ID", userId)
                    .setParameter("p_CONVERSATION_ID", conversationId)
                    .setParameter("p_MESSAGE_CONTENT", messageContent);

            query.execute();

            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            if (code == 0 && query.getOutputParameterValue("p_USER_MESSAGE_ID") != null) {
                Integer userMessageId = (Integer) query.getOutputParameterValue("p_USER_MESSAGE_ID");
                result.put("USER_MESSAGE_ID", userMessageId);
            }

            result.put("CODE", code);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", -1);
            result.put("ERROR", e.getMessage());
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    public Map<String, Object> chatBotResponseSave(Integer conversationId, Integer userId,
                                                   String botResponse, Integer replyToMessageId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CHAT_BOT_RESPONSE_SAVE")
                    .registerStoredProcedureParameter("p_CONVERSATION_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_BOT_RESPONSE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_REPLY_TO_MESSAGE_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .registerStoredProcedureParameter("p_BOT_MESSAGE_ID", Integer.class, ParameterMode.OUT)
                    .setParameter("p_CONVERSATION_ID", conversationId)
                    .setParameter("p_USER_ID", userId)
                    .setParameter("p_BOT_RESPONSE", botResponse)
                    .setParameter("p_REPLY_TO_MESSAGE_ID", replyToMessageId);

            query.execute();

            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            if (code == 0 && query.getOutputParameterValue("p_BOT_MESSAGE_ID") != null) {
                Integer botMessageId = (Integer) query.getOutputParameterValue("p_BOT_MESSAGE_ID");
                result.put("BOT_MESSAGE_ID", botMessageId);
            }

            result.put("CODE", code);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", -1);
            result.put("ERROR", e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }
}
