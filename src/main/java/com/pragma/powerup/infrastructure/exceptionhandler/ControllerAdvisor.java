package com.pragma.powerup.infrastructure.exceptionhandler;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception exception) {
        log.info("Exception arrives:" + exception.getClass().toString());
        String messageException;
        switch (exception.getClass().toString()) {
            case "class javax.validation.ConstraintViolationException":
                messageException = exception.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.ClientHasAnOrderException":
                messageException = ExceptionResponse.CLIENT_HAS_AN_ORDER.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.DishIsInactiveException":
                messageException = ExceptionResponse.DISH_IS_INACTIVE.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.DishNotExistException":
                messageException = ExceptionResponse.DISH_NOT_EXIST.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.DishRestaurantIdNotIsEqualsOrderException":
                messageException = ExceptionResponse.DISH_RESTAURANT_NOT_EQUALS_ORDER.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.NitException":
                messageException = ExceptionResponse.NIT_INCORRECT.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.NoDataFoundException":
                messageException = ExceptionResponse.NO_DATA_FOUND.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.OwnerInvalidException":
                messageException = ExceptionResponse.OWNER_INVALID.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.OwnerNotAuthenticatedException":
                messageException = ExceptionResponse.OWNER_NOT_AUTHENTICATED.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.PhoneNumberException":
                messageException = ExceptionResponse.PHONE_NUMBER_INCORRECT.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.PriceInvalidException":
                messageException = ExceptionResponse.PRICE_INVALID.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.RestaurantIdInvalidException":
                messageException = ExceptionResponse.RESTAURANT_ID_INVALID.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.RestaurantNameException":
                messageException = ExceptionResponse.RESTAURANT_NAME_INVALID.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.NumberDishRequiredException":
                messageException = ExceptionResponse.NUMBER_DISH_REQUIRED.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.RestaurantEmployeeNotExistException":
                messageException = ExceptionResponse.RESTAURANT_EMPLOYEE_NOT_EXIST.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.OrderNotExistException":
                messageException = ExceptionResponse.ORDER_NOT_EXIST.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.RestaurantOrderMustBeEqualsRestaurantEmployeeException":
                messageException = ExceptionResponse.RESTAURANT_ORDER_MUST_BE_EQUALS_RESTAURANT_EMPLOYEE.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.PinNotIsEqualsException":
                messageException = ExceptionResponse.PIN_NOT_EQUALS.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.ClientAuthMustBeEqualsClientOrderException":
                messageException = ExceptionResponse.CUSTOMER_AUTH_MUST_BE_EQUALS.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.OnlyCanceledPendingOrdersException":
                messageException = ExceptionResponse.ONLY_CANCELED_PENDING_ORDERS.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.OrdersInPreparationOrReadyCannotBeCanceledException":
                messageException = ExceptionResponse.ORDERS_IN_PREPARATION_OR_READY_CANNOT_BE_CANCELED.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.CanceledOrdersCannotBeCanceledException":
                messageException = ExceptionResponse.CANCELED_ORDERS_CANNOT_BE_CANCELED.getMessage();
                break;
            case "class com.pragma.powerup.common.exception.StatusOfOrderInvalidException":
                messageException = ExceptionResponse.STATUS_OF_THE_ORDER_INVALID.getMessage();
                break;
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap(exception.getClass().toString(), exception.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, messageException));
    }

}