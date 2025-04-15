package com.example.SHOP_SELL_CLOTHING_PROJECT.model;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:21 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ 2025. All rights reserved
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SUPPORT_TICKETS")
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID")
    private Integer ticketId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "SUBJECT", nullable = false)
    private String subject;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    // Getters and Setters
}
