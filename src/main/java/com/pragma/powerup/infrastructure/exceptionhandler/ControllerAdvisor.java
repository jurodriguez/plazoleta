package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.infrastructure.exception.NitException;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import com.pragma.powerup.infrastructure.exception.OwnerInvalidException;
import com.pragma.powerup.infrastructure.exception.PhoneNumberException;
import com.pragma.powerup.infrastructure.exception.PriceInvalidException;
import com.pragma.powerup.infrastructure.exception.RestaurantIdInvalidException;
import com.pragma.powerup.infrastructure.exception.RestaurantNameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(NoDataFoundException ignoredNoDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
    }

    @ExceptionHandler(NitException.class)
    public ResponseEntity<Map<String, String>> handleNitException(NitException ignoredNitException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.NIT_INCORRECT.getMessage()));
    }

    @ExceptionHandler(OwnerInvalidException.class)
    public ResponseEntity<Map<String, String>> handleOwnerInvalidException(OwnerInvalidException ignoredOwnerInvalidException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.OWNER_INVALID.getMessage()));
    }

    @ExceptionHandler(PhoneNumberException.class)
    public ResponseEntity<Map<String, String>> handlePhoneNumberException(PhoneNumberException ignoredPhoneNumberException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.PHONE_NUMBER_INCORRECT.getMessage()));
    }

    @ExceptionHandler(RestaurantNameException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantNameException(RestaurantNameException ignoredRestaurantNameException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANT_NAME_INVALID.getMessage()));
    }

    @ExceptionHandler(RestaurantIdInvalidException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantIdInvalidException(RestaurantIdInvalidException ignoredRestaurantIdInvalidException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANT_ID_INVALID.getMessage()));
    }

    @ExceptionHandler(PriceInvalidException.class)
    public ResponseEntity<Map<String, String>> handlePriceInvalidException(PriceInvalidException ignoredPriceInvalidException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.PRICE_INVALID.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception exception) {
        log.info("Exception arrives:" + exception.getClass().toString());
        String messageException;
        if (exception.getClass().toString().equals("class jakarta.validation.ConstraintViolationException")) {
            messageException = exception.getMessage();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap(exception.getClass().toString(), exception.getMessage()));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, messageException));
    }

}