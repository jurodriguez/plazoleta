package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    PHONE_NUMBER_INCORRECT("The number phone structure is incorrect"),
    NIT_INCORRECT("The nit structure is incorrect"),
    OWNER_INVALID("The owner of the restaurant is invalid"),
    RESTAURANT_NAME_INVALID("The restaurant name is invalid"),
    PRICE_INVALID("The price is invalid"),
    RESTAURANT_ID_INVALID("The restaurant ID is invalid"),
    DISH_NOT_EXIST("The dish doesn't exist");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}