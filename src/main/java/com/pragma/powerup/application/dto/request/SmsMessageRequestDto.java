package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SmsMessageRequestDto {

    @NotBlank(message = "The phone number is required")
    @Pattern(regexp = "^\\+57\\d{10}$", message = "The number must start with +57 followed by 10 digits (Colombian number)")
    private String number;

    @NotBlank(message = "The message is required")
    private String message;
}
