package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderDish;

import java.util.List;

public interface IOrderPersistencePort {

    Order saveOrder(Order order);

    Boolean existsByCustomerIdAndStatus(Long id, String status);

    void saveOrderDish(List<OrderDish> orderDishModels);

    List<Order> getAllOrdersWithPagination(Integer page, Integer size, Long restaurantId, String status);

    List<OrderDish> getAllOrdersByOrderId(Long orderId);

    Boolean existsByIdAndStatus(Long id, String status);

    Order getOrderById(Long id);
}
