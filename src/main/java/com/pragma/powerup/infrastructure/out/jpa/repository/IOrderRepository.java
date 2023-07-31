package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByCustomerIdAndStatus(Long customerId, String status);

    Page<OrderEntity> findByRestaurantIdAndStatus(Long restaurantId, String status, Pageable pageable);
}
