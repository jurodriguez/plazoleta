package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.powerup.application.handler.IRestaurantEmployeeHandler;
import com.pragma.powerup.application.mapper.IRestaurantEmployeeRequestMapper;
import com.pragma.powerup.domain.api.IRestaurantEmployeeServicePort;
import com.pragma.powerup.domain.model.RestaurantEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantEmployeeHandler implements IRestaurantEmployeeHandler {

    private final IRestaurantEmployeeRequestMapper restaurantEmployeeRequestMapper;

    private final IRestaurantEmployeeServicePort restaurantEmployeeServicePort;

    @Override
    public void saveRestaurantEmployee(RestaurantEmployeeRequestDto restaurantEmployeeRequestDto) {
        RestaurantEmployee restaurantEmployee = restaurantEmployeeRequestMapper.toRestaurantEmployeeModel(restaurantEmployeeRequestDto);
        restaurantEmployeeServicePort.saveRestaurantEmployee(restaurantEmployee);

    }
}
