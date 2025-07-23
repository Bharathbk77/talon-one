package com.app.service;

import com.app.model.User;
import com.app.repository.UserRepository;
lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for managing users.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Fetches a user by their ID.
     * @param id The ID of the user.
     * @return The User object if found, otherwise null.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Updates the user's totalOrders and totalSpent statistics.
     * @param id The user ID.
     * @param totalOrders The new total orders count.
     * @param totalSpent The new total spent amount.
     * @return true if the user was updated, false if not found.
     */
    public boolean updateUserStats(Long id, int totalOrders, double totalSpent) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();
        user.setTotalOrders(totalOrders);
        user.setTotalSpent(totalSpent);
        userRepository.save(user);
        return true;
    }

    /**
     * Saves or updates a user entity.
     * @param user The User object to save.
     * @return The saved User.
     */
    public User save(User user) {
        return userRepository.save(user);
    }
}
