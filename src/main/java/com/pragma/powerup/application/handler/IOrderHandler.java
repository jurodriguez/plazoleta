package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;

import java.util.List;

public interface IOrderHandler {
    void saveOrder(OrderRequestDto orderRequest);

    List<OrderResponseDto> getAllOrdersWithPagination(Integer page, Integer size, String status);

    void takeOrderAndUpdateStatus(Long orderId, String status);

    void updateAndNotifyOrderReady(Long orderId);
}
