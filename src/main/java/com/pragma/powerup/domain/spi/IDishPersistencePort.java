package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Dish;

import java.util.List;

public interface IDishPersistencePort {

    Dish saveDish(Dish dish);

    Dish getDishById(Long id);

    List<Dish> findDishPaginationByRestaurantId(Long restaurantId, Long categoryId, Integer page, Integer size);
}
