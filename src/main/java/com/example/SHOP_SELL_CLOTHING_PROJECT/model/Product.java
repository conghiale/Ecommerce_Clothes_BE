package com.example.SHOP_SELL_CLOTHING_PROJECT.model;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 10:48 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @ 2025. All rights reserved
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "PRODUCTS_NAME", nullable = false)
    private String productsName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "STOCK_QUANTITY")
    private Integer stockQuantity;

    @Column(name = "PRODUCT_STATUS")
    private ProductStatus productStatus;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    // Getters and Setters
}
