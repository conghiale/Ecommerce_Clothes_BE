package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:04 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.AuthProvider;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.User;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Repository
public class UserRepository {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public Boolean authenticateUser(String userName, String passwordHash) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_USER_AUTHENTICATE")
                    .registerStoredProcedureParameter("p_USER_NAME", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PASSWORD_HASH", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_IS_VALID", Boolean.class, ParameterMode.OUT)
                    .setParameter("p_USER_NAME", userName)
                    .setParameter("p_PASSWORD_HASH", passwordHash);

            query.execute();
            return (Boolean) query.getOutputParameterValue("p_IS_VALID");
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }

    }

    public Map<String, Object> createUser(String userName, String passwordHash, String firstName,
                                          String lastName, String phoneNumber, String authProvider) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_USER_CREATE")
                    .registerStoredProcedureParameter("p_USER_NAME", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PASSWORD_HASH", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_FIRST_NAME", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_LAST_NAME", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PHONE_NUMBER", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_AUTH_PROVIDER", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .registerStoredProcedureParameter("p_USER_ID", Integer.class, ParameterMode.OUT)
                    .setParameter("p_USER_NAME", userName)
                    .setParameter("p_PASSWORD_HASH", passwordHash)
                    .setParameter("p_FIRST_NAME", firstName)
                    .setParameter("p_LAST_NAME", lastName)
                    .setParameter("p_PHONE_NUMBER", phoneNumber)
                    .setParameter("p_AUTH_PROVIDER", authProvider);

            query.execute();

            Map<String, Object> result = new HashMap<>();
            result.put("CODE", query.getOutputParameterValue("p_CODE"));
            result.put("USER_ID", query.getOutputParameterValue("p_USER_ID"));
            return result;
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    public User findByUsername(String userName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_USER_GET_BY_USERNAME")
                    .registerStoredProcedureParameter("p_USERNAME", String.class, ParameterMode.IN)
                    .setParameter("p_USERNAME", userName);

            List<Object[]> results = query.getResultList(); // get List Object[]

            if (!results.isEmpty()) {
                Object[] row = results.get(0);

                User user = new User();
                user.setUserId((Integer) row[0]);
                user.setUserName((String) row[1]);
                user.setPasswordHash((String) row[2]);
                user.setFirstName((String) row[3]);
                user.setLastName((String) row[4]);
                user.setPhoneNumber((String) row[5]);
                user.setIsAdmin((Boolean) row[6]);
                user.setAuthProvider(AuthProvider.valueOf((String) row[7]));

                return user;
            }
            return null;

        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }
}
