package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderDish;

import java.util.List;

public interface IOrderPersistencePort {

    Order saveOrder(Order order);

    Boolean existsByCustomerIdAndStatus(Long id, String status);

    void saveOrderDish(List<OrderDish> orderDishModels);
}
