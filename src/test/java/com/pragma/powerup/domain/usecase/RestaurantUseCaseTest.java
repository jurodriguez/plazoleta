package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.common.exception.NitException;
import com.pragma.powerup.common.exception.OwnerInvalidException;
import com.pragma.powerup.common.exception.PhoneNumberException;
import com.pragma.powerup.common.exception.RestaurantNameException;
import com.pragma.powerup.domain.enums.ERoles;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.feignclients.IUserFeignClientPort;
import com.pragma.powerup.factory.FactoryRestaurantsDataTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
class RestaurantUseCaseTest {

    @InjectMocks
    RestaurantUseCase restaurantUseCase;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    IUserFeignClientPort userFeignClientPort;

    @Test
    void mustSaveRestaurant() {
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();
        User user = FactoryRestaurantsDataTest.getUser();

        Mockito.when(userFeignClientPort.getUserById(restaurant.getOwnerId())).thenReturn(user);
        Mockito.when(userFeignClientPort.getUserById(Mockito.anyLong())).thenReturn(user);

        restaurantUseCase.saveRestaurant(restaurant);

        //Then
        Mockito.verify(userFeignClientPort).getUserById(Mockito.anyLong());
        Mockito.verify(restaurantPersistencePort).saveRestaurant(Mockito.any(Restaurant.class));
    }

    @Test
    void mustSaveRestaurantWithPhoneNumberException() {
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();
        User user = FactoryRestaurantsDataTest.getUser();
        restaurant.setPhone("a3214587987");

        assertThrows(PhoneNumberException.class, () -> restaurantUseCase.saveRestaurant(restaurant));

        //Then
        Mockito.verify(restaurantPersistencePort, Mockito.never()).saveRestaurant(Mockito.any(Restaurant.class));
    }

    @Test
    void mustSaveRestaurantWithNitException() {
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();
        User user = FactoryRestaurantsDataTest.getUser();
        restaurant.setNit("a3214587987");

        assertThrows(NitException.class, () -> restaurantUseCase.saveRestaurant(restaurant));

        //Then
        Mockito.verify(restaurantPersistencePort, Mockito.never()).saveRestaurant(Mockito.any(Restaurant.class));
    }

    @Test
    void mustSaveRestaurantWithRestaurantNameException() {
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();
        User user = FactoryRestaurantsDataTest.getUser();
        restaurant.setName("654897");

        assertThrows(RestaurantNameException.class, () -> restaurantUseCase.saveRestaurant(restaurant));

        //Then
        Mockito.verify(restaurantPersistencePort, Mockito.never()).saveRestaurant(Mockito.any(Restaurant.class));
    }

    @Test
    void mustSaveRestaurantWithOwnerInvalidException() {
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();
        User user = FactoryRestaurantsDataTest.getUser();
        user.setRoleId(ERoles.EMPLOYEE.getId());

        assertThrows(OwnerInvalidException.class, () -> restaurantUseCase.saveRestaurant(restaurant));

        //Then
        Mockito.verify(restaurantPersistencePort, Mockito.never()).saveRestaurant(Mockito.any(Restaurant.class));
    }

    @Test
    void mustGetARestaurantByOwnerId() {
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();

        Mockito.when(restaurantPersistencePort.getRestaurantByOwnerId(Mockito.anyLong())).thenReturn(restaurant);

        restaurantUseCase.getRestaurantByOwnerId(restaurant.getOwnerId());
        Mockito.verify(restaurantPersistencePort).getRestaurantByOwnerId(Mockito.anyLong());
    }

    @Test
    void getRestaurantsWithPagination() {
        // configure test values
        Integer page = 1;
        Integer size = 10;
        List<Restaurant> expectedList = Arrays.asList(new Restaurant(), new Restaurant(), new Restaurant());

        // configure the mock to return the expected results
        Mockito.when(restaurantPersistencePort.getRestaurantsWithPagination(page, size)).thenReturn(expectedList);

        // Run test method
        List<Restaurant> resultList = restaurantUseCase.getRestaurantsWithPagination(page, size);

        // Verify that the results are as expected
        assertEquals(expectedList, resultList);

        // Verify that the mock method has been called
        Mockito.verify(restaurantPersistencePort).getRestaurantsWithPagination(page, size);
    }
}
