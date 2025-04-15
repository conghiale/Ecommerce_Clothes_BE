package com.example.SHOP_SELL_CLOTHING_PROJECT.model;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:43 PM
 */

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ 2025. All rights reserved
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TICKET_RESPONSES")
public class TicketResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESPONSE_ID")
    private Integer responseId;

    @ManyToOne
    @JoinColumn(name = "TICKET_ID", nullable = false)
    private SupportTicket ticket;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    // Getters and Setters
}
