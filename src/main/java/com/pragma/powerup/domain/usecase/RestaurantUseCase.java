package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.util.UtilNumbers;
import com.pragma.powerup.infrastructure.exception.NitException;
import com.pragma.powerup.infrastructure.exception.OwnerInvalidException;
import com.pragma.powerup.infrastructure.exception.PhoneNumberException;
import com.pragma.powerup.infrastructure.exception.RestaurantNameException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        saveValidations(restaurant);
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    private void saveValidations(Restaurant restaurant) {
        nitValidation(restaurant.getNit());
        phoneValidation(restaurant.getPhone());
        nameRestaurantValidation(restaurant.getName());
        ownerValidation(restaurant.getOwnerId());
    }

    private static void phoneValidation(String phone) {
        String regex = "^(\\+?\\d{1,3})?\\d{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.matches()) {
            throw new PhoneNumberException();
        }
    }

    private static void nitValidation(String nit) {
        if (!UtilNumbers.onlyNumbers(nit)) {
            throw new NitException();
        }
    }

    private void ownerValidation(Long ownerId) {
        if (true) {
            throw new OwnerInvalidException();
        }
    }

    private void nameRestaurantValidation(String nameRestaurant) {
        if (UtilNumbers.onlyNumbers(nameRestaurant)) {
            throw new RestaurantNameException();
        }
    }
}