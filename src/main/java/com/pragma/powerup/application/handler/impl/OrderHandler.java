package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.orders.OrderRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;

    private final IOrderRequestMapper orderRequestMapper;

    @Override
    public void saveOrder(OrderRequestDto orderRequest) {
        OrderRequestModel orderRequestModel = orderRequestMapper.toOrderRequestModel(orderRequest);
        orderServicePort.saveOrder(orderRequestModel);
    }
}
