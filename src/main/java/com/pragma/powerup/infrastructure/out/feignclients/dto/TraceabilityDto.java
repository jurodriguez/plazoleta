package com.pragma.powerup.infrastructure.out.feignclients.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraceabilityDto {

    private String id;
    private String orderId;
    private String customerEmail;
    private String date;
    private String lastStatus;
    private String newStatus;
    private String employeeEmail;
}
