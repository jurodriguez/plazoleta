package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.orders.OrderRequestModel;

public interface IOrderServicePort {
    void saveOrder(OrderRequestModel orderModel);
}
