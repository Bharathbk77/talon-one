package com.app.model;

import lombok.*;
import java.util.List;
import java.util.Map;

/**
 * DTO for evaluating a session in Talon.One (cart + profile).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDTO {
    private String sessionId;
    private String userId;
    private List<CartItemDTO> items;
    private double totalAmount;
    private Map<String, Object> sessionAttributes; // e.g., device, channel, etc.
}
