package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Restaurant;

public interface IRestaurantPersistencePort {
    Restaurant saveRestaurant(Restaurant restaurant);

    Restaurant getRestaurantById(Long id);

    Restaurant getRestaurantByOwnerId(Long ownerId);
}