package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantEmployeeServicePort;
import com.pragma.powerup.domain.model.RestaurantEmployee;
import com.pragma.powerup.domain.spi.IRestaurantEmployeePersistencePort;

public class RestaurantEmployeeUseCase implements IRestaurantEmployeeServicePort {
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    public RestaurantEmployeeUseCase(IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort) {
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
    }

    @Override
    public void saveRestaurantEmployee(RestaurantEmployee restaurantEmployee) {
        restaurantEmployeePersistencePort.saveRestaurantEmployee(restaurantEmployee);
    }

}
