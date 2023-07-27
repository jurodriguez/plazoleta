package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;

import javax.validation.Valid;

public interface IRestaurantHandler {

    void saveRestaurant(@Valid RestaurantRequestDto restaurantRequestDto);

    RestaurantResponseDto getRestaurantByOwnerId(Long ownerId);

}