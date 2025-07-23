package com.app.model;

import lombok.*;

/**
 * DTO for placing an order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private Long userId;
    private CartRequest cart;
}
