package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import com.pragma.powerup.application.mapper.IDishRequestMapper;
import com.pragma.powerup.application.mapper.IDishResponseMapper;
import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class DishHandler implements IDishHandler {


    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public void saveDish(DishRequestDto dishRequestDto) {
        Dish dish = dishRequestMapper.toObject(dishRequestDto);
        dishServicePort.saveDish(dish);
    }

    @Override
    public void updateDish(Long id, DishUpdateRequestDto dishUpdateRequestDto) {
        Dish dishModel = dishRequestMapper.toDishUpdate(dishUpdateRequestDto);
        dishServicePort.updateDish(id, dishModel);
    }

    @Override
    public void updateEnableDisableDish(Long dishId, Long flag) {
        dishServicePort.updateEnableDisableDish(dishId, flag);
    }

    @Override
    public List<DishResponseDto> findDishPaginationByRestaurantId(Long restaurantId, Long categoryId, Integer page, Integer size) {
        return dishResponseMapper.toResponseList(dishServicePort.findDishPaginationByRestaurantId(restaurantId, categoryId, page, size));
    }
}
