package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DishUpdateRequestDto {

    @NotNull(message = "The price field is required")
    private Integer price;

    @NotBlank(message = "The description field is required")
    private String description;
}
