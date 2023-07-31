package com.pragma.powerup.domain.model.orders;

import java.time.LocalDate;
import java.util.List;

public class OrderResponseModel {

    private Long id;
    private Long customerId;
    private Long chefId;
    private LocalDate date;

    private List<OrderDishResponseModel> orderDishes;

    public OrderResponseModel() {
    }

    public OrderResponseModel(Long id, Long customerId, Long chefId, LocalDate date, List<OrderDishResponseModel> orderDishes) {
        this.id = id;
        this.customerId = customerId;
        this.chefId = chefId;
        this.date = date;
        this.orderDishes = orderDishes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getChefId() {
        return chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<OrderDishResponseModel> getOrderDishes() {
        return orderDishes;
    }

    public void setOrderDishes(List<OrderDishResponseModel> orderDishes) {
        this.orderDishes = orderDishes;
    }
}

