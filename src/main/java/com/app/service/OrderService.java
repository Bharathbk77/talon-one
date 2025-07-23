package com.app.service;

import com.app.model.User;
import com.app.model.Order;
import com.app.model.OrderRequest;
import com.app.model.RewardsResponse;
import com.app.service.UserService;
import com.app.service.RewardsService;
import com.app.repository.OrderRepository;
lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service layer for managing orders.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserService userService;
    private final RewardsService rewardsService;
    private final OrderRepository orderRepository;

    /**
     * Places an order: evaluates rewards, applies discounts, saves the order, and updates user stats.
     * @param orderRequest The order request payload.
     * @param rewards The rewards/discounts to apply.
     * @return The saved Order.
     */
    public Order saveOrder(OrderRequest orderRequest, RewardsResponse rewards) {
        // Retrieve user
        User user = userService.getUserById(orderRequest.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("User not found for ID: " + orderRequest.getUserId());
        }

        // Calculate final price after applying rewards/discounts
        double discount = rewards != null ? rewards.getDiscountAmount() : 0.0;
        double total = orderRequest.getCart().getTotalAmount() - discount;

        // Create Order entity
        Order order = new Order();
        order.setUserId(user.getId());
        order.setItems(orderRequest.getCart().getItems());
        order.setTotalAmount(total);
        order.setDiscountApplied(discount);
        order.setStatus("PLACED");

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Update user statistics
        int newTotalOrders = user.getTotalOrders() + 1;
        double newTotalSpent = user.getTotalSpent() + total;
        userService.updateUserStats(user.getId(), newTotalOrders, newTotalSpent);

        // Confirm loyalty point usage if applicable
        rewardsService.confirmLoyalty(user.getId().toString(), total);

        return savedOrder;
    }
}
