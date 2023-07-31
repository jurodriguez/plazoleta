package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.RestaurantEmployee;

public interface IRestaurantEmployeePersistencePort {
    RestaurantEmployee saveRestaurantEmployee(RestaurantEmployee restaurantEmployee);

    RestaurantEmployee findByEmployeeId(Long employeeId);
}
