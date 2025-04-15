package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:33 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.OrderStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.TicketStatus;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.TicketResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.TicketSupportDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.service.APIResponseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Slf4j
@Repository
public class SupportTicketRepository {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Map<String, Object> createTicket(Integer userId, Integer orderId, String subject, String message) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_TICKET_SUPPORT_CREATE")
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_ORDER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_SUBJECT", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_MESSAGE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_TICKET_ID", Integer.class, ParameterMode.OUT)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_USER_ID", userId)
                    .setParameter("p_ORDER_ID", orderId)
                    .setParameter("p_SUBJECT", subject)
                    .setParameter("p_MESSAGE", message);

            query.execute();

            Integer code = (Integer) query.getOutputParameterValue("p_CODE");

            if (code == 0 && query.getOutputParameterValue("p_TICKET_ID") != null) {
                Integer ticketId = (Integer) query.getOutputParameterValue("p_TICKET_ID");
                result.put("TICKET_ID", ticketId);
            }

            result.put("CODE", code);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", -1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK CREATE TICKET] ERROR", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

//    Pending handle p_CODE
    public Map<String, Object> createTicketResponse(Integer ticketId, Integer userId, String message) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_TICKET_RESPONSE_CREATE")
                    .registerStoredProcedureParameter("p_TICKET_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_MESSAGE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_RESPONSE_ID", Integer.class, ParameterMode.OUT)
                    .setParameter("p_TICKET_ID", ticketId)
                    .setParameter("p_USER_ID", userId)
                    .setParameter("p_MESSAGE", message);

            query.execute();

            result.put("CODE", 0);
            Integer responseId = (Integer) query.getOutputParameterValue("p_RESPONSE_ID");
            result.put("RESPONSE_ID", responseId);

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

    //    Pending handle p_CODE
    public Map<String, Object> getTicketSupports(Integer userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> results = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_TICKET_SUPPORT_GET")
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.IN)
                    .setParameter("p_USER_ID", userId);

            List<?> result = query.getResultList();

            if (result != null && !result.isEmpty()) {
                List<TicketSupportDTO> ticketSupportDTOS = new ArrayList<>();
                for (Object o : result) {
                    Object[] object = (Object[]) o;

                    TicketSupportDTO  ticketSupportDTO = new TicketSupportDTO();
                    ticketSupportDTO.setTicketId((Integer) object[0]);
                    ticketSupportDTO.setUserId((Integer) object[1]);
                    ticketSupportDTO.setOrderId((Integer) object[2]);
                    ticketSupportDTO.setSubject((String) object[3]);
                    ticketSupportDTO.setMessage((String) object[4]);
                    ticketSupportDTO.setStatus(TicketStatus.valueOf((String) object[5]) );
                    ticketSupportDTO.setCreatedAt(object[7] != null ? ((Timestamp) object[7]).toLocalDateTime() : null);
                    ticketSupportDTO.setUpdateAt(object[8] != null ? ((Timestamp) object[8]).toLocalDateTime() : null);
                    ticketSupportDTO.setOrderStatus(OrderStatus.valueOf((String) object[9]));
                    ticketSupportDTOS.add(ticketSupportDTO);
                }

                results.put("CODE", 0);
                results.put("TICKET_SUPPORT", ticketSupportDTOS);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            results.put("CODE", -1);
            results.put("ERROR", e.getMessage());
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return results;
    }

//        Pending handle p_CODE
    public Map<String, Object> getTicketResponses(Integer ticketId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> results = new HashMap<>();

        try {
            transaction.begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_TICKET_RESPONSES_GET")
                    .registerStoredProcedureParameter("p_TICKET_ID", Integer.class, ParameterMode.IN)
                    .setParameter("p_TICKET_ID", ticketId);

            List<?> result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                List<TicketResponseDTO> ticketResponseDTOS = new ArrayList<>();

                for (Object row : result) {
                    Object[] object = (Object[]) row;
                    TicketResponseDTO  ticketResponseDTO = new TicketResponseDTO();
                    ticketResponseDTO.setTicketId((Integer) object[0]);
                    ticketResponseDTO.setTicketId((Integer) object[1]);
                    ticketResponseDTO.setUserId((Integer) object[2]);
                    ticketResponseDTO.setMessage((String) object[3]);
                    ticketResponseDTO.setCreatedAt(((Timestamp) object[4]).toLocalDateTime());
                    ticketResponseDTO.setUpdatedAt(((Timestamp) object[5]).toLocalDateTime());
                    ticketResponseDTO.setUserName((String) object[6]);
                    ticketResponseDTO.setIsAdmin((Boolean) object[7]);

                    ticketResponseDTOS.add(ticketResponseDTO);
                }
                results.put("CODE", 0);
                results.put("TICKET_RESPONSE", ticketResponseDTOS);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            results.put("CODE", -1);
            results.put("ERROR", e.getMessage());
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return results;
    }
}
