package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderDishRequestDto {
    @NotNull(message = "The dish id is required")
    private Long dishId;
    @NotNull(message = "The number of dishes is required")
    private Long number;
}
