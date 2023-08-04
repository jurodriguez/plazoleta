package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TraceabilityResponseDto {

    private String orderId;
    @NotBlank(message = "The customer email is required")
    private String customerEmail;
    @NotBlank(message = "The date is required")
    private String date;
    private String lastStatus;
    @NotBlank(message = "The new status is required")
    private String newStatus;
    private String employeeEmail;
}
