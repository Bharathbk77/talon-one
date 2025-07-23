package com.app.service;

import com.app.model.CartRequest;
import com.app.model.RewardsResponse;
import com.app.talonone.TalonOneClient;
lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service layer for integrating with Talon.One for rewards and discounts.
 */
@Service
@RequiredArgsConstructor
public class RewardsService {

    private final TalonOneClient talonOneClient;

    /**
     * Evaluates the cart for applicable rewards and discounts via Talon.One.
     * @param cartRequest The cart details.
     * @return RewardsResponse containing discounts and rewards.
     */
    public RewardsResponse evaluateRewards(CartRequest cartRequest) {
        // Update user profile in Talon.One
        talonOneClient.updateProfile(cartRequest.getUserId(), cartRequest);

        // Evaluate session for discounts/rewards
        RewardsResponse response = talonOneClient.evaluateSession(cartRequest.getUserId(), cartRequest);

        return response;
    }

    /**
     * Confirms loyalty point usage with Talon.One after order placement.
     * @param userId The user ID.
     * @param total The total amount for which loyalty points may be used.
     */
    public void confirmLoyalty(String userId, double total) {
        talonOneClient.confirmLoyalty(userId, total);
    }
}
