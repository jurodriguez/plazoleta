package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.enums.EOrderStatuses;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.orders.OrderDishRequestModel;
import com.pragma.powerup.domain.model.orders.OrderRequestModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.bearertoken.IToken;
import com.pragma.powerup.factory.FactoryDishesDataTest;
import com.pragma.powerup.factory.FactoryRestaurantsDataTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
class OrderUseCaseTest {

    @InjectMocks
    OrderUseCase orderUseCase;

    @Mock
    IOrderPersistencePort orderPersistencePort;

    @Mock
    IToken token;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    IDishPersistencePort dishPersistencePort;

    @Test
    void mustSaveOrder() {
        OrderRequestModel orderRequestModel = new OrderRequestModel();
        orderRequestModel.setRestaurantId(1L);
        List<OrderDishRequestModel> orderDishes = new ArrayList<>();
        OrderDishRequestModel orderDishRequestModel = new OrderDishRequestModel();
        orderDishRequestModel.setDishId(1L);
        orderDishRequestModel.setNumber(2L);
        orderDishes.add(orderDishRequestModel);
        orderRequestModel.setOrderDishes(orderDishes);

        validateToken();

        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();
        Mockito.when(restaurantPersistencePort.getRestaurantById(Mockito.anyLong())).thenReturn(restaurant);

        Dish dish = FactoryDishesDataTest.getDish();
        Mockito.when(dishPersistencePort.getDishById(Mockito.anyLong())).thenReturn(dish);

        Mockito.when(orderPersistencePort.saveOrder(Mockito.any(Order.class))).thenReturn(new Order());

        // Act
        orderUseCase.saveOrder(orderRequestModel);

        // Assert
        Mockito.verify(orderPersistencePort, Mockito.times(3)).existsByCustomerIdAndStatus(1L, EOrderStatuses.PENDING.getName());

        Mockito.verify(orderPersistencePort, Mockito.times(1)).saveOrder(Mockito.any(Order.class));
        Mockito.verify(orderPersistencePort, Mockito.times(1)).saveOrderDish(Mockito.anyList());
    }

    private void validateToken() {
        Mockito.when(token.getBearerToken()).thenReturn("bearer token");
        Mockito.when(token.getUserAuthenticatedId("bearer token")).thenReturn(1L);
    }
}
