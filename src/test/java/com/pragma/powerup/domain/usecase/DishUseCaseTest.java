package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.common.exception.OwnerInvalidException;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.bearertoken.IToken;
import com.pragma.powerup.factory.FactoryDishesDataTest;
import com.pragma.powerup.factory.FactoryRestaurantsDataTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
class DishUseCaseTest {

    @InjectMocks
    DishUseCase dishUseCase;

    @Mock
    IDishPersistencePort dishPersistencePort;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IToken token;

    @Test
    void mustSaveDish() {
        Dish dish = FactoryDishesDataTest.getDish();
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();

        Mockito.when(restaurantPersistencePort.getRestaurantById(Mockito.anyLong())).thenReturn(restaurant);
        Mockito.when(restaurantPersistencePort.getRestaurantById(dish.getRestaurantId())).thenReturn(restaurant);
        validateToken(1L);

        dishUseCase.saveDish(dish);

        //Then
        Mockito.verify(restaurantPersistencePort).getRestaurantById(Mockito.anyLong());
        Mockito.verify(dishPersistencePort).saveDish(Mockito.any(Dish.class));
    }

    @Test
    void mustSaveDishWithOwnerInvalidException() {
        Dish dish = FactoryDishesDataTest.getDish();
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();

        Mockito.when(restaurantPersistencePort.getRestaurantById(Mockito.anyLong())).thenReturn(restaurant);
        Mockito.when(restaurantPersistencePort.getRestaurantById(dish.getRestaurantId())).thenReturn(restaurant);
        validateToken(2L);

        assertThrows(OwnerInvalidException.class, () -> dishUseCase.saveDish(dish));

        //Then
        Mockito.verify(restaurantPersistencePort).getRestaurantById(Mockito.anyLong());
        Mockito.verify(dishPersistencePort, Mockito.never()).saveDish(Mockito.any(Dish.class));
    }

    @Test
    void mustUpdateDish() {
        Dish previusDish = FactoryDishesDataTest.getDish();
        Dish newDish = FactoryDishesDataTest.getNewDish();
        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();

        previusDish.setDescription(newDish.getDescription());
        previusDish.setPrice(newDish.getPrice());

        Mockito.when(dishPersistencePort.getDishById(Mockito.anyLong())).thenReturn(previusDish);
        Mockito.when(dishPersistencePort.getDishById(previusDish.getId())).thenReturn(previusDish);

        validateToken(1L);

        Mockito.when(restaurantPersistencePort.getRestaurantById(newDish.getRestaurantId())).thenReturn(restaurant);

        dishUseCase.updateDish(previusDish.getId(), previusDish);

        //Then
        Mockito.verify(dishPersistencePort).getDishById(Mockito.anyLong());

        Mockito.verify(token, Mockito.times(1)).getBearerToken();
        Mockito.verify(token, Mockito.times(1)).getUserAuthenticatedId("bearer token");

        Mockito.verify(restaurantPersistencePort, Mockito.times(1)).getRestaurantById(newDish.getRestaurantId());

        Mockito.verify(dishPersistencePort).saveDish(Mockito.any(Dish.class));
        assertEquals(previusDish.getDescription(), newDish.getDescription());
        assertEquals(previusDish.getPrice(), newDish.getPrice());
    }

    @Test
    void updateEnableDisableDish() {
        Long idDish = 1L;
        Long flag = 1L;
        Dish dish = new Dish();
        dish.setId(idDish);
        dish.setActive(false);
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(FactoryRestaurantsDataTest.getRestaurant().getOwnerId());
        dish.setRestaurantId(restaurant.getId());

        Mockito.when(dishPersistencePort.getDishById(idDish)).thenReturn(dish);
        validateToken(1L);
        Mockito.when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(restaurant);

        // Act
        dishUseCase.updateEnableDisableDish(idDish, flag);

        // Assert
        Assertions.assertTrue(dish.getActive());
    }

    private void validateToken(Long id) {
        Mockito.when(token.getBearerToken()).thenReturn("bearer token");
        Mockito.when(token.getUserAuthenticatedId("bearer token")).thenReturn(id);
    }

    @Test
    void findDishPaginationByRestaurantId() {
        Long restaurantId = 1L;
        Long categoryId = 1L;
        Integer page = 0;
        Integer size = 5;

        List<Dish> expectedList = new ArrayList<>();
        expectedList.add(FactoryDishesDataTest.getDish());
        expectedList.add(FactoryDishesDataTest.getNewDish());

        // configure the mock to return the expected results
        Mockito.when(dishPersistencePort.findDishPaginationByRestaurantId(restaurantId, categoryId, page, size)).thenReturn(expectedList);

        // Run test method
        List<Dish> resultList = dishUseCase.findDishPaginationByRestaurantId(restaurantId, categoryId, page, size);

        // Verify that the results are as expected
        assertEquals(expectedList, resultList);

        // Verify that the mock method has been called
        Mockito.verify(dishPersistencePort).findDishPaginationByRestaurantId(restaurantId, categoryId, page, size);
    }
}

