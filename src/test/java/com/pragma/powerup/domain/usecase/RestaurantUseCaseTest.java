package com.pragma.powerup.domain.usecase;

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

}
