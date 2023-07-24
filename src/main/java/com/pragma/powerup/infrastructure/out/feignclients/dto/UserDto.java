package com.pragma.powerup.infrastructure.out.feignclients.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String documentNumber;
    private String cellPhone;
    private LocalDate birthDate;
    private String email;
    private String password;
    private RoleDto role;
}
