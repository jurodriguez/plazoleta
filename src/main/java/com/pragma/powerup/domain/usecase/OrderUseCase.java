package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.common.exception.ClientHasAnOrderException;
import com.pragma.powerup.common.exception.DishIsInactiveException;
import com.pragma.powerup.common.exception.DishNotExistException;
import com.pragma.powerup.common.exception.DishRestaurantIdNotIsEqualsOrderException;
import com.pragma.powerup.common.exception.NoDataFoundException;
import com.pragma.powerup.common.exception.NumberDishRequiredException;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.enums.EOrderStatuses;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderDish;
import com.pragma.powerup.domain.model.orders.OrderDishRequestModel;
import com.pragma.powerup.domain.model.orders.OrderRequestModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.bearertoken.IToken;
import com.pragma.powerup.common.exception.OwnerNotAuthenticatedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;

    private final IToken token;

    private final IRestaurantPersistencePort restaurantPersistencePort;

    private final IDishPersistencePort dishPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IToken token, IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.token = token;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public void saveOrder(OrderRequestModel orderRequestModel) {
        Long customerId = getOwnerAuth();
        validatePendingOrder(customerId);
        Order order = createNewOrder(orderRequestModel.getRestaurantId(), customerId);
        List<OrderDishRequestModel> orderDishes = orderRequestModel.getOrderDishes();
        validateOrderDishes(orderDishes, order);

        order = orderPersistencePort.saveOrder(order);

        List<OrderDish> orderDishesSave = createOrderDishList(orderDishes, order);
        orderPersistencePort.saveOrderDish(orderDishesSave);
    }

    private List<OrderDish> createOrderDishList(List<OrderDishRequestModel> orderDishes, Order order) {
        List<OrderDish> resultList = new ArrayList<>();
        for (int i = 0; i < orderDishes.size(); i++) {
            Dish dish = dishPersistencePort.getDishById(orderDishes.get(i).getDishId());
            OrderDish orderDish = new OrderDish(null, order.getId(), dish.getId(), orderDishes.get(i).getNumber().intValue());
            resultList.add(orderDish);
        }

        return resultList;
    }

    private void validateOrderDishes(List<OrderDishRequestModel> orderDishes, Order order) {
        if (orderDishes == null || orderDishes.isEmpty()) throw new NoDataFoundException();
        for (int i = 0; i < orderDishes.size(); i++) {
            if (orderDishes.get(i).getNumber() == null || orderDishes.get(i).getNumber().intValue() == 0)
                throw new NumberDishRequiredException();
            Dish dish = dishPersistencePort.getDishById(orderDishes.get(i).getDishId());
            if (dish == null) throw new DishNotExistException();
            if (dish.getRestaurantId() != order.getRestaurantId())
                throw new DishRestaurantIdNotIsEqualsOrderException();
            if (!dish.getActive()) throw new DishIsInactiveException();
        }
    }

    private Long getOwnerAuth() {
        String bearerToken = token.getBearerToken();
        if (bearerToken == null) throw new OwnerNotAuthenticatedException();
        return token.getUserAuthenticatedId(bearerToken);
    }

    public void validatePendingOrder(Long customerId) {
        if (orderPersistencePort.existsByCustomerIdAndStatus(customerId, EOrderStatuses.PENDING.getName()) ||
                orderPersistencePort.existsByCustomerIdAndStatus(customerId, EOrderStatuses.PENDING.getName()) ||
                orderPersistencePort.existsByCustomerIdAndStatus(customerId, EOrderStatuses.PENDING.getName()))
            throw new ClientHasAnOrderException();
    }

    private Order createNewOrder(Long restaurantId, Long customerId) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setDate(LocalDate.now());
        order.setStatus(EOrderStatuses.PENDING.getName());
        order.setChefId(null);
        order.setRestaurantId(restaurantId);

        return order;
    }
}
