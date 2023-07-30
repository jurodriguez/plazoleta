package com.pragma.powerup.domain.model.orders;

public class OrderDishRequestModel {

    private Long dishId;
    private Long number;

    public OrderDishRequestModel() {
    }

    public OrderDishRequestModel(Long dishId, Long number) {
        this.dishId = dishId;
        this.number = number;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
