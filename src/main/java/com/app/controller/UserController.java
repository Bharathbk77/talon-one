package com.app.controller;

import com.app.model.User;
import com.app.model.OrderRequest;
import com.app.model.CartRequest;
import com.app.model.RewardsResponse;
import com.app.service.UserService;
import com.app.service.OrderService;
import com.app.service.RewardsService;
import jakarta.validation.Valid;
lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController handles user-related endpoints.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Fetch user details by ID.
     * @param id User ID
     * @return User details or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Update user's totalOrders and totalSpent.
     * @param id User ID
     * @param request User object with updated stats (only totalOrders and totalSpent are used)
     * @return 204 No Content on success, 404 if user not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserStats(
            @PathVariable Long id,
            @Valid @RequestBody User request) {
        boolean updated = userService.updateUserStats(id, request.getTotalOrders(), request.getTotalSpent());
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
