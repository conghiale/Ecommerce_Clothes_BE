package com.example.SHOP_SELL_CLOTHING_PROJECT.model;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 10:47 PM
 */

/**
 * @ 2025. All rights reserved
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.AuthProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "AUTH_PROVIDER")
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(name = "IS_ADMIN")
    private Boolean isAdmin;

    // Getters and Setters

}
