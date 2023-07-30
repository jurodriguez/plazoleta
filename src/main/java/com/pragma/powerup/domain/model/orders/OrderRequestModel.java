package com.pragma.powerup.domain.model.orders;

import java.util.List;

public class OrderRequestModel {

    private List<OrderDishRequestModel> orderDishes;
    private Long restaurantId;

    public OrderRequestModel() {
    }

    public OrderRequestModel(List<OrderDishRequestModel> orderDishes, Long restaurantId) {
        this.orderDishes = orderDishes;
        this.restaurantId = restaurantId;
    }

    public List<OrderDishRequestModel> getOrderDishes() {
        return orderDishes;
    }

    public void setOrderDishes(List<OrderDishRequestModel> orderDishes) {
        this.orderDishes = orderDishes;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
