package com.example.SHOP_SELL_CLOTHING_PROJECT.repository;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:45 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

/**
 * @ 2025. All rights reserved
 */

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Procedure(procedureName = "SP_CART_ADD_ITEM")
    void addCartItem(Integer userId, Integer productId, Integer variantId, Integer quantity);

    @Procedure(procedureName = "SP_CART_REMOVE_ITEM")
    void removeCartItem(Integer cartItemId);
}
