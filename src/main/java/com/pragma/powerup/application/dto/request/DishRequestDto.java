package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DishRequestDto {

    @NotBlank(message = "Name field is required")
    private String name;
    @NotNull(message = "The category id field is required")
    private Long categoryId;
    @NotBlank(message = "The description field is required")
    private String description;
    @NotNull(message = "The price field is required")
    private Integer price;
    @NotNull(message = "The restaurant id field is required")
    private Long restaurantId;
    @NotBlank(message = "The image url field is required")
    private String imageUrl;
    private Boolean active;
}
