package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RestaurantRequestDto {
    @NotBlank(message = "Name field is required")
    private String name;
    @NotBlank(message = "The address field is required")
    private String address;
    @NotNull(message = "The owner identifier field is required")
    private Long ownerId;
    @NotBlank(message = "The address field is required")
    @Size(max = 13)
    private String phone;
    @NotBlank(message = "The logo Url field is required")
    private String logoUrl;
    @NotBlank(message = "The nit field is required")
    private String nit;
}