package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishResponseDto {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private Boolean active;
    private Long categoryId;
    private Long restaurantId;
}
