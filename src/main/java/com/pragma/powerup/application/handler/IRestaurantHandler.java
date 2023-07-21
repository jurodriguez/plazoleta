package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;

import javax.validation.Valid;

public interface IRestaurantHandler {

    void saveRestaurant(@Valid RestaurantRequestDto restaurantRequestDto);

}