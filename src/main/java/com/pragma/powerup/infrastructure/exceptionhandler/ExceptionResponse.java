package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    PHONE_NUMBER_INCORRECT("The number phone structure is incorrect"),
    NIT_INCORRECT("The nit structure is incorrect"),
    OWNER_INVALID("The owner is invalid"),
    RESTAURANT_NAME_INVALID("The restaurant name is invalid"),
    PRICE_INVALID("The price is invalid"),
    RESTAURANT_ID_INVALID("The restaurant ID is invalid"),
    DISH_NOT_EXIST("The dish doesn't exist"),
    OWNER_NOT_AUTHENTICATED("The owner is not authenticated"),
    CLIENT_HAS_AN_ORDER("The client has an order"),
    DISH_IS_INACTIVE("The dish is inactive"),
    DISH_RESTAURANT_NOT_EQUALS_ORDER("The Dish restaurant id not equals order restaurant id"),
    NUMBER_DISH_REQUIRED("The number of dish is required"),
    RESTAURANT_EMPLOYEE_NOT_EXIST("The Restaurant employee does not exist"),
    ORDER_NOT_EXIST("The order does not exist"),
    RESTAURANT_ORDER_MUST_BE_EQUALS_RESTAURANT_EMPLOYEE("The restaurant of the order must be the same as the restaurant of the employee"),
    PIN_NOT_EQUALS("The PIN code is invalid"),
    CUSTOMER_AUTH_MUST_BE_EQUALS("The authenticated client has to be the same client of the order"),
    ONLY_CANCELED_PENDING_ORDERS("Only pending orders can be canceled"),
    ORDERS_IN_PREPARATION_OR_READY_CANNOT_BE_CANCELED("Orders in preparation or ready cannot be canceled"),
    CANCELED_ORDERS_CANNOT_BE_CANCELED("Canceled orders cannot be canceled"),
    STATUS_OF_THE_ORDER_INVALID("The status of the order does not allow calculating the time");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}