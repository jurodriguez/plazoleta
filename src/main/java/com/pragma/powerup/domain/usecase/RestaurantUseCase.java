package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.enums.ERoles;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.feignclients.IUserFeignClientPort;
import com.pragma.powerup.domain.util.UtilNumbers;
import com.pragma.powerup.common.exception.NitException;
import com.pragma.powerup.common.exception.OwnerInvalidException;
import com.pragma.powerup.common.exception.PhoneNumberException;
import com.pragma.powerup.common.exception.RestaurantNameException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;

    private final IUserFeignClientPort userFeignClientPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserFeignClientPort userFeignClientPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignClientPort = userFeignClientPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        saveValidations(restaurant);
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant getRestaurantByOwnerId(Long ownerId) {
        return restaurantPersistencePort.getRestaurantByOwnerId(ownerId);
    }

    @Override
    public List<Restaurant> getRestaurantsWithPagination(Integer page, Integer size) {
        return restaurantPersistencePort.getRestaurantsWithPagination(page, size);
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
        User user = userFeignClientPort.getUserById(ownerId);
        if (user != null && user.getRoleId() != null) {
            if (user.getRoleId().longValue() != ERoles.OWNER.getId().longValue()) {
                throw new OwnerInvalidException();
            }
        } else {
            throw new OwnerInvalidException();
        }
    }

    private void nameRestaurantValidation(String nameRestaurant) {
        if (UtilNumbers.onlyNumbers(nameRestaurant)) {
            throw new RestaurantNameException();
        }
    }
}