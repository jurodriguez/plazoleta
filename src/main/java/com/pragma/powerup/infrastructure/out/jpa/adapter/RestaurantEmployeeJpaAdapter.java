package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantEmployee;
import com.pragma.powerup.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEmployeeEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantEmployeeRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantEmployeeJpaAdapter implements IRestaurantEmployeePersistencePort {

    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;

    @Override
    public RestaurantEmployee saveRestaurantEmployee(RestaurantEmployee restaurantEmployee) {
        RestaurantEmployeeEntity restaurantEmployeeEntity = restaurantEmployeeRepository.save(restaurantEmployeeEntityMapper.toEntity(restaurantEmployee));
        return restaurantEmployeeEntityMapper.toRestaurantEmployee(restaurantEmployeeEntity);
    }

    @Override
    public RestaurantEmployee findByEmployeeId(Long employeeId) {
        Optional<RestaurantEmployeeEntity> restaurantEmployeeEntityOptional = restaurantEmployeeRepository.findByEmployeeId(employeeId);
        RestaurantEmployeeEntity restaurantEmployeeEntity = restaurantEmployeeEntityOptional.orElse(null);
        return restaurantEmployeeEntityMapper.toRestaurantEmployee(restaurantEmployeeEntity);
    }
}
