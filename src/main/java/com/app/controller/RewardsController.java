package com.app.controller;

import com.app.model.CartRequest;
import com.app.model.RewardsResponse;
import com.app.service.RewardsService;
import jakarta.validation.Valid;
lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RewardsController handles reward evaluation via Talon.One.
 */
@RestController
@RequestMapping("/rewards")
@RequiredArgsConstructor
public class RewardsController {

    private final RewardsService rewardsService;

    /**
     * Evaluate rewards based on cart details.
     * @param cartRequest CartRequest payload
     * @return RewardsResponse with applicable discounts and rewards
     */
    @PostMapping("/evaluate")
    public ResponseEntity<RewardsResponse> evaluateRewards(
            @Valid @RequestBody CartRequest cartRequest) {
        RewardsResponse response = rewardsService.evaluateRewards(cartRequest);
        return ResponseEntity.ok(response);
    }
}
