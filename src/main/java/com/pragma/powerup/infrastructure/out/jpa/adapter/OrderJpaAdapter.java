package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.common.exception.NoDataFoundException;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderDish;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderDishEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderDishEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderDishRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;

    private final IOrderEntityMapper orderEntityMapper;

    private final IOrderDishRepository orderDishRepository;

    private final IOrderDishEntityMapper orderDishEntityMapper;

    @Override
    public Order saveOrder(Order order) {
        OrderEntity orderEntity = orderRepository.save(orderEntityMapper.toEntity(order));
        return orderEntityMapper.toOrder(orderEntity);
    }

    @Override
    public Boolean existsByCustomerIdAndStatus(Long id, String status) {
        return orderRepository.existsByCustomerIdAndStatus(id, status);
    }

    @Override
    public void saveOrderDish(List<OrderDish> orderDishModels) {
        List<OrderDishEntity> orderDishEntities = new ArrayList<>();
        for (OrderDish orderDish : orderDishModels) {
            orderDishEntities.add(orderDishEntityMapper.toEntity(orderDish));
        }
        orderDishRepository.saveAll(orderDishEntities);
    }

    @Override
    public List<Order> getAllOrdersWithPagination(Integer page, Integer size, Long restaurantId, String status) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<OrderEntity> orderEntityPage = orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);
        if (orderEntityPage.isEmpty()) {
            throw new NoDataFoundException();
        }
        return orderEntityPage
                .stream().map(orderEntityMapper::toOrder).collect(Collectors.toList());
    }

    @Override
    public List<OrderDish> getAllOrdersByOrderId(Long orderId) {
        List<OrderDishEntity> orderDishEntities = orderDishRepository.findByOrderId(orderId);
        if (orderDishEntities.isEmpty()) {
            throw new NoDataFoundException();
        }
        return orderDishEntityMapper.toOrderDishList(orderDishEntities);
    }

    @Override
    public Boolean existsByIdAndStatus(Long id, String status) {
        return orderRepository.existsByIdAndStatus(id, status);
    }

    @Override
    public Order getOrderById(Long id) {
        Optional<OrderEntity> orderEntityOptional = orderRepository.findById(id);
        OrderEntity orderEntity = orderEntityOptional.orElse(null);
        return orderEntityMapper.toOrder(orderEntity);
    }
}
