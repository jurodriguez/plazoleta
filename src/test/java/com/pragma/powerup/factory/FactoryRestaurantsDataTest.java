package com.pragma.powerup.factory;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.RestaurantEmployee;
import com.pragma.powerup.domain.model.User;

public class FactoryRestaurantsDataTest {

    public static Restaurant getRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(20L);
        restaurant.setName("ElCorral");
        restaurant.setAddress("cabecera");
        restaurant.setNit("231321");
        restaurant.setPhone("+573154571321");
        restaurant.setLogoUrl("https://cdn.logo.com/hotlink-ok/logo-social.png");
        restaurant.setOwnerId(1L);

        return restaurant;
    }

    public static User getUser() {
        User user = new User();
        user.setRoleId(2L);

        return user;
    }

    public static RestaurantEmployee getRestaurantEmployee() {
        RestaurantEmployee restaurantEmployee = new RestaurantEmployee();
        restaurantEmployee.setId(1L);
        restaurantEmployee.setRestaurantId(20L);
        restaurantEmployee.setEmployeeId(1L);

        return restaurantEmployee;
    }
}
