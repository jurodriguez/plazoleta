package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {

    void saveRestaurant(Restaurant restaurant);

    Restaurant getRestaurantByOwnerId(Long ownerId);

    List<Restaurant> getRestaurantsWithPagination(Integer page, Integer size);
}