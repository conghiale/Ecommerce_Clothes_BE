package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:44 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.CategoryDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.Category;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ 2025. All rights reserved
 */

@Repository
@Slf4j
public class CategoryRepository {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public Map<String, Object> createCategory(String categoriesName, String description, Integer parentId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CATEGORY_CREATE")
                    .registerStoredProcedureParameter("p_CATEGORIES_NAME", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_DESCRIPTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PARENT_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .registerStoredProcedureParameter("p_CATEGORY_ID", Integer.class, ParameterMode.OUT)
                    .setParameter("p_CATEGORIES_NAME", categoriesName)
                    .setParameter("p_DESCRIPTION", description)
                    .setParameter("p_PARENT_ID", parentId);
    
            query.execute();
            
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            Integer categoryId = (Integer) query.getOutputParameterValue("p_CATEGORY_ID");
            
            result.put("CODE", code);
            result.put("CATEGORY_ID", categoryId);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1); // Generic error code
            result.put("ERROR", e.getMessage());
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }

    public Map<String, Object> updateCategory(Integer categoryId, String categoriesName, String description, Integer parentId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CATEGORY_UPDATE")
                    .registerStoredProcedureParameter("p_CATEGORY_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CATEGORIES_NAME", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_DESCRIPTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_PARENT_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_CATEGORY_ID", categoryId)
                    .setParameter("p_CATEGORIES_NAME", categoriesName)
                    .setParameter("p_DESCRIPTION", description)
                    .setParameter("p_PARENT_ID", parentId);
    
            query.execute();
            
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            result.put("CODE", code);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1); // Generic error code
            result.put("ERROR", e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }

    public Map<String, Object> deleteCategory(Integer categoryId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CATEGORY_DELETE")
                    .registerStoredProcedureParameter("p_CATEGORY_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_CATEGORY_ID", categoryId);
    
            query.execute();
            
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            result.put("CODE", code);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1); // Generic error code
            result.put("ERROR", e.getMessage());
            log.error("[CHECK CATEGORY REPOSITORY] Delete Category. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    @Transactional
    public Map<String, Object> getAllCategories() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
//             Create query to fetch all categories with their relationships
            String jpql = "SELECT DISTINCT c FROM Category c " +
                         "LEFT JOIN FETCH c.parent " +
                         "LEFT JOIN FETCH c.subCategories " +
                         "ORDER BY c.categoryId";

            List<Category> categories = entityManager.createQuery(jpql, Category.class)
                                                   .getResultList();
            
            // Get the status code from stored procedure
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CATEGORY_GET_ALL")
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT);
            
            query.execute();
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");

            List<?> results = query.getResultList();
            
            result.put("CODE", code);
            result.put("CATEGORIES", categories);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK CATEGORY REPOSITORY] Get All Categories. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }

    public Map<String, Object> getCategoryById(Integer categoryId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();

        try {
            transaction.begin();

            // Get the status code from stored procedure
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CATEGORY_GET_BY_ID")
                    .registerStoredProcedureParameter("p_CATEGORY_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_CATEGORY_ID", categoryId);

            List<?> results = query.getResultList();

            Integer code = (Integer) query.getOutputParameterValue("p_CODE");

            Category category = null;
            if (!results.isEmpty()) {
                Object[] row = (Object[]) results.get(0);
                category = new Category();
                category.setCategoryId((Integer) row[0]);
                category.setCategoriesName((String) row[1]);
                category.setDescription((String) row[2]);

                Integer parentId = (Integer) row[3];
                if (parentId != null) {
                    // Recursively fetch parent category
                    Category parent = getCategoryWithParents(parentId, entityManager);
                    category.setParent(parent);
                }

            }

            result.put("CODE", code);
            result.put("CATEGORY", category);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK CATEGORY REPOSITORY] Get Category By ID. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    private Category getCategoryWithParents(Integer categoryId, EntityManager entityManager) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("SP_CATEGORY_GET_BY_ID")
                .registerStoredProcedureParameter("p_CATEGORY_ID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                .setParameter("p_CATEGORY_ID", categoryId);

        List<?> results = query.getResultList();

        if (!results.isEmpty()) {
            Object[] row = (Object[]) results.get(0);
            Category category = new Category();
            category.setCategoryId((Integer) row[0]);
            category.setCategoriesName((String) row[1]);
            category.setDescription((String) row[2]);

            Integer parentId = (Integer) row[3];
            if (parentId != null) {
                // Recursively fetch parent category
                Category parent = getCategoryWithParents(parentId, entityManager);
                category.setParent(parent);
            }

            return category;
        }

        return null;
    }

    public Map<String, Object> getRootCategories() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CATEGORIES_ROOT_GET")
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT);
    
            List<?> results = query.getResultList();
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");

            List<CategoryDTO> rootCategories = getCategories(results);

            result.put("CODE", code);
            result.put("ROOT_CATEGORIES", rootCategories);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK CATEGORY REPOSITORY] Get Root Categories. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }

    private static List<CategoryDTO> getCategories(List<?> results) {
        List<CategoryDTO> rootCategories = new ArrayList<>();
        if (results != null && !results.isEmpty()) {
            for (Object object : results) {
                Object[] row = (Object[]) object;

                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setCategoryId((Integer) row[0]);
                categoryDTO.setCategoriesName((String) row[1]);
                categoryDTO.setDescription((String) row[2]);
                rootCategories.add(categoryDTO);
            }
        }
        return rootCategories;
    }

    public Map<String, Object> getSubCategories(Integer parentId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, Object> result = new HashMap<>();
        
        try {
            transaction.begin();
            
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("SP_CATEGORIES_CHILDREN_GET")
                    .registerStoredProcedureParameter("p_PARENT_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_CODE", Integer.class, ParameterMode.OUT)
                    .setParameter("p_PARENT_ID", parentId);

            List<?> results = query.getResultList();
            Integer code = (Integer) query.getOutputParameterValue("p_CODE");
            
            List<CategoryDTO> subCategories = getCategories(results);
            
            result.put("CODE", code);
            result.put("SUB_CATEGORIES", subCategories);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            result.put("CODE", 1);
            result.put("ERROR", e.getMessage());
            log.error("[CHECK CATEGORY REPOSITORY] Get Sub Categories. ERROR: ", e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return result;
    }
}
