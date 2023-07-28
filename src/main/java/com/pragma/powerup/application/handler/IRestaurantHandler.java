package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantPaginationResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;

import javax.validation.Valid;
import java.util.List;

public interface IRestaurantHandler {

    void saveRestaurant(@Valid RestaurantRequestDto restaurantRequestDto);

    RestaurantResponseDto getRestaurantByOwnerId(Long ownerId);

    List<RestaurantPaginationResponseDto> getRestaurantsWithPagination(Integer page, Integer size);
}