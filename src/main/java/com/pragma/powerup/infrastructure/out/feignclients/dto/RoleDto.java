package com.pragma.powerup.infrastructure.out.feignclients.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Long id;
    private String name;
    private String description;
}
