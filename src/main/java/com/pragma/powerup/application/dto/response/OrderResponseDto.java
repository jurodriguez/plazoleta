package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {

    private Long id;
    private Long customerId;
    private Long chefId;
    private Date date;
    private List<OrderDishResponseDto> orderDishes;
}
