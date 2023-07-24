package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.factory.FactoryDishesDataTest;
import com.pragma.powerup.factory.FactoryRestaurantsDataTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class DishUseCaseTest {

    @InjectMocks
    DishUseCase dishUseCase;

    @Mock
    IDishPersistencePort dishPersistencePort;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Test
    void mustSaveDish() {
        Dish dish = FactoryDishesDataTest.getDish();
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();

        Mockito.when(restaurantPersistencePort.getRestaurantById(Mockito.anyLong())).thenReturn(restaurant);
        Mockito.when(restaurantPersistencePort.getRestaurantById(dish.getRestaurantId())).thenReturn(restaurant);

        dishUseCase.saveDish(dish);

        //Then
        Mockito.verify(restaurantPersistencePort).getRestaurantById(Mockito.anyLong());
        Mockito.verify(dishPersistencePort).saveDish(Mockito.any(Dish.class));
    }
}
