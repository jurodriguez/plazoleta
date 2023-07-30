package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    @NotNull(message = "The dish list is required")
    @Size(min = 1, message = "There must be at least one dish on the list")
    private List<OrderDishRequestDto> orderDishes;
    @NotNull(message = "The restaurant id is required")
    private Long restaurantId;
}
