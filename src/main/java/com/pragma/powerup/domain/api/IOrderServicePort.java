package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.orders.OrderRequestModel;
import com.pragma.powerup.domain.model.orders.OrderResponseModel;

import java.util.List;

public interface IOrderServicePort {
    void saveOrder(OrderRequestModel orderModel);

    List<OrderResponseModel> getAllOrdersWithPagination(Integer page, Integer size, String status);

    void takeOrderAndUpdateStatus(Long orderId, String status);

    void updateAndNotifyOrderReady(Long orderId);

    void deliverOrder(Long orderId, String pin);

    void cancelOrder(Long orderId);

    String timeDifferenceForOrders(Long orderId);
}
