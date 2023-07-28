package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Dish;

import java.util.List;

public interface IDishServicePort {

    void saveDish(Dish dish);

    void updateDish(Long id, Dish dishModel);

    void updateEnableDisableDish(Long dishId, Long flag);

    List<Dish> findDishPaginationByRestaurantId(Long restaurantId, Long categoryId, Integer page, Integer size);
}
