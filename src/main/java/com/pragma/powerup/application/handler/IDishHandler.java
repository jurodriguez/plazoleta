package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;

import javax.validation.Valid;
import java.util.List;

public interface IDishHandler {

    void saveDish(@Valid DishRequestDto dishRequestDto);

    void updateDish(Long id, DishUpdateRequestDto dishUpdateRequestDto);

    void updateEnableDisableDish(Long dishId, Long flag);

    List<DishResponseDto> findDishPaginationByRestaurantId(Long restaurantId, Long categoryId, Integer page, Integer size);
}
