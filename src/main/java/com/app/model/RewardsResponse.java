package com.app.model;

import lombok.*;
import java.util.List;

/**
 * DTO representing the response from Talon.One with rewards and discounts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardsResponse {
    private double discountAmount;
    private List<RewardDetailDTO> rewards;
    private List<String> appliedCoupons;
    private int loyaltyPointsUsed;
    private int loyaltyPointsEarned;
}
