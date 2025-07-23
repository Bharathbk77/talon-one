package com.app.model;

import jakarta.persistence.*;
lombok.*;
import java.util.List;

/**
 * Order entity representing a purchase made by a user.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Foreign key to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;
    private double totalAmount;
    private double discountApplied;
    private String status; // e.g., PLACED, CANCELLED, etc.
    private long createdAt; // Unix timestamp for order creation
}
