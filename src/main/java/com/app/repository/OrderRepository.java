package com.app.repository;

import com.app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the Order entity.
 * <p>
 * Provides CRUD operations and query methods for Order data.
 * This interface extends JpaRepository, leveraging Spring Data JPA's
 * default implementation for common database operations.
 * </p>
 * <p>
 * No custom query methods are defined; default methods are sufficient.
 * </p>
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Default query methods provided by JpaRepository are sufficient.
}
