package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.DishRequestDto;

import javax.validation.Valid;

public interface IDishHandler {

    void saveDish(@Valid DishRequestDto dishRequestDto);
}
