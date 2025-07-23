package com.app.model;

import jakarta.persistence.*;
lombok.*;
import java.util.List;

/**
 * User entity representing a customer in the e-commerce system.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String name;
    private int totalOrders;
    private double totalSpent;
    private int loyaltyPoints;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
}
