package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.common.exception.OrdersInPreparationOrReadyCannotBeCanceledException;
import com.pragma.powerup.domain.enums.EOrderStatuses;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderDish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.RestaurantEmployee;
import com.pragma.powerup.domain.model.SmsMessageModel;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.model.orders.OrderDishRequestModel;
import com.pragma.powerup.domain.model.orders.OrderDishResponseModel;
import com.pragma.powerup.domain.model.orders.OrderRequestModel;
import com.pragma.powerup.domain.model.orders.OrderResponseModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.bearertoken.IToken;
import com.pragma.powerup.domain.spi.feignclients.ITraceabilityFeignClientPort;
import com.pragma.powerup.domain.spi.feignclients.ITwilioFeignClientPort;
import com.pragma.powerup.domain.spi.feignclients.IUserFeignClientPort;
import com.pragma.powerup.factory.FactoryDishesDataTest;
import com.pragma.powerup.factory.FactoryOrdersDataTest;
import com.pragma.powerup.factory.FactoryRestaurantsDataTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Mock
    IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    @Mock
    IUserFeignClientPort userFeignClientPort;

    @Mock
    ITwilioFeignClientPort twilioFeignClientPort;

    @Mock
    ITraceabilityFeignClientPort traceabilityFeignClientPort;

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
        validateTraceability();

        Restaurant restaurant = FactoryRestaurantsDataTest.getRestaurant();
        Mockito.when(restaurantPersistencePort.getRestaurantById(Mockito.anyLong())).thenReturn(restaurant);

        Dish dish = FactoryDishesDataTest.getDish();
        Mockito.when(dishPersistencePort.getDishById(Mockito.anyLong())).thenReturn(dish);

        Mockito.when(orderPersistencePort.saveOrder(Mockito.any(Order.class))).thenReturn(new Order());

        // Act
        orderUseCase.saveOrder(orderRequestModel);

        // Assert
        Mockito.verify(orderPersistencePort, Mockito.times(1)).existsByCustomerIdAndStatus(1L, EOrderStatuses.PENDING.getName());
        Mockito.verify(orderPersistencePort, Mockito.times(1)).existsByCustomerIdAndStatus(1L, EOrderStatuses.IN_PREPARATION.getName());
        Mockito.verify(orderPersistencePort, Mockito.times(1)).existsByCustomerIdAndStatus(1L, EOrderStatuses.READY.getName());
        Mockito.verify(orderPersistencePort, Mockito.times(1)).saveOrder(Mockito.any(Order.class));
        Mockito.verify(orderPersistencePort, Mockito.times(1)).saveOrderDish(Mockito.anyList());
        Mockito.verify(traceabilityFeignClientPort).saveTraceability(Mockito.any(Traceability.class));
    }

    private void validateTraceability() {
        User user = FactoryRestaurantsDataTest.getUser();
        User employee = FactoryRestaurantsDataTest.getUser();
        employee.setId(2L);
        Mockito.when(userFeignClientPort.getUserById(1L)).thenReturn(user);
        Mockito.when(userFeignClientPort.getUserById(2L)).thenReturn(employee);
    }

    @Test
    void getAllOrdersWithPagination() {
        RestaurantEmployee RestaurantEmployee = new RestaurantEmployee();
        RestaurantEmployee.setRestaurantId(1L);
        List<Order> orderList = new ArrayList<>();
        Order order = FactoryOrdersDataTest.getOrder();
        orderList.add(order);
        List<OrderDish> orderDishes = new ArrayList<>();
        OrderDish orderDish = FactoryOrdersDataTest.getOrderDish();
        orderDishes.add(orderDish);
        Dish dish = FactoryDishesDataTest.getDish();

        validateToken();

        Mockito.when(restaurantEmployeePersistencePort.findByEmployeeId(1L)).thenReturn(RestaurantEmployee);
        Mockito.when(orderPersistencePort.getAllOrdersWithPagination(1, 10, RestaurantEmployee.getRestaurantId(), EOrderStatuses.PENDING.getName())).thenReturn(orderList);
        Mockito.when(orderPersistencePort.getAllOrdersByOrderId(order.getId())).thenReturn(orderDishes);
        Mockito.when(dishPersistencePort.getDishById(orderDish.getDishId())).thenReturn(dish);

        // call the method under test
        List<OrderResponseModel> result = orderUseCase.getAllOrdersWithPagination(1, 10, EOrderStatuses.PENDING.getName());

        // verify the result
        assertEquals(1, result.size());
        OrderResponseModel orderResponseModel = result.get(0);
        assertEquals(order.getId(), orderResponseModel.getId());
        assertEquals(order.getCustomerId(), orderResponseModel.getCustomerId());
        assertEquals(order.getChefId(), orderResponseModel.getChefId());
        assertEquals(order.getDate(), orderResponseModel.getDate());
        assertEquals(1, orderResponseModel.getOrderDishes().size());
        OrderDishResponseModel orderDishResponseModel = orderResponseModel.getOrderDishes().get(0);
        assertEquals(dish.getId(), orderDishResponseModel.getId());
        assertEquals(dish.getName(), orderDishResponseModel.getName());
        assertEquals(dish.getPrice(), orderDishResponseModel.getPrice());
        assertEquals(dish.getDescription(), orderDishResponseModel.getDescription());
        assertEquals(dish.getImageUrl(), orderDishResponseModel.getImageUrl());
        assertEquals(dish.getCategoryId(), orderDishResponseModel.getCategoryId());
        assertEquals(orderDish.getNumber(), orderDishResponseModel.getNumber());
    }

    @Test
    void takeOrderAndUpdateStatus() {
        Long orderId = 1L;

        validateToken();
        validateTraceability();
        Mockito.when(orderPersistencePort.existsByIdAndStatus(orderId, EOrderStatuses.PENDING.getName())).thenReturn(Boolean.TRUE);

        Order order = FactoryOrdersDataTest.getOrder();
        Mockito.when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        RestaurantEmployee restaurantEmployee = FactoryRestaurantsDataTest.getRestaurantEmployee();

        Mockito.when(restaurantEmployeePersistencePort.findByEmployeeId(restaurantEmployee.getEmployeeId())).thenReturn(restaurantEmployee);

        // Act
        orderUseCase.takeOrderAndUpdateStatus(orderId, EOrderStatuses.IN_PREPARATION.getName());

        // Assert
        Mockito.verify(orderPersistencePort, Mockito.times(1)).existsByIdAndStatus(orderId, EOrderStatuses.PENDING.getName());
        Mockito.verify(orderPersistencePort, Mockito.times(1)).getOrderById(orderId);
        Mockito.verify(orderPersistencePort, Mockito.times(1)).saveOrder(order);
        Mockito.verify(traceabilityFeignClientPort).saveTraceability(Mockito.any(Traceability.class));
    }

    @Test
    void updateAndNotifyOrderReady() {
        Long orderId = 1L;

        // Mocking the dependencies of the method
        validateToken();
        validateTraceability();
        Mockito.when(orderPersistencePort.existsByIdAndStatus(orderId, EOrderStatuses.IN_PREPARATION.getName())).thenReturn(Boolean.TRUE);
        Mockito.when(restaurantEmployeePersistencePort.findByEmployeeId(1L)).thenReturn(FactoryRestaurantsDataTest.getRestaurantEmployee());
        Mockito.when(orderPersistencePort.getOrderById(orderId)).thenReturn(FactoryOrdersDataTest.getOrderWithStatus());
        Mockito.when(userFeignClientPort.getUserById(1L)).thenReturn(FactoryRestaurantsDataTest.getUser());

        // Calling the method under test
        orderUseCase.updateAndNotifyOrderReady(orderId);

        // Verifying that the method called the dependencies with the expected parameters
        Mockito.verify(orderPersistencePort).existsByIdAndStatus(orderId, EOrderStatuses.IN_PREPARATION.getName());
        Mockito.verify(token).getBearerToken();
        Mockito.verify(token).getUserAuthenticatedId("bearer token");
        Mockito.verify(restaurantEmployeePersistencePort).findByEmployeeId(1L);
        Mockito.verify(orderPersistencePort).getOrderById(orderId);
        Mockito.verify(orderPersistencePort).saveOrder(Mockito.any(Order.class));
        Mockito.verify(twilioFeignClientPort).sendSmsMessage(Mockito.any(SmsMessageModel.class));
        Mockito.verify(traceabilityFeignClientPort).saveTraceability(Mockito.any(Traceability.class));
    }

    @Test
    void deliverOrder() {
        Long orderId = 1L;
        String pin = "id7478os";

        Order order = FactoryOrdersDataTest.getOrder();
        order.setStatus(EOrderStatuses.READY.getName());
        User user = FactoryRestaurantsDataTest.getUser();

        validateToken();
        validateTraceability();
        Mockito.when(orderPersistencePort.existsByIdAndStatus(orderId, EOrderStatuses.READY.getName())).thenReturn(true);
        Mockito.when(restaurantEmployeePersistencePort.findByEmployeeId(1L)).thenReturn(FactoryRestaurantsDataTest.getRestaurantEmployee());
        Mockito.when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        Mockito.when(userFeignClientPort.getUserById(order.getCustomerId())).thenReturn(user);
        OrderUseCase orderUseCaseMock = Mockito.mock(OrderUseCase.class);
        Mockito.when(orderUseCaseMock.validatePin(user)).thenReturn("id7478os");

        // Execution
        orderUseCase.deliverOrder(orderId, pin);

        // Verification
        assertEquals(20L, order.getRestaurantId());
        Mockito.verify(orderPersistencePort).existsByIdAndStatus(orderId, EOrderStatuses.READY.getName());
        Mockito.verify(token).getBearerToken();
        Mockito.verify(token).getUserAuthenticatedId("bearer token");
        Mockito.verify(restaurantEmployeePersistencePort).findByEmployeeId(1L);
        Mockito.verify(orderPersistencePort).getOrderById(orderId);
        Mockito.verify(orderPersistencePort).saveOrder(order);
        Mockito.verify(traceabilityFeignClientPort).saveTraceability(Mockito.any(Traceability.class));
    }

    @Test
    void cancelOrder() {
        Long orderId = 1L;

        validateToken();
        validateTraceability();

        Order order = FactoryOrdersDataTest.getOrder();
        order.setStatus(EOrderStatuses.PENDING.getName());
        Mockito.when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        Mockito.when(orderPersistencePort.existsByIdAndStatus(orderId, EOrderStatuses.PENDING.getName())).thenReturn(true);

        // Calling Method
        orderUseCase.cancelOrder(orderId);

        // Verifying Method Calls
        Mockito.verify(orderPersistencePort, Mockito.times(1)).saveOrder(order);
        Mockito.verify(traceabilityFeignClientPort).saveTraceability(Mockito.any(Traceability.class));
    }

    @Test
    void cancelOrderExceptionStatusPreparationOrReady() {
        Long orderId = 1L;

        // Mocking Token
        validateToken();

        // Mocking Order
        Order order = FactoryOrdersDataTest.getOrder();
        order.setStatus(EOrderStatuses.IN_PREPARATION.getName());
        Mockito.when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        Mockito.when(orderPersistencePort.existsByIdAndStatus(orderId, EOrderStatuses.IN_PREPARATION.getName())).thenReturn(true);

        // Calling Method and Expecting Exception
        assertThrows(OrdersInPreparationOrReadyCannotBeCanceledException.class, () -> orderUseCase.cancelOrder(orderId));

        // Verifying Method Calls
        Mockito.verify(orderPersistencePort, Mockito.never()).saveOrder(order);
        Mockito.verify(twilioFeignClientPort, Mockito.times(1)).sendSmsMessage(Mockito.any(SmsMessageModel.class));
    }

    private void validateToken() {
        Mockito.when(token.getBearerToken()).thenReturn("bearer token");
        Mockito.when(token.getUserAuthenticatedId("bearer token")).thenReturn(1L);
    }
}
