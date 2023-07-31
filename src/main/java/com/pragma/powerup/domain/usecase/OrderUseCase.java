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
import com.pragma.powerup.domain.model.orders.OrderDishResponseModel;
import com.pragma.powerup.domain.model.orders.OrderRequestModel;
import com.pragma.powerup.domain.model.orders.OrderResponseModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantEmployeePersistencePort;
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

    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IToken token, IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.token = token;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
    }

    @Override
    public void saveOrder(OrderRequestModel orderRequestModel) {
        Long ownerAuthId = getOwnerAuth();
        validatePendingOrder(ownerAuthId);
        Order order = createNewOrder(orderRequestModel.getRestaurantId(), ownerAuthId);
        List<OrderDishRequestModel> orderDishes = orderRequestModel.getOrderDishes();
        validateOrderDishes(orderDishes, order);

        order = orderPersistencePort.saveOrder(order);

        List<OrderDish> orderDishesSave = createOrderDishList(orderDishes, order);
        orderPersistencePort.saveOrderDish(orderDishesSave);
    }

    @Override
    public List<OrderResponseModel> getAllOrdersWithPagination(Integer page, Integer size, String status) {
        Long ownerAuthId = getOwnerAuth();
        Long restaurantId = restaurantEmployeePersistencePort.findByEmployeeId(ownerAuthId).getRestaurantId();
        List<Order> orders = orderPersistencePort.getAllOrdersWithPagination(page, size, restaurantId, status);
        return createOrderResponseModelList(orders);
    }

    private List<OrderResponseModel> createOrderResponseModelList(List<Order> orders) {
        List<OrderResponseModel> orderResponseModelList = new ArrayList<>();
        for (Order order : orders) {
            OrderResponseModel orderResponseModel = new OrderResponseModel();
            orderResponseModel.setId(order.getId());
            orderResponseModel.setCustomerId(order.getCustomerId());
            orderResponseModel.setChefId(order.getChefId());
            orderResponseModel.setDate(order.getDate());
            orderResponseModel.setOrderDishes(new ArrayList<>());

            createOrderDishResponseModelList(orderResponseModel);

            orderResponseModelList.add(orderResponseModel);
        }

        return orderResponseModelList;
    }

    private void createOrderDishResponseModelList(OrderResponseModel orderResponseModel) {
        List<OrderDish> orderDishList = orderPersistencePort.getAllOrdersByOrderId(orderResponseModel.getId());
        for (OrderDish orderDish : orderDishList) {
            Dish dish = dishPersistencePort.getDishById(orderDish.getDishId());
            OrderDishResponseModel orderDishResponseModel = new OrderDishResponseModel();
            orderDishResponseModel.setId(dish.getId());
            orderDishResponseModel.setName(dish.getName());
            orderDishResponseModel.setPrice(dish.getPrice());
            orderDishResponseModel.setDescription(dish.getDescription());
            orderDishResponseModel.setImageUrl(dish.getImageUrl());
            orderDishResponseModel.setCategoryId(dish.getCategoryId());
            orderDishResponseModel.setNumber(orderDish.getNumber());

            orderResponseModel.getOrderDishes().add(orderDishResponseModel);
        }
    }

    private List<OrderDish> createOrderDishList(List<OrderDishRequestModel> orderDishes, Order order) {
        List<OrderDish> resultList = new ArrayList<>();
        for (OrderDishRequestModel orderDishRequestModel : orderDishes) {
            Dish dish = dishPersistencePort.getDishById(orderDishRequestModel.getDishId());
            OrderDish orderDish = new OrderDish(null, order.getId(), dish.getId(), orderDishRequestModel.getNumber().intValue());
            resultList.add(orderDish);
        }

        return resultList;
    }

    private void validateOrderDishes(List<OrderDishRequestModel> orderDishes, Order order) {
        if (orderDishes == null || orderDishes.isEmpty()) throw new NoDataFoundException();
        for (OrderDishRequestModel orderDishRequestModel : orderDishes) {
            if (orderDishRequestModel.getNumber() == null || orderDishRequestModel.getNumber().intValue() == 0)
                throw new NumberDishRequiredException();
            Dish dish = dishPersistencePort.getDishById(orderDishRequestModel.getDishId());
            if (dish == null) throw new DishNotExistException();
            if (dish.getRestaurantId().longValue() != order.getRestaurantId().longValue())
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
                orderPersistencePort.existsByCustomerIdAndStatus(customerId, EOrderStatuses.IN_PREPARATION.getName()) ||
                orderPersistencePort.existsByCustomerIdAndStatus(customerId, EOrderStatuses.READY.getName()))
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
