package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDto {
    private Long id;
    private String name;
    private String address;
    private Long ownerId;
    private String phone;
    private String logoUrl;
    private String nit;
}
