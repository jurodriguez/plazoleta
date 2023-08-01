package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.common.exception.ClientHasAnOrderException;
import com.pragma.powerup.common.exception.DishIsInactiveException;
import com.pragma.powerup.common.exception.DishNotExistException;
import com.pragma.powerup.common.exception.DishRestaurantIdNotIsEqualsOrderException;
import com.pragma.powerup.common.exception.NoDataFoundException;
import com.pragma.powerup.common.exception.NumberDishRequiredException;
import com.pragma.powerup.common.exception.OrderNotExistException;
import com.pragma.powerup.common.exception.RestaurantOrderMustBeEqualsRestaurantEmployeeException;
import com.pragma.powerup.common.exception.RestaurantEmployeeNotExistException;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.enums.EOrderStatuses;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderDish;
import com.pragma.powerup.domain.model.RestaurantEmployee;
import com.pragma.powerup.domain.model.SmsMessageModel;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.model.orders.OrderDishRequestModel;
import com.pragma.powerup.domain.model.orders.OrderDishResponseModel;
import com.pragma.powerup.domain.model.orders.OrderRequestModel;
import com.pragma.powerup.domain.model.orders.OrderResponseModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.domain.spi.bearertoken.IToken;
import com.pragma.powerup.common.exception.OwnerNotAuthenticatedException;
import com.pragma.powerup.domain.spi.feignclients.ITwilioFeignClientPort;
import com.pragma.powerup.domain.spi.feignclients.IUserFeignClientPort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;

    private final IToken token;

    private final IDishPersistencePort dishPersistencePort;

    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    private final IUserFeignClientPort userFeignClientPort;

    private final ITwilioFeignClientPort twilioFeignClientPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IToken token, IDishPersistencePort dishPersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, IUserFeignClientPort userFeignClientPort, ITwilioFeignClientPort twilioFeignClientPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.token = token;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userFeignClientPort = userFeignClientPort;
        this.twilioFeignClientPort = twilioFeignClientPort;
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

    @Override
    public void takeOrderAndUpdateStatus(Long orderId, String status) {
        Long ownerAuthId = getOwnerAuth();
        validationsToUpdate(orderId, status);
        RestaurantEmployee restaurantEmployee = getRestaurantEmployeeById(ownerAuthId);
        Order order = getOrderById(orderId);
        validateOrderRestaurantAndRestaurantEmployee(order.getRestaurantId(), restaurantEmployee.getRestaurantId());

        order.setChefId(restaurantEmployee.getEmployeeId());
        order.setStatus(status);

        orderPersistencePort.saveOrder(order);
    }

    @Override
    public void updateAndNotifyOrderReady(Long orderId) {
        Long ownerAuthId = getOwnerAuth();
        validateIfExistByOrderIdAndStatus(orderId, EOrderStatuses.IN_PREPARATION.getName());
        RestaurantEmployee restaurantEmployee = getRestaurantEmployeeById(ownerAuthId);
        Order order = getOrderById(orderId);
        validateOrderRestaurantAndRestaurantEmployee(order.getRestaurantId(), restaurantEmployee.getRestaurantId());

        order.setStatus(EOrderStatuses.READY.getName());
        orderPersistencePort.saveOrder(order);

        User user = userFeignClientPort.getUserById(order.getCustomerId());
        String pin = validatePin(user);
        String message = "Good day, mr(s) " + user.getName().toUpperCase() + ", your order is now ready to pick up.\nRemember to show the next pin " + pin + " to be able to deliver your order.";
        sendMessage(message);
    }

    private void sendMessage(String message) {
        String phoneNumber = "+573175324391";
        SmsMessageModel smsMessageModel = new SmsMessageModel(phoneNumber, message);
        twilioFeignClientPort.sendSmsMessage(smsMessageModel);
    }

    public String validatePin(User user) {
        String documentPin = user.getDocumentNumber();
        String namePin = user.getName();
        String lastNamePin = user.getLastName();
        return namePin.substring(namePin.length() - 2) + documentPin.substring(documentPin.length() - 4) + lastNamePin.substring(lastNamePin.length() - 2);
    }

    private void validateOrderRestaurantAndRestaurantEmployee(Long restaurantOrderId, Long restaurantEmployeeId) {
        if (restaurantOrderId == null || restaurantEmployeeId == null || restaurantOrderId.longValue() != restaurantEmployeeId.longValue())
            throw new RestaurantOrderMustBeEqualsRestaurantEmployeeException();
    }

    private Order getOrderById(Long orderId) {
        Order order = orderPersistencePort.getOrderById(orderId);
        if (order == null) throw new OrderNotExistException();
        return order;
    }

    private RestaurantEmployee getRestaurantEmployeeById(Long id) {
        RestaurantEmployee restaurantEmployee = restaurantEmployeePersistencePort.findByEmployeeId(id);
        if (restaurantEmployee == null) throw new RestaurantEmployeeNotExistException();
        return restaurantEmployee;
    }

    private void validationsToUpdate(Long orderId, String status) {
        if (!status.equals(EOrderStatuses.IN_PREPARATION.getName())) throw new NoDataFoundException();
        validateIfExistByOrderIdAndStatus(orderId, EOrderStatuses.PENDING.getName());
    }

    private void validateIfExistByOrderIdAndStatus(Long orderId, String orderStatus) {
        if (Boolean.FALSE.equals(orderPersistencePort.existsByIdAndStatus(orderId, orderStatus)))
            throw new NoDataFoundException();
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
