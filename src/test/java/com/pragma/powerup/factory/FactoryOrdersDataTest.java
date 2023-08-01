package com.pragma.powerup.factory;

import com.pragma.powerup.domain.enums.EOrderStatuses;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderDish;

import java.time.LocalDate;

public class FactoryOrdersDataTest {
    public static Order getOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerId(1L);
        order.setDate(LocalDate.now());
        order.setRestaurantId(20L);

        return order;
    }

    public static Order getOrderWithStatus() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerId(1L);
        order.setDate(LocalDate.now());
        order.setRestaurantId(20L);
        order.setStatus(EOrderStatuses.IN_PREPARATION.getName());

        return order;
    }

    public static OrderDish getOrderDish() {
        OrderDish orderDish = new OrderDish();
        orderDish.setId(1L);
        orderDish.setNumber(2);
        orderDish.setDishId(1L);

        return orderDish;
    }

}
