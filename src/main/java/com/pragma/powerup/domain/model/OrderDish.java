package com.pragma.powerup.domain.model;

public class OrderDish {

    private Long id;
    private Long orderId;
    private Long dishId;
    private Integer number;

    public OrderDish() {
    }

    public OrderDish(Long id, Long orderId, Long dishId, Integer number) {
        this.id = id;
        this.orderId = orderId;
        this.dishId = dishId;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
