package com.app.model;

import lombok.*;
import java.util.List;

/**
 * DTO representing the cart details for reward evaluation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {
    private Long userId;
    private List<CartItemDTO> items;
    private double totalAmount;
}
